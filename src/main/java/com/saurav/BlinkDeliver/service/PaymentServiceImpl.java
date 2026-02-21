package com.saurav.BlinkDeliver.service;

import com.razorpay.RazorpayException;
import com.saurav.BlinkDeliver.dto.PaymentOrderRequestDto;
import com.saurav.BlinkDeliver.dto.PaymentOrderResponseDto;
import com.saurav.BlinkDeliver.dto.PaymentRefundDto;
import com.saurav.BlinkDeliver.dto.PaymentVerificationDto;
import com.saurav.BlinkDeliver.entity.Order;
import com.saurav.BlinkDeliver.entity.Payment;
import com.saurav.BlinkDeliver.enums.OrderStatus;
import com.saurav.BlinkDeliver.enums.PaymentMethod;
import com.saurav.BlinkDeliver.enums.PaymentStatus;
import com.saurav.BlinkDeliver.exception.BadRequestException;
import com.saurav.BlinkDeliver.exception.ResourceNotFoundException;
import com.saurav.BlinkDeliver.repository.OrderRepository;
import com.saurav.BlinkDeliver.repository.PaymentRepository;
import com.saurav.BlinkDeliver.service.payment.PaymentProcessor;
import com.saurav.BlinkDeliver.service.payment.PaymentProcessorFactory;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

