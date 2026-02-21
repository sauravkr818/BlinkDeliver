package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.BrandDto;
import com.saurav.BlinkDeliver.entity.Brand;
import com.saurav.BlinkDeliver.mapper.BrandMapper;
import com.saurav.BlinkDeliver.repository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    @Transactional
    public BrandDto createBrand(BrandDto brandDto) {
        Brand brand = brandMapper.toEntity(brandDto);
        brand.setBrandId(null); // Ensure new creation
        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.toDto(savedBrand);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BrandDto> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(brandMapper::toDto)
                .collect(Collectors.toList());
    }
}
