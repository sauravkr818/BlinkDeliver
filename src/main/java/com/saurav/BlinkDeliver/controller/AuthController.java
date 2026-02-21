package com.saurav.BlinkDeliver.controller;

import com.saurav.BlinkDeliver.dto.RegisterDto;
import com.saurav.BlinkDeliver.dto.UserDto;
import com.saurav.BlinkDeliver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST Controller for authentication operations
 * Handles user registration and authentication endpoints
 */
@RestController

@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j

@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private final UserService userService;

    /**
     * Register a new user
     * 
     * @param registerDto the registration data
     * @return ResponseEntity with user data or error message
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        log.info("Registration request received for username: {}", registerDto.getUsername());

        Map<String, Object> response = new HashMap<>();

        try {
            // Register the user

            UserDto userDto = userService.registerUser(registerDto);

            response.put("success", true);
            response.put("message", "User registered successfully");

            response.put("user", userDto);

            log.info("User registration successful for username: {}", registerDto.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {

            log.error("Registration failed for username: {}, Error: {}", registerDto.getUsername(), e.getMessage());

            response.put("success", false);
            response.put("message", e.getMessage());

            // Return appropriate HTTP status based on error type
            if (e.getMessage().contains("already exists")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (Exception e) {
            log.error("Unexpected error during registration for username: {}", registerDto.getUsername(), e);

            response.put("success", false);
            response.put("message", "Internal server error occurred");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/users")
    public String getUsersDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "fetched user details successfully";
    }

    /**
     * Check if username is available
     * 
     * @param username the username to check
     * @return ResponseEntity with availability status
     */
    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsernameAvailability(@PathVariable String username) {
        log.info("Checking username availability for: {}", username);

        Map<String, Object> response = new HashMap<>();
        boolean isAvailable = userService.isUsernameAvailable(username);

        response.put("available", isAvailable);
        response.put("username", username);

        return ResponseEntity.ok(response);
    }

    /**
     * 
     * 
     * 
     * Check if email is available
     * 
     * @param email the email to check
     * @return ResponseEntity with availability status
     */
    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Object>> checkEmailAvailability(@PathVariable String email) {
        log.info("Checking email availability for: {}", email);

        Map<String, Object> response = new HashMap<>();
        boolean isAvailable = userService.isEmailAvailable(email);

        response.put("available", isAvailable);
        response.put("email", email);

        return ResponseEntity.ok(response);
    }
}