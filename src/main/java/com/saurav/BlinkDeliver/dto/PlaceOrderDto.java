package com.saurav.BlinkDeliver.dto;

import com.saurav.BlinkDeliver.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceOrderDto {
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;
    
    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;
    
    @NotEmpty(message = "Order items cannot be empty")
    @Valid
    private List<PlaceOrderItemDto> orderItems;
}