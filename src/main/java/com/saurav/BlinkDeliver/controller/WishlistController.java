package com.saurav.BlinkDeliver.controller;

import com.saurav.BlinkDeliver.dto.CartDto;
import com.saurav.BlinkDeliver.dto.CartItemDto;
import com.saurav.BlinkDeliver.service.UserService;
import com.saurav.BlinkDeliver.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;
    private final UserService userService;

    @GetMapping
    public CompletableFuture<ResponseEntity<CartDto>> getWishlist() {
        Long userId = getCurrentUserId();
        return wishlistService.getWishlist(userId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/items")
    public CompletableFuture<ResponseEntity<CartDto>> addItemToWishlist(@RequestBody CartItemDto cartItemDto) {
        Long userId = getCurrentUserId();
        return wishlistService.addItemToWishlist(userId, cartItemDto.getProductId())
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/items/{productId}")
    public CompletableFuture<ResponseEntity<CartDto>> removeItemFromWishlist(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        return wishlistService.removeItemFromWishlist(userId, productId)
                .thenApply(ResponseEntity::ok);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).getUserId();
    }
}
