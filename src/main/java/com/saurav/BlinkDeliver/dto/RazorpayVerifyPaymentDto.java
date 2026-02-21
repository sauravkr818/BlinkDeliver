package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RazorpayVerifyPaymentDto {

    @NotBlank(message = "Razorpay order ID is required")
    private String orderId;

    @NotBlank(message = "Razorpay payment ID is required")
    private String paymentId;

    @NotBlank(message = "Razorpay signature is required")
    private String signature;

    @NotNull(message = "Amount is required")
    private BigDecimal amount;
}
