package com.raved.ecommerce.controller;

import com.raved.ecommerce.dto.request.CreateProductRequest;
import com.raved.ecommerce.dto.request.UpdateProductRequest;
import com.raved.ecommerce.dto.response.ProductResponse;
import com.raved.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST Controller for Product Management
 * Handles product catalog operations including CRUD, search, and filtering
 */
@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Product Management", description = "APIs for managing product catalog")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get product catalog with filtering and pagination")
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(
            @Parameter(description = "Product category filter") @RequestParam(required = false) String category,
            @Parameter(description = "Minimum price filter") @RequestParam(required = false) BigDecimal minPrice,
            @Parameter(description = "Maximum price filter") @RequestParam(required = false) BigDecimal maxPrice,
            @Parameter(description = "Size filter") @RequestParam(required = false) String size,
            @Parameter(description = "Condition filter") @RequestParam(required = false) String condition,
            @Parameter(description = "Seller faculty filter") @RequestParam(required = false) String faculty,
            @Parameter(description = "Sort by (price, date, popularity)") @RequestParam(defaultValue = "date") String sort,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int pageSize) {

        logger.debug("Getting products with filters - category: {}, minPrice: {}, maxPrice: {}, sort: {}",
                    category, minPrice, maxPrice, sort);

        // Create sort criteria
        Sort sortCriteria = switch (sort.toLowerCase()) {
            case "price" -> Sort.by("price").ascending();
            case "popularity" -> Sort.by("viewCount").descending();
            default -> Sort.by("createdAt").descending();
        };

        Pageable pageable = PageRequest.of(page, pageSize, sortCriteria);

        // For now, implement basic filtering - can be enhanced with specifications
        Page<ProductResponse> products;
        if (minPrice != null && maxPrice != null) {
            products = productService.getProductsByPriceRange(minPrice, maxPrice, pageable);
        } else {
            // TODO: Implement comprehensive filtering with category, condition, etc.
            // For now, return all products with pagination
            products = productService.getProductsBySeller(null, pageable); // This needs to be updated
        }

        Map<String, Object> response = Map.of(
            "success", true,
            "products", products.getContent(),
            "pagination", Map.of(
                "page", products.getNumber(),
                "size", products.getSize(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages(),
                "hasNext", products.hasNext()
            )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create new product listing")
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        logger.info("Creating new product: {}", request.getName());

        try {
            ProductResponse product = productService.createProduct(request);

            Map<String, Object> response = Map.of(
                "success", true,
                "product", product,
                "message", "Product created successfully"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating product: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to create product: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Get detailed product information")
    @GetMapping("/{productId}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable Long productId) {
        logger.debug("Getting product by ID: {}", productId);

        Optional<ProductResponse> productOpt = productService.getProductById(productId);

        if (productOpt.isPresent()) {
            Map<String, Object> response = Map.of(
                "success", true,
                "product", productOpt.get()
            );
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = Map.of(
                "success", false,
                "message", "Product not found"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(summary = "Update product")
    @PutMapping("/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductRequest request) {

        logger.info("Updating product with ID: {}", productId);

        try {
            ProductResponse product = productService.updateProduct(productId, request);

            Map<String, Object> response = Map.of(
                "success", true,
                "product", product,
                "message", "Product updated successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error updating product {}: {}", productId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to update product: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long productId) {
        logger.info("Deleting product with ID: {}", productId);

        try {
            productService.deleteProduct(productId);

            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Product deleted successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error deleting product {}: {}", productId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to delete product: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Search products with advanced filters")
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProducts(
            @Parameter(description = "Search query") @RequestParam String query,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int pageSize) {

        logger.debug("Searching products with query: {}", query);

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductResponse> products = productService.searchProducts(query, pageable);

        Map<String, Object> response = Map.of(
            "success", true,
            "products", products.getContent(),
            "pagination", Map.of(
                "page", products.getNumber(),
                "size", products.getSize(),
                "totalElements", products.getTotalElements(),
                "totalPages", products.getTotalPages(),
                "hasNext", products.hasNext()
            )
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get trending products")
    @GetMapping("/trending")
    public ResponseEntity<Map<String, Object>> getTrendingProducts(
            @Parameter(description = "Number of products to return") @RequestParam(defaultValue = "10") int limit) {

        logger.debug("Getting trending products with limit: {}", limit);

        List<ProductResponse> products = productService.getTrendingProducts(limit);

        Map<String, Object> response = Map.of(
            "success", true,
            "products", products
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get personalized product recommendations")
    @GetMapping("/recommendations")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> getRecommendations(
            @Parameter(description = "Number of recommendations") @RequestParam(defaultValue = "10") int limit) {

        logger.debug("Getting product recommendations with limit: {}", limit);

        // TODO: Implement personalized recommendations based on user behavior
        // For now, return trending products as recommendations
        List<ProductResponse> products = productService.getTrendingProducts(limit);

        Map<String, Object> response = Map.of(
            "success", true,
            "products", products,
            "message", "Personalized recommendations (currently showing trending products)"
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Upload product images")
    @PostMapping("/{productId}/images")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Map<String, Object>> uploadProductImages(
            @PathVariable Long productId,
            @Parameter(description = "Product images") @RequestParam("images") List<MultipartFile> images) {

        logger.info("Uploading {} images for product ID: {}", images.size(), productId);

        try {
            ProductResponse product = productService.uploadProductImages(productId, images);

            Map<String, Object> response = Map.of(
                "success", true,
                "product", product,
                "message", "Images uploaded successfully"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error uploading images for product {}: {}", productId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to upload images: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @Operation(summary = "Feature product (admin/premium seller only)")
    @PostMapping("/{productId}/feature")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PREMIUM_SELLER')")
    public ResponseEntity<Map<String, Object>> featureProduct(
            @PathVariable Long productId,
            @Parameter(description = "Duration in hours") @RequestParam(defaultValue = "24") int durationHours) {

        logger.info("Featuring product ID: {} for {} hours", productId, durationHours);

        try {
            productService.featureProduct(productId, durationHours);

            Map<String, Object> response = Map.of(
                "success", true,
                "message", "Product featured successfully for " + durationHours + " hours"
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error featuring product {}: {}", productId, e.getMessage(), e);
            Map<String, Object> errorResponse = Map.of(
                "success", false,
                "message", "Failed to feature product: " + e.getMessage()
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
