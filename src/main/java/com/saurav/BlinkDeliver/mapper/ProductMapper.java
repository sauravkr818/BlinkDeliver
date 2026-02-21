package com.saurav.BlinkDeliver.mapper;

import com.saurav.BlinkDeliver.dto.ProductDto;
import com.saurav.BlinkDeliver.entity.Brand;
import com.saurav.BlinkDeliver.entity.Category;
import com.saurav.BlinkDeliver.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }

        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setStock(product.getStock());

        productDto.setDiscountPrice(product.getDiscountPrice());
        productDto.setImages(product.getImages());
        productDto.setSellingQuantityValue(product.getSellingQuantityValue());
        productDto.setSellingQuantityUnit(product.getSellingQuantityUnit());
        productDto.setRating(product.getRating());

        if (product.getCategory() != null) {
            productDto.setCategoryId(product.getCategory().getCategoryId());
            productDto.setCategoryName(product.getCategory().getName());
        }

        if (product.getBrand() != null) {
            productDto.setBrandId(product.getBrand().getBrandId());
            productDto.setBrandName(product.getBrand().getName());
        }

        productDto.setCreatedAt(product.getCreatedAt());
        productDto.setUpdatedAt(product.getUpdatedAt());

        return productDto;
    }

    public Product toEntity(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setStock(productDto.getStock());
        product.setDiscountPrice(productDto.getDiscountPrice());
        if (productDto.getImages() != null) {
            product.setImages(new java.util.ArrayList<>(productDto.getImages()));
        }
        product.setSellingQuantityValue(productDto.getSellingQuantityValue());
        product.setSellingQuantityUnit(productDto.getSellingQuantityUnit());
        product.setRating(productDto.getRating());

        product.setCreatedAt(productDto.getCreatedAt());
        product.setUpdatedAt(productDto.getUpdatedAt());

        if (productDto.getCategoryId() != null) {
            Category category = new Category();
            category.setCategoryId(productDto.getCategoryId());
            product.setCategory(category);
        }

        if (productDto.getBrandId() != null) {
            Brand brand = new Brand();
            brand.setBrandId(productDto.getBrandId());
            product.setBrand(brand);
        }

        return product;
    }
}