/**
 * Payment Service with Resilience4j for circuit breaking and retries.
 * Uses pessimistic locking to prevent double payments and race conditions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentProcessorFactory processorFactory;
    private final OrderService orderService;

    @Override
    @Async
    @Transactional
    @CircuitBreaker(name = "paymentService", fallbackMethod = "createPaymentFallback")
    @Retry(name = "paymentService")
    public CompletableFuture<PaymentOrderResponseDto> createPayment(PaymentOrderRequestDto requestDto, Long userId) {
        try {
            // Fetch order
            Order order = orderRepository.findById(requestDto.getOrderId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Order not found with id: " + requestDto.getOrderId()));

            // Verify order belongs to user
            if (!order.getUser().getUserId().equals(userId)) {
                throw new BadRequestException("Order does not belong to user");
            }

            // Check if order is in PENDING state
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new BadRequestException("Order is not in PENDING state");
            }

            // Check if payment already exists
            if (paymentRepository.findByOrder_OrderId(requestDto.getOrderId()).isPresent()) {
                throw new BadRequestException("Payment already exists for this order");
            }

            // Validate amount matches order total
            if (requestDto.getAmount().compareTo(order.getTotalAmount()) != 0) {
                throw new BadRequestException("Payment amount mismatch: Expected " + order.getTotalAmount()
                        + " but got " + requestDto.getAmount());
            }

            // Get payment processor
            PaymentProcessor processor = processorFactory.getProcessor(order.getPaymentMethod());

            // Create order in payment gateway
            PaymentOrderResponseDto responseDto = processor.createOrder(requestDto.getOrderId(),
                    requestDto.getAmount());

            // Save payment record
            Payment payment = new Payment();
            payment.setOrder(order);
            payment.setRazorpayOrderId(responseDto.getGatewayOrderId());
            payment.setAmount(requestDto.getAmount());
            payment.setStatus(PaymentStatus.PENDING);
            payment.setPaymentMethod(order.getPaymentMethod());

            paymentRepository.save(payment);

            log.info("Payment created successfully for order: {}", requestDto.getOrderId());
            return CompletableFuture.completedFuture(responseDto);
        } catch (RazorpayException e) {
            log.error("Payment gateway error creating payment: {}", e.getMessage());
            throw new BadRequestException("Failed to create payment: " + e.getMessage());
        }
    }

    @Override
    @Async
    @Transactional
    @CircuitBreaker(name = "paymentService", fallbackMethod = "verifyPaymentFallback")
    @Retry(name = "paymentService")
    public CompletableFuture<Boolean> verifyPayment(PaymentVerificationDto verifyDto, Long userId) {
        try {
            // Fetch payment
            Payment payment = paymentRepository.findByRazorpayOrderId(verifyDto.getGatewayOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Payment not found for order: " + verifyDto.getGatewayOrderId()));

            // Verify order belongs to user
            if (!payment.getOrder().getUser().getUserId().equals(userId)) {
                throw new BadRequestException("Payment does not belong to user");
            }

            // Check if payment is already completed
            if (payment.getStatus() == PaymentStatus.COMPLETED) {
                log.warn("Payment already completed for order: {}", verifyDto.getGatewayOrderId());
                return CompletableFuture.completedFuture(true);
            }

            // Validate amount matches payment record
            if (verifyDto.getAmount().compareTo(payment.getAmount()) != 0) {
                throw new BadRequestException("Payment amount mismatch: Expected " + payment.getAmount() + " but got "
                        + verifyDto.getAmount());
            }

            // Check if payment is in valid state for verification
            if (payment.getStatus() != PaymentStatus.PENDING && payment.getStatus() != PaymentStatus.PROCESSING) {
                throw new BadRequestException("Payment is not in a valid state for verification");
            }

            // Update status to PROCESSING
            payment.setStatus(PaymentStatus.PROCESSING);
            paymentRepository.save(payment);

            // Get payment processor and verify
            PaymentProcessor processor = processorFactory.getProcessor(payment.getPaymentMethod());
            boolean isVerified = processor.verifyAndCapturePayment(verifyDto);

            if (!isVerified) {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                log.error("Payment verification failed for order: {}", verifyDto.getGatewayOrderId());
                return CompletableFuture.completedFuture(false);
            }

            // Update payment record
            payment.setRazorpayPaymentId(verifyDto.getGatewayPaymentId());
            payment.setRazorpaySignature(verifyDto.getVerificationToken());
            payment.setStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(payment);

            // Finalize order (deduct stock, clear cart, update order status)
            try {
                orderService.completeOrder(userId, payment.getOrder().getOrderId()).join();
                log.info("Order finalized successfully for order: {}", payment.getOrder().getOrderId());
            } catch (Exception e) {
                log.error("CRITICAL: Payment captured but order completion failed for order: {}. Reason: {}",
                        payment.getOrder().getOrderId(), e.getMessage());
                // We do NOT rethrow here to ensure the Payment status remains COMPLETED
                // In a real system, this would trigger an alert or a reconciliation job
            }

            log.info("Payment verified for order: {}", payment.getOrder().getOrderId());
            return CompletableFuture.completedFuture(true);
        } catch (RazorpayException e) {
            log.error("Payment gateway error verifying payment: {}", e.getMessage());
            throw new BadRequestException("Failed to verify payment: " + e.getMessage());
        }
    }

    @Override
    @Async
    @Transactional
    @CircuitBreaker(name = "paymentService", fallbackMethod = "initiateRefundFallback")
    @Retry(name = "paymentService")
    public CompletableFuture<String> initiateRefund(PaymentRefundDto refundDto, Long userId) {
        try {
            // Fetch payment
            Payment payment = paymentRepository.findByRazorpayPaymentId(refundDto.getGatewayPaymentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Payment not found: " + refundDto.getGatewayPaymentId()));

            // Verify order belongs to user
            if (!payment.getOrder().getUser().getUserId().equals(userId)) {
                throw new BadRequestException("Payment does not belong to user");
            }

            // Check if payment is completed
            if (payment.getStatus() != PaymentStatus.COMPLETED) {
                throw new BadRequestException("Payment is not in COMPLETED state");
            }

            // Get payment processor and initiate refund
            PaymentProcessor processor = processorFactory.getProcessor(payment.getPaymentMethod());
            String refundId = processor.initiateRefund(refundDto);

            // Update payment status
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);

            log.info("Refund initiated successfully: {}", refundId);
            return CompletableFuture.completedFuture(refundId);
        } catch (RazorpayException e) {
            log.error("Payment gateway error initiating refund: {}", e.getMessage());
            throw new BadRequestException("Failed to initiate refund: " + e.getMessage());
        }
    }

    @Override
    @Async
    public CompletableFuture<String> getPaymentGatewayKey() {
        return CompletableFuture.completedFuture(processorFactory.getProcessor(PaymentMethod.UPI).getKeyId());
    }

    // Fallback methods for Circuit Breaker
    private CompletableFuture<PaymentOrderResponseDto> createPaymentFallback(PaymentOrderRequestDto requestDto,
            Long userId, Exception e) {
        log.error("Circuit breaker fallback for createPayment: {}", e.getMessage());
        throw new BadRequestException("Payment service is currently unavailable. Please try again later.");
    }

    private CompletableFuture<Boolean> verifyPaymentFallback(PaymentVerificationDto verifyDto, Long userId,
            Exception e) {
        log.error("Circuit breaker fallback for verifyPayment: {}", e.getMessage());
        throw new BadRequestException("Payment verification service is currently unavailable. Please try again later.");
    }

    private CompletableFuture<String> initiateRefundFallback(PaymentRefundDto refundDto, Long userId, Exception e) {
        log.error("Circuit breaker fallback for initiateRefund: {}", e.getMessage());
        throw new BadRequestException("Refund service is currently unavailable. Please try again later.");
    }
}
