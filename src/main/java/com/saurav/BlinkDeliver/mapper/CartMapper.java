package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.CartDto;
import com.saurav.BlinkDeliver.entity.Cart;
import com.saurav.BlinkDeliver.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {
    
    @Autowired
    private CartItemMapper cartItemMapper;
    
    public CartDto toDto(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        CartDto cartDto = new CartDto();
        cartDto.setCartId(cart.getCartId());
        
        if (cart.getUser() != null) {
            cartDto.setUserId(cart.getUser().getUserId());
            cartDto.setUsername(cart.getUser().getUsername());
        }
        
        if (cart.getCartItems() != null) {
            cartDto.setCartItems(cart.getCartItems().stream()
                    .map(cartItemMapper::toDto)
                    .collect(Collectors.toList()));
        }
        
        cartDto.setCreatedAt(cart.getCreatedAt());
        cartDto.setUpdatedAt(cart.getUpdatedAt());
        
        return cartDto;
    }
    
    public Cart toEntity(CartDto cartDto) {
        if (cartDto == null) {
            return null;
        }
        
        Cart cart = new Cart();
        cart.setCartId(cartDto.getCartId());
        cart.setCreatedAt(cartDto.getCreatedAt());
        cart.setUpdatedAt(cartDto.getUpdatedAt());
        
        if (cartDto.getUserId() != null) {
            User user = new User();
            user.setUserId(cartDto.getUserId());
            cart.setUser(user);
        }
        
        return cart;
    }
}