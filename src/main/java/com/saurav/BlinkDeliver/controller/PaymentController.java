package com.saurav.BlinkDeliver.controller;

import com.saurav.BlinkDeliver.dto.PaymentOrderRequestDto;
import com.saurav.BlinkDeliver.dto.PaymentOrderResponseDto;
import com.saurav.BlinkDeliver.dto.PaymentRefundDto;
import com.saurav.BlinkDeliver.dto.PaymentVerificationDto;
import com.saurav.BlinkDeliver.service.PaymentService;
import com.saurav.BlinkDeliver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    @PostMapping("/create")
    public CompletableFuture<ResponseEntity<PaymentOrderResponseDto>> createPayment(
            @Valid @RequestBody PaymentOrderRequestDto requestDto) {
        Long userId = getCurrentUserId();
        return paymentService.createPayment(requestDto, userId)
                .thenApply(orderDto -> new ResponseEntity<>(orderDto, HttpStatus.CREATED));
    }

    @PostMapping("/verify")
    public CompletableFuture<ResponseEntity<Boolean>> verifyPayment(
            @Valid @RequestBody PaymentVerificationDto verifyDto) {
        Long userId = getCurrentUserId();
        return paymentService.verifyPayment(verifyDto, userId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/refund")
    public CompletableFuture<ResponseEntity<String>> initiateRefund(
            @Valid @RequestBody PaymentRefundDto refundDto) {
        Long userId = getCurrentUserId();
        return paymentService.initiateRefund(refundDto, userId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/key")
    public CompletableFuture<ResponseEntity<String>> getPaymentGatewayKey() {
        return paymentService.getPaymentGatewayKey()
                .thenApply(ResponseEntity::ok);
    }

    @org.springframework.beans.factory.annotation.Value("${razorpay.key.secret}")
    private String secret;

    @PostMapping("/test/signature")
    public ResponseEntity<String> generateSignature(@RequestParam String orderId, @RequestParam String paymentId)
            throws Exception {
        String data = orderId + "|" + paymentId;
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(secret.getBytes(),
                "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return ResponseEntity.ok(hexString.toString());
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).getUserId();
    }
}
