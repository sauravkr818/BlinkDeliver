package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.BrandDto;
import com.saurav.BlinkDeliver.entity.Brand;
import org.springframework.stereotype.Component;

@Component
public class BrandMapper {

    public BrandDto toDto(Brand brand) {
        if (brand == null) {
            return null;
        }

        BrandDto brandDto = new BrandDto();
        brandDto.setBrandId(brand.getBrandId());
        brandDto.setName(brand.getName());
        brandDto.setDescription(brand.getDescription());
        brandDto.setImageUrl(brand.getImageUrl());
        brandDto.setCreatedAt(brand.getCreatedAt());
        brandDto.setUpdatedAt(brand.getUpdatedAt());

        return brandDto;
    }

    public Brand toEntity(BrandDto brandDto) {
        if (brandDto == null) {
            return null;
        }

        Brand brand = new Brand();
        brand.setBrandId(brandDto.getBrandId());
        brand.setName(brandDto.getName());
        brand.setDescription(brandDto.getDescription());
        brand.setImageUrl(brandDto.getImageUrl());
        brand.setCreatedAt(brandDto.getCreatedAt());
        brand.setUpdatedAt(brandDto.getUpdatedAt());

        return brand;
    }
}