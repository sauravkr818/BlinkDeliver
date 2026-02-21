package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.OrderDto;
import com.saurav.BlinkDeliver.entity.*;
import com.saurav.BlinkDeliver.enums.CartType;
import com.saurav.BlinkDeliver.enums.OrderStatus;
import com.saurav.BlinkDeliver.enums.PaymentMethod;
import com.saurav.BlinkDeliver.exception.BadRequestException;
import com.saurav.BlinkDeliver.exception.ResourceNotFoundException;
import com.saurav.BlinkDeliver.mapper.OrderMapper;
import com.saurav.BlinkDeliver.repository.*;
import com.saurav.BlinkDeliver.utils.OrderCalculationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final OrderCalculationUtil orderCalculationUtil;

    @Override
    @Async
    @Transactional
    public CompletableFuture<OrderDto> createOrder(Long userId, Long addressId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        Cart cart = cartRepository.findByUser_UserIdAndType(userId, CartType.SHOPPING_CART)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        validateStock(cart.getCartItems());

        Order order = new Order();
        order.setUser(user);
        order.setDeliveryAddress(address);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(PaymentMethod.UPI);
        order.setOrderNumber(UUID.randomUUID().toString());

        // Calculate item total (without fees)
        BigDecimal itemTotal = orderCalculationUtil.calculateTotalAmount(cart.getCartItems());
        order.setItemTotal(itemTotal);

        // Calculate handling and delivery fees
        // Free if item total >= 99, otherwise handling = 10, delivery = 30
        BigDecimal freeDeliveryThreshold = new BigDecimal("99");
        boolean isFreeDelivery = itemTotal.compareTo(freeDeliveryThreshold) >= 0;

        BigDecimal handlingFee = isFreeDelivery ? BigDecimal.ZERO : new BigDecimal("10");
        BigDecimal deliveryFee = isFreeDelivery ? BigDecimal.ZERO : new BigDecimal("30");

        order.setHandlingFee(handlingFee);
        order.setDeliveryFee(deliveryFee);

        // Total = items + handling + delivery
        BigDecimal totalAmount = itemTotal.add(handlingFee).add(deliveryFee);
        order.setTotalAmount(totalAmount);

        List<OrderItem> orderItems = createOrderItems(cart.getCartItems(), order);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        return CompletableFuture.completedFuture(orderMapper.toDto(savedOrder));
    }

    @Override
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public CompletableFuture<OrderDto> completeOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to user");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Order is not in PENDING state");
        }

        // Update order status to confirmed
        order.setStatus(OrderStatus.CONFIRMED);

        deductStock(order.getOrderItems());

        orderRepository.save(order);

        clearCart(userId);

        return CompletableFuture.completedFuture(orderMapper.toDto(order));
    }

    @Override
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<OrderDto> getOrder(Long userId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Order does not belong to user");
        }

        return CompletableFuture.completedFuture(orderMapper.toDto(order));
    }

    @Override
    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<OrderDto>> getUserOrders(Long userId) {
        // Verify user exists
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<Order> orders = orderRepository.findByUser_UserId(userId);
        List<OrderDto> orderDtos = orders.stream()
                .map(orderMapper::toDto)
                .collect(java.util.stream.Collectors.toList());

        return CompletableFuture.completedFuture(orderDtos);
    }

    private void validateStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getStock() < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + item.getProduct().getName());
            }
        }
    }

    private List<OrderItem> createOrderItems(List<CartItem> cartItems, Order order) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            // Use discounted price if available, otherwise use original price
            BigDecimal effectivePrice = (product.getDiscountPrice() != null)
                    ? product.getDiscountPrice()
                    : product.getPrice();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setUnitPrice(effectivePrice);
            orderItem.setTotalPrice(effectivePrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private void deductStock(List<OrderItem> orderItems) {
        for (OrderItem item : orderItems) {
            int updatedRows = productRepository.decreaseStock(item.getProduct().getProductId(), item.getQuantity());
            if (updatedRows == 0) {
                throw new BadRequestException(
                        "Insufficient stock for product: " + item.getProduct().getName()
                                + " during payment processing");
            }
        }
    }

    private void clearCart(Long userId) {
        Cart cart = cartRepository.findByUser_UserIdAndType(userId, CartType.SHOPPING_CART)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found for user id: " + userId));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
