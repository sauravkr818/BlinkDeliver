package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.AddressDto;
import com.saurav.BlinkDeliver.entity.Address;
import com.saurav.BlinkDeliver.entity.User;
import com.saurav.BlinkDeliver.exception.BadRequestException;
import com.saurav.BlinkDeliver.exception.ResourceNotFoundException;
import com.saurav.BlinkDeliver.mapper.AddressMapper;
import com.saurav.BlinkDeliver.repository.AddressRepository;
import com.saurav.BlinkDeliver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional
    public AddressDto createAddress(Long userId, AddressDto addressDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // If this is the first address, make it default automatically
        List<Address> existingAddresses = addressRepository.findAllByUser_UserId(userId);
        if (existingAddresses.isEmpty()) {
            addressDto.setIsDefault(true);
        }

        // If new address is set as default, unset other default addresses
        if (Boolean.TRUE.equals(addressDto.getIsDefault())) {
            unsetOtherDefaultAddresses(userId);
        }

        Address address = addressMapper.toEntity(addressDto);
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public AddressDto getAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        return addressMapper.toDto(address);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressDto> getAllAddresses(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        return addressRepository.findAllByUser_UserId(userId).stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDto updateAddress(Long userId, Long addressId, AddressDto addressDto) {
        Address existingAddress = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!existingAddress.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        // If setting as default, unset others
        if (Boolean.TRUE.equals(addressDto.getIsDefault()) && !Boolean.TRUE.equals(existingAddress.getIsDefault())) {
            unsetOtherDefaultAddresses(userId);
        }

        // Update fields
        existingAddress.setStreet(addressDto.getStreet());
        existingAddress.setCity(addressDto.getCity());
        existingAddress.setState(addressDto.getState());
        existingAddress.setPostalCode(addressDto.getPostalCode());
        existingAddress.setCountry(addressDto.getCountry());

        // Only update isDefault if explicitly provided
        if (addressDto.getIsDefault() != null) {
            existingAddress.setIsDefault(addressDto.getIsDefault());
        }

        Address savedAddress = addressRepository.save(existingAddress);
        return addressMapper.toDto(savedAddress);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found with id: " + addressId));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        addressRepository.delete(address);
    }

    private void unsetOtherDefaultAddresses(Long userId) {
        List<Address> defaultAddresses = addressRepository.findAllByUser_UserIdAndIsDefaultTrue(userId);
        for (Address addr : defaultAddresses) {
            addr.setIsDefault(false);
            addressRepository.save(addr);
        }
    }
}
