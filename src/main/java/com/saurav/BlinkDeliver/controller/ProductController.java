package com.saurav.BlinkDeliver.controller;

import com.saurav.BlinkDeliver.dto.ProductDto;
import com.saurav.BlinkDeliver.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @GetMapping("/products")
    public ResponseEntity<List<ProductDto>> getProducts(@RequestParam Map<String, String> filters) {
        List<ProductDto> products = productService.getProducts(filters);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        ProductDto product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody String payload) {
        try {
            com.fasterxml.jackson.databind.JsonNode jsonNode = objectMapper.readTree(payload);
            if (jsonNode.isArray()) {
                List<ProductDto> productDtos = java.util.Arrays
                        .asList(objectMapper.treeToValue(jsonNode, ProductDto[].class));
                List<ProductDto> createdProducts = productService.createProducts(productDtos);
                return new ResponseEntity<>(createdProducts, HttpStatus.CREATED);
            } else {
                ProductDto productDto = objectMapper.treeToValue(jsonNode, ProductDto.class);
                ProductDto createdProduct = productService.createProduct(productDto);
                return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
            }
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new com.saurav.BlinkDeliver.exception.BadRequestException("Invalid request format");
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        ProductDto updatedProduct = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
