package com.saurav.BlinkDeliver.utils;

import com.saurav.BlinkDeliver.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderCalculationUtil {

    public BigDecimal calculateTotalAmount(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return cartItems.stream()
                .map(item -> {
                    // Use discountPrice if available, otherwise use original price
                    BigDecimal effectivePrice = (item.getProduct().getDiscountPrice() != null)
                            ? item.getProduct().getDiscountPrice()
                            : item.getProduct().getPrice();
                    return effectivePrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
