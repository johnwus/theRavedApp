package com.raved.ecommerce.mapper;

import com.raved.ecommerce.dto.request.CreateProductRequest;
import com.raved.ecommerce.dto.request.UpdateProductRequest;
import com.raved.ecommerce.dto.response.ProductResponse;
import com.raved.ecommerce.model.Product;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Mapper for Product entity and DTOs
 */
@Component
public class ProductMapper {

    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setSellerId(product.getSellerUserId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setImageUrls(product.getImageUrls());
        response.setCondition(product.getCondition());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());

        return response;
    }

    public Product toProduct(CreateProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        product.setSellerUserId(request.getSellerId());
        // TODO: Load ProductCategory entity from repository using categoryId
        // product.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow());
        product.setUniversityId(request.getUniversityId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        
        if (request.getCondition() != null) {
            product.setCondition(request.getCondition());
        } else {
            product.setCondition("New");
        }
        
        // Initialize counters
        product.setViewsCount(0);
        product.setLikesCount(0);
        
        // Initialize flags
        product.setIsFeatured(false);
        product.setIsDeleted(false);
        
        return product;
    }

    public void updateProductFromRequest(Product product, UpdateProductRequest request) {
        if (product == null || request == null) {
            return;
        }

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getCategoryId() != null) {
            // TODO: Load ProductCategory entity from repository using categoryId
            // product.setCategory(categoryRepository.findById(request.getCategoryId()).orElseThrow());
        }
        if (request.getCondition() != null) {
            product.setCondition(request.getCondition());
        }
    }
}
