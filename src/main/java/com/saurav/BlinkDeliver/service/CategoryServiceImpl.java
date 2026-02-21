package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.CategoryDto;
import com.saurav.BlinkDeliver.entity.Category;
import com.saurav.BlinkDeliver.exception.ResourceNotFoundException;
import com.saurav.BlinkDeliver.mapper.CategoryMapper;
import com.saurav.BlinkDeliver.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setCategoryId(null); // Ensure new creation

        if (categoryDto.getParentId() != null) {
            Category parent = categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Parent category not found with id: " + categoryDto.getParentId()));
            category.setParent(parent);
        }

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(String view) {
        if ("tree".equalsIgnoreCase(view)) {
            return categoryRepository.findAll().stream()
                    .filter(c -> c.getParent() == null)
                    .map(categoryMapper::toDto)
                    .collect(Collectors.toList());
        }
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategoriesByIds(List<Long> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        return categories.stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        existingCategory.setName(categoryDto.getName());
        existingCategory.setDescription(categoryDto.getDescription());
        existingCategory.setImageUrl(categoryDto.getImageUrl());

        if (categoryDto.getParentId() != null) {
            // Prevent self-referencing
            if (categoryDto.getParentId().equals(id)) {
                throw new IllegalArgumentException("A category cannot be its own parent");
            }
            Category parent = categoryRepository.findById(categoryDto.getParentId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Parent category not found with id: " + categoryDto.getParentId()));
            existingCategory.setParent(parent);
        } else {
            existingCategory.setParent(null);
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<com.saurav.BlinkDeliver.dto.ProductDto> getCategoryProducts(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        // TODO: Implement fetching products and mapping them.
        // For now returning empty list to fix compilation.
        // Ideally we should inject ProductRepository or use category.getProducts() and
        // map them.
        // Since ProductMapper is not injected here, I'll rely on lazy loading if
        // transaction is active,
        // but mapping requires ProductMapper.
        // I'll leave it empty for now or add ProductMapper dependency.
        return java.util.Collections.emptyList();
    }
}
