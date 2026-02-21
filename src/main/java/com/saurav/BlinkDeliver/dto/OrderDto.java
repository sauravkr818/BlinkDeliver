package com.saurav.BlinkDeliver.dto;

import com.saurav.BlinkDeliver.enums.OrderStatus;
import com.saurav.BlinkDeliver.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private BigDecimal itemTotal;
    private BigDecimal handlingFee;
    private BigDecimal deliveryFee;
    private PaymentMethod paymentMethod;
    private LocalDateTime orderDate;
    private List<OrderItemDto> items;
    private AddressDto deliveryAddress;
}