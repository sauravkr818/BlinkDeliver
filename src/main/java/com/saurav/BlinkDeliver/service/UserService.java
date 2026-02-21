package com.saurav.BlinkDeliver.service;

import com.netflix.discovery.converters.Auto;
import com.saurav.BlinkDeliver.dto.RegisterDto;
import com.saurav.BlinkDeliver.dto.UserDto;
import com.saurav.BlinkDeliver.entity.User;
import com.saurav.BlinkDeliver.enums.Role;
import com.saurav.BlinkDeliver.mapper.UserMapper;
import com.saurav.BlinkDeliver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for User management operations
 * Handles business logic for user registration and related operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {
    
    @Auto
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;
    
    /**
     * Register a new user
     * @param registerDto the registration data
     * @return UserDto of the created user
     * @throws RuntimeException if username or email already exists
     */
    public UserDto registerUser(RegisterDto registerDto) {
        log.info("Attempting to register user with username: {}", registerDto.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            log.warn("Registration failed: Username '{}' already exists", registerDto.getUsername());
            throw new RuntimeException("Username already exists");
        }
        
        // Check if email already exists
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            log.warn("Registration failed: Email '{}' already exists", registerDto.getEmail());
            throw new RuntimeException("Email already exists");
        }
        
        // Convert RegisterDto to User entity
        User user = userMapper.toEntity(registerDto);
        
        // Encode password using BCrypt
        String encodedPassword = passwordEncoder.encode(registerDto.getPassword());
        user.setPassword(encodedPassword);
        
        // Set default role if not provided
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
        }
        
        // Save user to database
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getUserId());
        
        // Convert back to DTO and return
        return userMapper.toDto(savedUser);
    }
    
    /**
     * Find user by username
     * @param username the username to search for
     * @return UserDto if found
     * @throws RuntimeException if user not found
     */
    @Transactional(readOnly = true)
    public UserDto findByUsername(String username) {
        log.info("Finding user by username: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("User not found with username: {}", username);
                    return new RuntimeException("User not found");
                });
        
        return userMapper.toDto(user);
    }
    
    /**
     * Find user by email
     * @param email the email to search for
     * @return UserDto if found
     * @throws RuntimeException if user not found
     */
    @Transactional(readOnly = true)
    public UserDto findByEmail(String email) {
        log.info("Finding user by email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email: {}", email);
                    return new RuntimeException("User not found");
                });
        
        return userMapper.toDto(user);
    }
    
    /**
     * Check if username is available
     * @param username the username to check
     * @return true if available, false if taken
     */
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
    
    /**
     * Check if email is available
     * @param email the email to check
     * @return true if available, false if taken
     */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }
}