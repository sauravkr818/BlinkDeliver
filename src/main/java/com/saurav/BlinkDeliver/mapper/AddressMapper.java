package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.AddressDto;
import com.saurav.BlinkDeliver.entity.Address;
import com.saurav.BlinkDeliver.entity.User;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {
    
    public AddressDto toDto(Address address) {
        if (address == null) {
            return null;
        }
        
        AddressDto addressDto = new AddressDto();
        addressDto.setAddressId(address.getAddressId());
        
        if (address.getUser() != null) {
            addressDto.setUserId(address.getUser().getUserId());
            addressDto.setUsername(address.getUser().getUsername());
        }
        
        addressDto.setStreet(address.getStreet());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setPostalCode(address.getPostalCode());
        addressDto.setCountry(address.getCountry());
        addressDto.setIsDefault(address.getIsDefault());
        addressDto.setCreatedAt(address.getCreatedAt());
        addressDto.setUpdatedAt(address.getUpdatedAt());
        
        return addressDto;
    }
    
    public Address toEntity(AddressDto addressDto) {
        if (addressDto == null) {
            return null;
        }
        
        Address address = new Address();
        address.setAddressId(addressDto.getAddressId());
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setPostalCode(addressDto.getPostalCode());
        address.setCountry(addressDto.getCountry());
        address.setIsDefault(addressDto.getIsDefault());
        address.setCreatedAt(addressDto.getCreatedAt());
        address.setUpdatedAt(addressDto.getUpdatedAt());
        
        if (addressDto.getUserId() != null) {
            User user = new User();
            user.setUserId(addressDto.getUserId());
            address.setUser(user);
        }
        
        return address;
    }
}