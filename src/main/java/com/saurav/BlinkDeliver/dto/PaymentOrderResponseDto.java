package com.saurav.BlinkDeliver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic DTO for payment order response.
 * Contains gateway-specific order details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderResponseDto {

    private String gatewayOrderId; // razorpay_order_id, stripe_payment_intent_id, etc.
    private String currency;
    private Integer amount;
    private String key; // Public key for frontend integration
}
