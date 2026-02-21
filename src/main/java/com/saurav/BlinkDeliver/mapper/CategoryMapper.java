package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.CategoryDto;
import com.saurav.BlinkDeliver.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setCategoryId(category.getCategoryId());
        categoryDto.setName(category.getName());
        categoryDto.setDescription(category.getDescription());
        categoryDto.setImageUrl(category.getImageUrl());
        categoryDto.setCreatedAt(category.getCreatedAt());
        categoryDto.setUpdatedAt(category.getUpdatedAt());

        if (category.getParent() != null) {
            categoryDto.setParentId(category.getParent().getCategoryId());
        }

        if (category.getSubCategories() != null) {
            categoryDto.setSubCategories(category.getSubCategories().stream()
                    .map(this::toDto)
                    .collect(java.util.stream.Collectors.toList()));
        }

        return categoryDto;
    }

    public Category toEntity(CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }

        Category category = new Category();
        category.setCategoryId(categoryDto.getCategoryId());
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        category.setImageUrl(categoryDto.getImageUrl());
        category.setCreatedAt(categoryDto.getCreatedAt());
        category.setUpdatedAt(categoryDto.getUpdatedAt());

        // Parent and SubCategories are handled in Service layer usually to fetch
        // entities

        return category;
    }
}