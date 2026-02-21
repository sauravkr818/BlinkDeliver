package com.saurav.BlinkDeliver.service.payment;

import com.saurav.BlinkDeliver.dto.PaymentOrderResponseDto;
import com.saurav.BlinkDeliver.dto.PaymentRefundDto;
import com.saurav.BlinkDeliver.dto.PaymentVerificationDto;
import com.razorpay.RazorpayException;

import java.math.BigDecimal;

/**
 * Strategy interface for payment processing.
 * Allows different payment gateways to be plugged in without modifying existing
 * code.
 * Follows Open/Closed Principle - open for extension, closed for modification.
 */
public interface PaymentProcessor {

    /**
     * Creates an order in the payment gateway
     */
    PaymentOrderResponseDto createOrder(Long orderId, BigDecimal amount) throws RazorpayException;

    /**
     * Verifies payment signature and captures the payment
     */
    boolean verifyAndCapturePayment(PaymentVerificationDto verifyDto) throws RazorpayException;

    /**
     * Initiates a refund for a payment
     */
    String initiateRefund(PaymentRefundDto refundDto) throws RazorpayException;

    /**
     * Gets the gateway's public key for frontend integration
     */
    String getKeyId();
}
