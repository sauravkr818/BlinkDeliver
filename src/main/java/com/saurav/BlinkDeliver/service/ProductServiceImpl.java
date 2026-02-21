package com.saurav.BlinkDeliver.service;

import com.saurav.BlinkDeliver.dto.ProductDto;
import com.saurav.BlinkDeliver.entity.Brand;
import com.saurav.BlinkDeliver.entity.Category;
import com.saurav.BlinkDeliver.entity.Product;
import com.saurav.BlinkDeliver.exception.ResourceNotFoundException;
import com.saurav.BlinkDeliver.mapper.ProductMapper;
import com.saurav.BlinkDeliver.repository.BrandRepository;
import com.saurav.BlinkDeliver.repository.CategoryRepository;
import com.saurav.BlinkDeliver.repository.ProductRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductMapper productMapper;

    @Override

    @Transactional(readOnly = true)
    public List<ProductDto> getProducts(Map<String, String> filters) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filters.containsKey("category")) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("name"), filters.get("category")));
            }
            if (filters.containsKey("categoryId")) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("categoryId"),
                        Long.parseLong(filters.get("categoryId"))));
            }
            if (filters.containsKey("brand")) {
                predicates.add(criteriaBuilder.equal(root.get("brand").get("name"), filters.get("brand")));
            }
            if (filters.containsKey("minPrice")) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"),
                        new BigDecimal(filters.get("minPrice"))));
            }
            if (filters.containsKey("maxPrice")) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), new BigDecimal(filters.get("maxPrice"))));
            }
            if (filters.containsKey("name")) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + filters.get("name").toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Basic sorting
        org.springframework.data.domain.Sort sort = org.springframework.data.domain.Sort.unsorted();
        if (filters.containsKey("sort")) {
            String sortParam = filters.get("sort");
            if ("price_asc".equalsIgnoreCase(sortParam)) {
                sort = org.springframework.data.domain.Sort.by("price").ascending();
            } else if ("price_desc".equalsIgnoreCase(sortParam)) {
                sort = org.springframework.data.domain.Sort.by("price").descending();
            } else if ("newest".equalsIgnoreCase(sortParam)) {
                sort = org.springframework.data.domain.Sort.by("createdAt").descending();
            }
        }

        return productRepository.findAll(spec, sort).stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toDto(product);
    }

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto productDto) {
        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + productDto.getCategoryId()));

        Brand brand = null;
        if (productDto.getBrandId() != null) {
            brand = brandRepository.findById(productDto.getBrandId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Brand not found with id: " + productDto.getBrandId()));
        }

        Product product = productMapper.toEntity(productDto);
        product.setCategory(category);
        product.setBrand(brand);

        // Ensure ID is null for new creation
        product.setProductId(null);

        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    @Transactional
    public List<ProductDto> createProducts(List<ProductDto> productDtos) {
        return productDtos.stream()
                .map(this::createProduct)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + productDto.getCategoryId()));

        Brand brand = null;
        if (productDto.getBrandId() != null) {
            brand = brandRepository.findById(productDto.getBrandId())
                    .orElseThrow(
                            () -> new ResourceNotFoundException("Brand not found with id: " + productDto.getBrandId()));
        }

        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStock(productDto.getStock());
        existingProduct.setCategory(category);
        existingProduct.setBrand(brand);

        // Update new fields
        existingProduct.setDiscountPrice(productDto.getDiscountPrice());
        if (productDto.getImages() != null) {
            existingProduct.setImages(new java.util.ArrayList<>(productDto.getImages()));
        }
        existingProduct.setSellingQuantityValue(productDto.getSellingQuantityValue());
        existingProduct.setSellingQuantityUnit(productDto.getSellingQuantityUnit());
        existingProduct.setRating(productDto.getRating());

        Product updatedProduct = productRepository.save(existingProduct);
        return productMapper.toDto(updatedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

}
