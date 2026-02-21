package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.CartDto;
import com.saurav.BlinkDeliver.dto.CartItemDto;
import com.saurav.BlinkDeliver.entity.Cart;
import com.saurav.BlinkDeliver.entity.CartItem;
import com.saurav.BlinkDeliver.entity.Product;
import com.saurav.BlinkDeliver.entity.User;
import com.saurav.BlinkDeliver.enums.CartType;
import com.saurav.BlinkDeliver.exception.BadRequestException;
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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<CartDto> getCartByUserId(Long userId) {
        Cart cart = cartRepository.findByUser_UserIdAndType(userId, CartType.SHOPPING_CART)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        return CompletableFuture.completedFuture(mapToDto(cart));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<CartDto> addItemToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (product.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName());
        }

        Cart cart = cartRepository.findByUser_UserIdAndType(userId, CartType.SHOPPING_CART).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setType(CartType.SHOPPING_CART);
            newCart.setCartItems(new ArrayList<>());
            return cartRepository.save(newCart);
        });

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProduct().getProductId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            if (product.getStock() < newQuantity) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cart.getCartItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return CompletableFuture.completedFuture(mapToDto(savedCart));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<CartDto> removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUser_UserIdAndType(userId, CartType.SHOPPING_CART)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        boolean removed = cart.getCartItems().removeIf(item -> item.getProduct().getProductId().equals(productId));

        if (!removed) {
            throw new ResourceNotFoundException("Product not found in cart: " + productId);
        }

        Cart savedCart = cartRepository.save(cart);
        return CompletableFuture.completedFuture(mapToDto(savedCart));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<CartDto> updateItemQuantity(Long userId, Long productId, Integer quantity) {
        Cart cart = cartRepository.findByUser_UserIdAndType(userId, CartType.SHOPPING_CART)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        CartItem item = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart: " + productId));

        Product product = item.getProduct();
        if (product.getStock() < quantity) {
            throw new BadRequestException("Insufficient stock for product: " + product.getName());
        }

        item.setQuantity(quantity);
        Cart savedCart = cartRepository.save(cart);
        return CompletableFuture.completedFuture(mapToDto(savedCart));
    }

    @Override
    @Async
    @Transactional
    public CompletableFuture<Void> clearCart(Long userId) {
        Cart cart = cartRepository.findByUser_UserIdAndType(userId, CartType.SHOPPING_CART)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        cart.getCartItems().clear();
        cartRepository.save(cart);
        return CompletableFuture.completedFuture(null);
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

                // Populate details for UI
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
