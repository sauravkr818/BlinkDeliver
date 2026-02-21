package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.AddressDto;
import java.util.List;

public interface AddressService {

    AddressDto createAddress(Long userId, AddressDto addressDto);

    AddressDto getAddress(Long userId, Long addressId);

    List<AddressDto> getAllAddresses(Long userId);

    AddressDto updateAddress(Long userId, Long addressId, AddressDto addressDto);

    void deleteAddress(Long userId, Long addressId);
}
