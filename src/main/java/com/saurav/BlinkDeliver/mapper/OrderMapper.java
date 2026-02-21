package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.AddressDto;
import com.saurav.BlinkDeliver.dto.OrderDto;
import com.saurav.BlinkDeliver.dto.OrderItemDto;
import com.saurav.BlinkDeliver.entity.Address;
import com.saurav.BlinkDeliver.entity.Order;
import com.saurav.BlinkDeliver.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }

        OrderDto dto = new OrderDto();
        dto.setOrderId(order.getOrderId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setStatus(order.getStatus());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setItemTotal(order.getItemTotal());
        dto.setHandlingFee(order.getHandlingFee());
        dto.setDeliveryFee(order.getDeliveryFee());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setOrderDate(order.getOrderDate());

        if (order.getOrderItems() != null) {
            dto.setItems(order.getOrderItems().stream()
                    .map(this::toOrderItemDto)
                    .collect(Collectors.toList()));
        }

        if (order.getDeliveryAddress() != null) {
            dto.setDeliveryAddress(toAddressDto(order.getDeliveryAddress()));
        }

        return dto;
    }

    public OrderItemDto toOrderItemDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        OrderItemDto dto = new OrderItemDto();
        dto.setOrderItemId(orderItem.getOrderItemId());
        dto.setOrderId(orderItem.getOrder().getOrderId());
        dto.setProductId(orderItem.getProduct().getProductId());
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setUnitPrice(orderItem.getUnitPrice());
        dto.setTotalPrice(orderItem.getTotalPrice());
        dto.setCreatedAt(orderItem.getCreatedAt());
        dto.setUpdatedAt(orderItem.getUpdatedAt());

        // Populate product details for order page display
        com.saurav.BlinkDeliver.entity.Product product = orderItem.getProduct();
        dto.setProductDescription(product.getDescription());
        dto.setProductImages(product.getImages() != null ? new java.util.ArrayList<>(product.getImages()) : null);
        dto.setProductOriginalPrice(product.getPrice());
        dto.setProductDiscountPrice(product.getDiscountPrice());
        dto.setSellingQuantityValue(product.getSellingQuantityValue());
        dto.setSellingQuantityUnit(
                product.getSellingQuantityUnit() != null ? product.getSellingQuantityUnit().name() : null);
        dto.setProductRating(product.getRating());

        // Brand and category names
        if (product.getBrand() != null) {
            dto.setBrandName(product.getBrand().getName());
        }
        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }

        return dto;
    }

    public AddressDto toAddressDto(Address address) {
        if (address == null) {
            return null;
        }

        AddressDto dto = new AddressDto();
        dto.setAddressId(address.getAddressId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setPostalCode(address.getPostalCode());
        dto.setCountry(address.getCountry());
        dto.setIsDefault(address.getIsDefault());
        return dto;
    }
}