package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.ProductDto;
import java.util.List;
import java.util.Map;

public interface ProductService {
    List<ProductDto> getProducts(Map<String, String> filters);

    ProductDto getProduct(Long id);

    ProductDto createProduct(ProductDto productDto);

    List<ProductDto> createProducts(List<ProductDto> productDtos);

    ProductDto updateProduct(Long id, ProductDto productDto);

    void deleteProduct(Long id);

}
