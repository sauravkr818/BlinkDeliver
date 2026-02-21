package com.saurav.BlinkDeliver.controller;

import com.saurav.BlinkDeliver.dto.AddressDto;
import com.saurav.BlinkDeliver.service.AddressService;
import com.saurav.BlinkDeliver.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AddressDto> createAddress(@Valid @RequestBody AddressDto addressDto) {
        Long userId = getCurrentUserId();
        AddressDto createdAddress = addressService.createAddress(userId, addressDto);
        return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getAddress(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        AddressDto address = addressService.getAddress(userId, id);
        return ResponseEntity.ok(address);
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAllAddresses() {
        Long userId = getCurrentUserId();
        List<AddressDto> addresses = addressService.getAllAddresses(userId);
        return ResponseEntity.ok(addresses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDto> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressDto addressDto) {
        Long userId = getCurrentUserId();
        AddressDto updatedAddress = addressService.updateAddress(userId, id, addressDto);
        return ResponseEntity.ok(updatedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        Long userId = getCurrentUserId();
        addressService.deleteAddress(userId, id);
        return ResponseEntity.noContent().build();
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userService.findByUsername(username).getUserId();
    }
}
