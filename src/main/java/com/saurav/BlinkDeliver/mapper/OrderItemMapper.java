package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.OrderItemDto;
import com.saurav.BlinkDeliver.dto.PlaceOrderItemDto;
import com.saurav.BlinkDeliver.entity.Order;
import com.saurav.BlinkDeliver.entity.OrderItem;
import com.saurav.BlinkDeliver.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    
    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        
        OrderItemDto orderItemDto = new OrderItemDto();
        orderItemDto.setOrderItemId(orderItem.getOrderItemId());
        
        if (orderItem.getOrder() != null) {
            orderItemDto.setOrderId(orderItem.getOrder().getOrderId());
        }
        
        if (orderItem.getProduct() != null) {
            orderItemDto.setProductId(orderItem.getProduct().getProductId());
            orderItemDto.setProductName(orderItem.getProduct().getName());
        }
        
        orderItemDto.setQuantity(orderItem.getQuantity());
        orderItemDto.setUnitPrice(orderItem.getUnitPrice());
        orderItemDto.setTotalPrice(orderItem.getTotalPrice());
        orderItemDto.setCreatedAt(orderItem.getCreatedAt());
        orderItemDto.setUpdatedAt(orderItem.getUpdatedAt());
        
        return orderItemDto;
    }
    
    public OrderItem toEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            return null;
        }
        
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderItemId(orderItemDto.getOrderItemId());
        orderItem.setQuantity(orderItemDto.getQuantity());
        orderItem.setUnitPrice(orderItemDto.getUnitPrice());
        orderItem.setTotalPrice(orderItemDto.getTotalPrice());
        orderItem.setCreatedAt(orderItemDto.getCreatedAt());
        orderItem.setUpdatedAt(orderItemDto.getUpdatedAt());
        
        if (orderItemDto.getOrderId() != null) {
            Order order = new Order();
            order.setOrderId(orderItemDto.getOrderId());
            orderItem.setOrder(order);
        }
        
        if (orderItemDto.getProductId() != null) {
            Product product = new Product();
            product.setProductId(orderItemDto.getProductId());
            orderItem.setProduct(product);
        }
        
        return orderItem;
    }
    
    public OrderItem toEntity(PlaceOrderItemDto placeOrderItemDto) {
        if (placeOrderItemDto == null) {
            return null;
        }
        
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(placeOrderItemDto.getQuantity());
        
        if (placeOrderItemDto.getProductId() != null) {
            Product product = new Product();
            product.setProductId(placeOrderItemDto.getProductId());
            orderItem.setProduct(product);
        }
        
        return orderItem;
    }
}