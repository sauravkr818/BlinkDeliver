package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private Long orderItemId;

    @NotNull(message = "Order ID is required")
    private Long orderId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    private String productName;

    // Product details for order page display
    private String productDescription;
    private List<String> productImages;
    private BigDecimal productOriginalPrice;
    private BigDecimal productDiscountPrice;
    private BigDecimal sellingQuantityValue;
    private String sellingQuantityUnit;
    private BigDecimal productRating;
    private String brandName;
    private String categoryName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.01", message = "Unit price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Unit price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal unitPrice;

    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.01", message = "Total price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Total price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal totalPrice;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}