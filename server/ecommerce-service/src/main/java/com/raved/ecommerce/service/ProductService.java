package com.raved.ecommerce.service;

import com.raved.ecommerce.dto.request.CreateProductRequest;
import com.raved.ecommerce.dto.request.UpdateProductRequest;
import com.raved.ecommerce.dto.response.ProductResponse;
import com.raved.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * ProductService for TheRavedApp
 */
public interface ProductService {

    /**
     * Create a new product
     */
    ProductResponse createProduct(CreateProductRequest request);

    /**
     * Get product by ID
     */
    Optional<ProductResponse> getProductById(Long id);

    /**
     * Update product
     */
    ProductResponse updateProduct(Long id, UpdateProductRequest request);

    /**
     * Delete product
     */
    void deleteProduct(Long id);

    /**
     * Get products by seller
     */
    Page<ProductResponse> getProductsBySeller(Long sellerId, Pageable pageable);

    /**
     * Get products by category
     */
    Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);

    /**
     * Search products
     */
    Page<ProductResponse> searchProducts(String query, Pageable pageable);

    /**
     * Get featured products
     */
    List<ProductResponse> getFeaturedProducts(int limit);

    /**
     * Get products by price range
     */
    Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Get trending products
     */
    List<ProductResponse> getTrendingProducts(int limit);

    /**
     * Upload product images
     */
    ProductResponse uploadProductImages(Long productId, List<MultipartFile> images);

    /**
     * Update product status
     */
    ProductResponse updateProductStatus(Long id, Product.ProductStatus status);

    /**
     * Feature product
     */
    void featureProduct(Long productId, int durationHours);

    /**
     * Get products by university
     */
    Page<ProductResponse> getProductsByUniversity(Long universityId, Pageable pageable);

    /**
     * Get low stock products
     */
    List<ProductResponse> getLowStockProducts(Long sellerId, int threshold);

    /**
     * Update product inventory
     */
    void updateProductInventory(Long productId, int quantity);
}
