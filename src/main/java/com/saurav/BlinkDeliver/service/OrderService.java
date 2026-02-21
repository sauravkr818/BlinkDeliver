package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.OrderDto;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OrderService {
    CompletableFuture<OrderDto> createOrder(Long userId, Long addressId);

    CompletableFuture<OrderDto> completeOrder(Long userId, Long orderId);

    CompletableFuture<OrderDto> getOrder(Long userId, Long orderId);

    CompletableFuture<List<OrderDto>> getUserOrders(Long userId);
}
