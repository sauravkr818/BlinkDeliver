package com.saurav.BlinkDeliver.controller;

import com.saurav.BlinkDeliver.dto.OrderDto;
import com.saurav.BlinkDeliver.dto.OrderRequest;
import com.saurav.BlinkDeliver.service.OrderService;
import com.saurav.BlinkDeliver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    public CompletableFuture<ResponseEntity<OrderDto>> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        Long userId = getCurrentUserId();
        return orderService.createOrder(userId, orderRequest.getAddressId())
                .thenApply(orderDto -> new ResponseEntity<>(orderDto, HttpStatus.CREATED));
    }

    @PostMapping("/{orderId}/pay")
    public CompletableFuture<ResponseEntity<OrderDto>> completeOrder(@PathVariable Long orderId) {
        Long userId = getCurrentUserId();
        return orderService.completeOrder(userId, orderId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("/{orderId}")
    public CompletableFuture<ResponseEntity<OrderDto>> getOrder(@PathVariable Long orderId) {
        Long userId = getCurrentUserId();
        return orderService.getOrder(userId, orderId)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping
    public CompletableFuture<ResponseEntity<List<OrderDto>>> getUserOrders() {
        Long userId = getCurrentUserId();
        return orderService.getUserOrders(userId)
                .thenApply(ResponseEntity::ok);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).getUserId();
    }
}
