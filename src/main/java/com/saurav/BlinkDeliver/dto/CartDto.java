package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {

    private Long cartId;

    @NotNull(message = "User ID is required")
    private Long userId;

    private String username;

    private List<CartItemDto> cartItems;

    private java.math.BigDecimal totalAmount;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}