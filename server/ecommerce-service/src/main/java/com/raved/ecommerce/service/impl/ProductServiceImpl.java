package com.raved.ecommerce.service.impl;

import com.raved.ecommerce.dto.request.CreateProductRequest;
import com.raved.ecommerce.dto.request.UpdateProductRequest;
import com.raved.ecommerce.dto.response.ProductResponse;
import com.raved.ecommerce.exception.ProductNotFoundException;
import com.raved.ecommerce.mapper.ProductMapper;
import com.raved.ecommerce.model.Product;
import com.raved.ecommerce.repository.ProductRepository;
import com.raved.ecommerce.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ProductService
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        logger.info("Creating new product: {}", request.getName());
        
        Product product = productMapper.toProduct(request);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setStatus(Product.ProductStatus.ACTIVE);
        
        Product savedProduct = productRepository.save(product);
        logger.info("Product created successfully with ID: {}", savedProduct.getId());
        
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductResponse> getProductById(Long id) {
        logger.debug("Getting product by ID: {}", id);
        
        Optional<Product> productOpt = productRepository.findByIdAndIsDeletedFalse(id);
        return productOpt.map(productMapper::toProductResponse);
    }

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        logger.info("Updating product with ID: {}", id);
        
        Optional<Product> productOpt = productRepository.findByIdAndIsDeletedFalse(id);
        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        
        Product product = productOpt.get();
        productMapper.updateProductFromRequest(product, request);
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        logger.info("Product updated successfully with ID: {}", id);
        
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        
        Optional<Product> productOpt = productRepository.findByIdAndIsDeletedFalse(id);
        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        
        Product product = productOpt.get();
        product.setIsDeleted(true);
        product.setUpdatedAt(LocalDateTime.now());
        
        productRepository.save(product);
        logger.info("Product deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsBySeller(Long sellerId, Pageable pageable) {
        logger.debug("Getting products for seller ID: {}", sellerId);
        
        Page<Product> products = productRepository.findBySellerIdAndIsDeletedFalseOrderByCreatedAtDesc(sellerId, pageable);
        return products.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        logger.debug("Getting products for category ID: {}", categoryId);
        
        Page<Product> products = productRepository.findByCategoryIdAndIsDeletedFalseAndStatusOrderByCreatedAtDesc(
                categoryId, Product.ProductStatus.ACTIVE, pageable);
        return products.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProducts(String query, Pageable pageable) {
        logger.debug("Searching products with query: {}", query);
        
        Page<Product> products = productRepository.searchProducts(query, pageable);
        return products.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getFeaturedProducts(int limit) {
        logger.debug("Getting featured products with limit: {}", limit);
        
        List<Product> products = productRepository.findByIsFeaturedTrueAndIsDeletedFalseAndStatusAndFeaturedUntilAfterOrderByCreatedAtDesc(
                Product.ProductStatus.ACTIVE, LocalDateTime.now()).stream().limit(limit).collect(Collectors.toList());
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        logger.debug("Getting products by price range: {} - {}", minPrice, maxPrice);
        
        Page<Product> products = productRepository.findByPriceBetweenAndIsDeletedFalseAndStatusOrderByPriceAsc(
                minPrice, maxPrice, Product.ProductStatus.ACTIVE, pageable);
        return products.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getTrendingProducts(int limit) {
        logger.debug("Getting trending products with limit: {}", limit);
        
        List<Product> products = productRepository.findTrendingProducts(limit);
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponse uploadProductImages(Long productId, List<MultipartFile> images) {
        logger.info("Uploading {} images for product ID: {}", images.size(), productId);
        
        Optional<Product> productOpt = productRepository.findByIdAndIsDeletedFalse(productId);
        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException("Product not found with ID: " + productId);
        }
        
        Product product = productOpt.get();
        
        // TODO: Implement actual file upload logic
        // For now, we'll just set placeholder URLs
        StringBuilder imageUrls = new StringBuilder();
        for (int i = 0; i < images.size(); i++) {
            if (i > 0) imageUrls.append(",");
            imageUrls.append("/uploads/products/").append(productId).append("/").append(images.get(i).getOriginalFilename());
        }
        
        product.setImageUrls(imageUrls.toString());
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        logger.info("Product images uploaded successfully for ID: {}", productId);
        
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProductStatus(Long id, Product.ProductStatus status) {
        logger.info("Updating product status for ID: {} to {}", id, status);
        
        Optional<Product> productOpt = productRepository.findByIdAndIsDeletedFalse(id);
        if (productOpt.isEmpty()) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        
        Product product = productOpt.get();
        product.setStatus(status);
        product.setUpdatedAt(LocalDateTime.now());
        
        Product savedProduct = productRepository.save(product);
        logger.info("Product status updated successfully for ID: {}", id);
        
        return productMapper.toProductResponse(savedProduct);
    }

    @Override
    public void featureProduct(Long productId, int durationHours) {
        logger.info("Featuring product ID: {} for {} hours", productId, durationHours);
        
        Optional<Product> productOpt = productRepository.findByIdAndIsDeletedFalse(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setIsFeatured(true);
            product.setFeaturedUntil(LocalDateTime.now().plusHours(durationHours));
            product.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product);
            
            logger.info("Product featured successfully: {}", productId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductsByUniversity(Long universityId, Pageable pageable) {
        logger.debug("Getting products for university ID: {}", universityId);
        
        Page<Product> products = productRepository.findByUniversityIdAndIsDeletedFalseAndStatusOrderByCreatedAtDesc(
                universityId, Product.ProductStatus.ACTIVE, pageable);
        return products.map(productMapper::toProductResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getLowStockProducts(Long sellerId, int threshold) {
        logger.debug("Getting low stock products for seller ID: {} with threshold: {}", sellerId, threshold);
        
        List<Product> products = productRepository.findBySellerIdAndStockQuantityLessThanAndIsDeletedFalseAndStatus(
                sellerId, threshold, Product.ProductStatus.ACTIVE);
        return products.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void updateProductInventory(Long productId, int quantity) {
        logger.info("Updating inventory for product ID: {} to quantity: {}", productId, quantity);
        
        Optional<Product> productOpt = productRepository.findByIdAndIsDeletedFalse(productId);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.setStockQuantity(quantity);
            product.setUpdatedAt(LocalDateTime.now());
            
            // Auto-update status based on stock
            if (quantity <= 0) {
                product.setStatus(Product.ProductStatus.OUT_OF_STOCK);
            } else if (product.getStatus() == Product.ProductStatus.OUT_OF_STOCK) {
                product.setStatus(Product.ProductStatus.ACTIVE);
            }
            
            productRepository.save(product);
            logger.info("Product inventory updated successfully for ID: {}", productId);
        }
    }
}
