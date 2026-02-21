package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.CartItemDto;
import com.saurav.BlinkDeliver.entity.Cart;
import com.saurav.BlinkDeliver.entity.CartItem;
import com.saurav.BlinkDeliver.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    
    public CartItemDto toDto(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        
        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setCartItemId(cartItem.getCartItemId());
        
        if (cartItem.getCart() != null) {
            cartItemDto.setCartId(cartItem.getCart().getCartId());
        }
        
        if (cartItem.getProduct() != null) {
            cartItemDto.setProductId(cartItem.getProduct().getProductId());
            cartItemDto.setProductName(cartItem.getProduct().getName());
        }
        
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setCreatedAt(cartItem.getCreatedAt());
        cartItemDto.setUpdatedAt(cartItem.getUpdatedAt());
        
        return cartItemDto;
    }
    
    public CartItem toEntity(CartItemDto cartItemDto) {
        if (cartItemDto == null) {
            return null;
        }
        
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(cartItemDto.getCartItemId());
        cartItem.setQuantity(cartItemDto.getQuantity());
        cartItem.setCreatedAt(cartItemDto.getCreatedAt());
        cartItem.setUpdatedAt(cartItemDto.getUpdatedAt());
        
        if (cartItemDto.getCartId() != null) {
            Cart cart = new Cart();
            cart.setCartId(cartItemDto.getCartId());
            cartItem.setCart(cart);
        }
        
        if (cartItemDto.getProductId() != null) {
            Product product = new Product();
            product.setProductId(cartItemDto.getProductId());
            cartItem.setProduct(product);
        }
        
        return cartItem;
    }
}