package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Generic DTO for creating a payment order.
 * Works with any payment gateway (Razorpay, Stripe, PayPal, etc.)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentOrderRequestDto {

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}
