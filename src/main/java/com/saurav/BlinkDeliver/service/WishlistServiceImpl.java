package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.CartDto;
import com.saurav.BlinkDeliver.dto.CartItemDto;
import com.saurav.BlinkDeliver.entity.Cart;
import com.saurav.BlinkDeliver.entity.CartItem;
import com.saurav.BlinkDeliver.entity.Product;
import com.saurav.BlinkDeliver.entity.User;
import com.saurav.BlinkDeliver.enums.CartType;
import com.saurav.BlinkDeliver.exception.ResourceNotFoundException;
import com.saurav.BlinkDeliver.repository.CartRepository;
import com.saurav.BlinkDeliver.repository.ProductRepository;
import com.saurav.BlinkDeliver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<CartDto> getWishlist(Long userId) {
        Cart wishlist = cartRepository.findByUser_UserIdAndType(userId, CartType.WISHLIST)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

                    Cart newWishlist = new Cart();
                    newWishlist.setUser(user);
                    newWishlist.setType(CartType.WISHLIST);
                    newWishlist.setCartItems(new ArrayList<>());
                    return cartRepository.save(newWishlist);
                });
        return CompletableFuture.completedFuture(mapToDto(wishlist));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<CartDto> addItemToWishlist(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        Cart wishlist = cartRepository.findByUser_UserIdAndType(userId, CartType.WISHLIST).orElseGet(() -> {
            Cart newWishlist = new Cart();
            newWishlist.setUser(user);
            newWishlist.setType(CartType.WISHLIST);
            newWishlist.setCartItems(new ArrayList<>());
            return cartRepository.save(newWishlist);
        });

        // Check if item already exists in wishlist
        boolean exists = wishlist.getCartItems().stream()
                .anyMatch(item -> item.getProduct().getProductId().equals(productId));

        if (!exists) {
            CartItem newItem = new CartItem();
            newItem.setCart(wishlist);
            newItem.setProduct(product);
            newItem.setQuantity(1); // Default quantity for wishlist
            wishlist.getCartItems().add(newItem);
            cartRepository.save(wishlist);
        }

        return CompletableFuture.completedFuture(mapToDto(wishlist));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<CartDto> removeItemFromWishlist(Long userId, Long productId) {
        Cart wishlist = cartRepository.findByUser_UserIdAndType(userId, CartType.WISHLIST)
                .orElseThrow(() -> new ResourceNotFoundException("Wishlist not found for user id: " + userId));

        boolean removed = wishlist.getCartItems().removeIf(item -> item.getProduct().getProductId().equals(productId));

        if (!removed) {
            throw new ResourceNotFoundException("Product not found in wishlist: " + productId);
        }

        Cart savedWishlist = cartRepository.save(wishlist);
        return CompletableFuture.completedFuture(mapToDto(savedWishlist));
    }

    private CartDto mapToDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setCartId(cart.getCartId());
        dto.setUserId(cart.getUser().getUserId());
        dto.setUsername(cart.getUser().getUsername());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (cart.getCartItems() != null) {
            dto.setCartItems(cart.getCartItems().stream().map(item -> {
                CartItemDto itemDto = new CartItemDto();
                itemDto.setCartItemId(item.getCartItemId());
                itemDto.setCartId(cart.getCartId());
                itemDto.setProductId(item.getProduct().getProductId());
                itemDto.setProductName(item.getProduct().getName());
                itemDto.setQuantity(item.getQuantity());
                itemDto.setCreatedAt(item.getCreatedAt());
                itemDto.setUpdatedAt(item.getUpdatedAt());

                // Use discountPrice if available, otherwise use original price
                Product product = item.getProduct();

                if (product.getImages() != null && !product.getImages().isEmpty()) {
                    itemDto.setImageUrl(product.getImages().get(0));
                }

                itemDto.setOriginalPrice(product.getPrice());
                itemDto.setDiscountPrice(product.getDiscountPrice());
                if (product.getSellingQuantityValue() != null) {
                    itemDto.setSellingQuantityValue(product.getSellingQuantityValue());
                }
                if (product.getSellingQuantityUnit() != null) {
                    itemDto.setSellingQuantityUnit(product.getSellingQuantityUnit().toString());
                }

                BigDecimal price = (product.getDiscountPrice() != null)
                        ? product.getDiscountPrice()
                        : product.getPrice();
                itemDto.setPrice(price);
                itemDto.setTotalPrice(price.multiply(BigDecimal.valueOf(item.getQuantity())));

                return itemDto;
            }).collect(Collectors.toList()));

            totalAmount = dto.getCartItems().stream()
                    .map(CartItemDto::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        dto.setTotalAmount(totalAmount);
        return dto;
    }
}
