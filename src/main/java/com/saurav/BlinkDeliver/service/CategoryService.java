package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.CategoryDto;
import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategories(String view);

    CategoryDto getCategory(Long id);

    List<CategoryDto> getCategoriesByIds(List<Long> ids);

    CategoryDto updateCategory(Long id, CategoryDto categoryDto);

    void deleteCategory(Long id);

    List<com.saurav.BlinkDeliver.dto.ProductDto> getCategoryProducts(Long id);
}
