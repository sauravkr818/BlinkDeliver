package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.PaymentOrderRequestDto;
import com.saurav.BlinkDeliver.dto.PaymentOrderResponseDto;
import com.saurav.BlinkDeliver.dto.PaymentRefundDto;
import com.saurav.BlinkDeliver.dto.PaymentVerificationDto;

import java.util.concurrent.CompletableFuture;

public interface PaymentService {

    CompletableFuture<PaymentOrderResponseDto> createPayment(PaymentOrderRequestDto requestDto, Long userId);

    CompletableFuture<Boolean> verifyPayment(PaymentVerificationDto verifyDto, Long userId);

    CompletableFuture<String> initiateRefund(PaymentRefundDto refundDto, Long userId);

    CompletableFuture<String> getPaymentGatewayKey();
}
