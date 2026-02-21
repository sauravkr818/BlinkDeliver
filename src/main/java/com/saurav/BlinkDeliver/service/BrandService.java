package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.BrandDto;
import java.util.List;

public interface BrandService {
    BrandDto createBrand(BrandDto brandDto);

    List<BrandDto> getAllBrands();
}
