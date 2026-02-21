package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Generic DTO for payment verification.
 * Works with different gateway verification mechanisms:
 * - Razorpay: signature verification
 * - Stripe: webhook verification
 * - PayPal: IPN verification
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentVerificationDto {

    @NotBlank(message = "Gateway order ID is required")
    private String gatewayOrderId; // razorpay_order_id, stripe_payment_intent_id, etc.

    @NotBlank(message = "Gateway payment ID is required")
    private String gatewayPaymentId; // razorpay_payment_id, stripe_charge_id, etc.

    @NotBlank(message = "Verification token is required")
    private String verificationToken; // razorpay_signature, stripe_signature, etc.

    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}
