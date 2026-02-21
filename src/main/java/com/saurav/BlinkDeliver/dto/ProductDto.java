package com.saurav.BlinkDeliver.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long productId;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 200, message = "Product name must be between 2 and 200 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Digits(integer = 8, fraction = 2, message = "Price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "Discount price cannot be negative")
    @Digits(integer = 8, fraction = 2, message = "Discount price must have at most 8 integer digits and 2 decimal places")
    private BigDecimal discountPrice;

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stock;

    private java.util.List<String> images;

    @NotNull(message = "Selling quantity value is required")
    @DecimalMin(value = "0.01", message = "Selling quantity value must be greater than 0")
    private BigDecimal sellingQuantityValue;

    @NotNull(message = "Selling quantity unit is required")
    private com.saurav.BlinkDeliver.enums.SellingUnit sellingQuantityUnit;

    @DecimalMin(value = "0.0", message = "Rating must be at least 0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5")
    private BigDecimal rating;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private String categoryName;

    // Brand is optional now in Product entity but keeping it here as it might be
    // useful.
    // If user wants to remove it from DTO too, I can. But for now I'll keep it as
    // optional.
    private Long brandId;

    private String brandName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}