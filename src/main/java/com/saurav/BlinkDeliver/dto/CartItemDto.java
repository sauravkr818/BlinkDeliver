package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {

    private Long cartItemId;

    @NotNull(message = "Cart ID is required")
    private Long cartId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private String productName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private java.math.BigDecimal price;
    private java.math.BigDecimal totalPrice;

    private String imageUrl;
    private java.math.BigDecimal originalPrice;
    private java.math.BigDecimal discountPrice;
    private java.math.BigDecimal sellingQuantityValue;
    private String sellingQuantityUnit;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}