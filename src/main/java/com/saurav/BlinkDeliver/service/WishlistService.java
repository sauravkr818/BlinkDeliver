package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.CartDto;
import java.util.concurrent.CompletableFuture;

public interface WishlistService {
    CompletableFuture<CartDto> getWishlist(Long userId);

    CompletableFuture<CartDto> addItemToWishlist(Long userId, Long productId);

    CompletableFuture<CartDto> removeItemFromWishlist(Long userId, Long productId);
}
