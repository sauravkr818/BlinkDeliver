package com.saurav.BlinkDeliver.controller;

import com.saurav.BlinkDeliver.dto.CartDto;
import com.saurav.BlinkDeliver.dto.CartItemDto;
import com.saurav.BlinkDeliver.service.CartService;
import com.saurav.BlinkDeliver.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public CompletableFuture<ResponseEntity<CartDto>> getCart() {
        Long userId = getCurrentUserId();
        return cartService.getCartByUserId(userId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/items")
    public CompletableFuture<ResponseEntity<CartDto>> addItemToCart(@RequestBody CartItemDto cartItemDto) {
        Long userId = getCurrentUserId();
        return cartService.addItemToCart(userId, cartItemDto.getProductId(), cartItemDto.getQuantity())
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/items/{productId}")
    public CompletableFuture<ResponseEntity<CartDto>> updateItemQuantity(
            @PathVariable Long productId,
            @RequestBody CartItemDto cartItemDto) {
        Long userId = getCurrentUserId();
        return cartService.updateItemQuantity(userId, productId, cartItemDto.getQuantity())
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping("/items/{productId}")
    public CompletableFuture<ResponseEntity<CartDto>> removeItemFromCart(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        return cartService.removeItemFromCart(userId, productId)
                .thenApply(ResponseEntity::ok);
    }

    @DeleteMapping
    public CompletableFuture<ResponseEntity<Void>> clearCart() {
        Long userId = getCurrentUserId();
        return cartService.clearCart(userId)
                .thenApply(v -> ResponseEntity.noContent().build());
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).getUserId();
    }
}
