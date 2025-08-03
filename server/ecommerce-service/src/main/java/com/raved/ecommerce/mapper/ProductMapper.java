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
        response.setSellerId(product.getSellerId());
        response.setCategoryId(product.getCategoryId());
        response.setUniversityId(product.getUniversityId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setOriginalPrice(product.getOriginalPrice());
        response.setStockQuantity(product.getStockQuantity());
        response.setImageUrls(product.getImageUrls());
        response.setStatus(product.getStatus() != null ? product.getStatus().name() : null);
        response.setCondition(product.getCondition() != null ? product.getCondition().name() : null);
        response.setIsFeatured(product.getIsFeatured());
        response.setViewsCount(product.getViewsCount());
        response.setLikesCount(product.getLikesCount());
        response.setOrdersCount(product.getOrdersCount());
        response.setRating(product.getRating());
        response.setReviewsCount(product.getReviewsCount());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setFeaturedUntil(product.getFeaturedUntil());

        return response;
    }

    public Product toProduct(CreateProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        product.setSellerId(request.getSellerId());
        product.setCategoryId(request.getCategoryId());
        product.setUniversityId(request.getUniversityId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setOriginalPrice(request.getOriginalPrice());
        product.setStockQuantity(request.getStockQuantity());
        
        if (request.getCondition() != null) {
            product.setCondition(Product.ProductCondition.valueOf(request.getCondition()));
        } else {
            product.setCondition(Product.ProductCondition.NEW);
        }
        
        // Initialize counters
        product.setViewsCount(0);
        product.setLikesCount(0);
        product.setOrdersCount(0);
        product.setReviewsCount(0);
        product.setRating(0.0);
        
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
        if (request.getOriginalPrice() != null) {
            product.setOriginalPrice(request.getOriginalPrice());
        }
        if (request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if (request.getCategoryId() != null) {
            product.setCategoryId(request.getCategoryId());
        }
        if (request.getCondition() != null) {
            product.setCondition(Product.ProductCondition.valueOf(request.getCondition()));
        }
    }
}
