package com.saurav.BlinkDeliver.service.payment;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.saurav.BlinkDeliver.dto.PaymentOrderResponseDto;
import com.saurav.BlinkDeliver.dto.PaymentRefundDto;
import com.saurav.BlinkDeliver.dto.PaymentVerificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Razorpay implementation of PaymentProcessor.
 * Handles all Razorpay-specific payment operations.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RazorpayPaymentProcessor implements PaymentProcessor {

    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @Override
    public PaymentOrderResponseDto createOrder(Long orderId, BigDecimal amount) throws RazorpayException {
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue()); // Amount in paise
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "order_rcptid_" + orderId);

            Order order = razorpayClient.orders.create(orderRequest);

            PaymentOrderResponseDto responseDto = new PaymentOrderResponseDto();
            responseDto.setGatewayOrderId(order.get("id"));
            responseDto.setCurrency("INR");
            responseDto.setAmount(amount.multiply(BigDecimal.valueOf(100)).intValue());
            responseDto.setKey(keyId);

            log.info("Razorpay order created successfully: {}", (Object) order.get("id"));
            return responseDto;
        } catch (RazorpayException e) {
            log.error("Error creating Razorpay order: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean verifyAndCapturePayment(PaymentVerificationDto verifyDto) throws RazorpayException {
        try {
            // Verify signature using Razorpay Utils
            JSONObject options = new JSONObject();
            options.put("razorpay_order_id", verifyDto.getGatewayOrderId());
            options.put("razorpay_payment_id", verifyDto.getGatewayPaymentId());
            options.put("razorpay_signature", verifyDto.getVerificationToken());

            boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

            if (!isValid) {
                log.error("Payment signature verification failed for payment: {}", verifyDto.getGatewayPaymentId());
                return false;
            }

            // For testing: Skip actual Razorpay capture if using test payment ID
            if (verifyDto.getGatewayPaymentId().startsWith("pay_test")) {
                log.warn("TEST MODE: Skipping Razorpay capture for test payment ID: {}",
                        verifyDto.getGatewayPaymentId());
                return true;
            }

            // Try to capture payment (may fail if auto-capture is enabled in Razorpay
            // dashboard)
            try {
                JSONObject captureRequest = new JSONObject();
                captureRequest.put("amount", verifyDto.getAmount().multiply(BigDecimal.valueOf(100)).intValue());
                captureRequest.put("currency", "INR");

                Payment payment = razorpayClient.payments.capture(verifyDto.getGatewayPaymentId(), captureRequest);
                log.info("Payment captured successfully: {}", (Object) payment.get("id"));
            } catch (RazorpayException captureEx) {
                // If payment is already captured (auto-capture enabled), that's fine
                if (captureEx.getMessage() != null && captureEx.getMessage().contains("already been captured")) {
                    log.info("Payment was already captured (auto-capture enabled): {}",
                            verifyDto.getGatewayPaymentId());
                } else {
                    throw captureEx;
                }
            }

            return true;
        } catch (RazorpayException e) {
            log.error("Error verifying/capturing payment: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String initiateRefund(PaymentRefundDto refundDto) throws RazorpayException {
        try {
            JSONObject refundRequest = new JSONObject();
            refundRequest.put("amount", refundDto.getAmount().multiply(BigDecimal.valueOf(100)).intValue());

            if (refundDto.getReason() != null) {
                JSONObject notes = new JSONObject();
                notes.put("reason", refundDto.getReason());
                refundRequest.put("notes", notes);
            }

            com.razorpay.Refund refund = razorpayClient.payments.refund(refundDto.getGatewayPaymentId(), refundRequest);

            String refundId = refund.get("id");
            log.info("Refund initiated successfully: {}", (Object) refundId);
            return refundId;
        } catch (RazorpayException e) {
            log.error("Error initiating refund: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String getKeyId() {
        return keyId;
    }
}
