package com.saurav.BlinkDeliver.repository;

import com.saurav.BlinkDeliver.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.saurav.BlinkDeliver.enums.CartType;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser_UserId(Long userId);

    Optional<Cart> findByUser_UserIdAndType(Long userId, CartType type);
}
