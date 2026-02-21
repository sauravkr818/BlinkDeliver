package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.CartDto;
import java.util.concurrent.CompletableFuture;

public interface CartService {
    CompletableFuture<CartDto> getCartByUserId(Long userId);

    CompletableFuture<CartDto> addItemToCart(Long userId, Long productId, Integer quantity);

    CompletableFuture<CartDto> removeItemFromCart(Long userId, Long productId);

    CompletableFuture<CartDto> updateItemQuantity(Long userId, Long productId, Integer quantity);

    CompletableFuture<Void> clearCart(Long userId);
}
