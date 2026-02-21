package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Generic DTO for payment refund.
 * Works with any payment gateway.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRefundDto {

    @NotBlank(message = "Gateway payment ID is required")
    private String gatewayPaymentId;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;

    private String reason;
}
