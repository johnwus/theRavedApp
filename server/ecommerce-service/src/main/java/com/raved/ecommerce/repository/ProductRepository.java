package com.raved.ecommerce.repository;

import com.raved.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * ProductRepository for TheRavedApp
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find product by ID and not deleted
     */
    Optional<Product> findByIdAndIsDeletedFalse(Long id);

    /**
     * Find products by seller
     */
    Page<Product> findBySellerIdAndIsDeletedFalseOrderByCreatedAtDesc(Long sellerId, Pageable pageable);

    /**
     * Find products by category
     */
    Page<Product> findByCategoryIdAndIsDeletedFalseAndStatusOrderByCreatedAtDesc(
            Long categoryId, Product.ProductStatus status, Pageable pageable);

    /**
     * Find products by university
     */
    Page<Product> findByUniversityIdAndIsDeletedFalseAndStatusOrderByCreatedAtDesc(
            Long universityId, Product.ProductStatus status, Pageable pageable);

    /**
     * Find products by price range
     */
    Page<Product> findByPriceBetweenAndIsDeletedFalseAndStatusOrderByPriceAsc(
            BigDecimal minPrice, BigDecimal maxPrice, Product.ProductStatus status, Pageable pageable);

    /**
     * Find featured products
     */
    List<Product> findByIsFeaturedTrueAndIsDeletedFalseAndStatusAndFeaturedUntilAfterOrderByCreatedAtDesc(
            Product.ProductStatus status, LocalDateTime now);

    /**
     * Find low stock products
     */
    List<Product> findBySellerIdAndStockQuantityLessThanAndIsDeletedFalseAndStatus(
            Long sellerId, int threshold, Product.ProductStatus status);

    /**
     * Search products by name or description
     */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.status = 'ACTIVE' " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY p.createdAt DESC")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);

    /**
     * Find trending products based on orders and views
     */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.status = 'ACTIVE' " +
           "ORDER BY (p.ordersCount * 5 + p.viewsCount * 0.1 + p.likesCount * 2) DESC")
    List<Product> findTrendingProducts(@Param("limit") int limit);

    /**
     * Count products by seller
     */
    long countBySellerIdAndIsDeletedFalse(Long sellerId);

    /**
     * Count products by category
     */
    long countByCategoryIdAndIsDeletedFalseAndStatus(Long categoryId, Product.ProductStatus status);

    /**
     * Find products by status
     */
    List<Product> findByStatusAndIsDeletedFalse(Product.ProductStatus status);

    /**
     * Find recently added products
     */
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND p.status = 'ACTIVE' " +
           "AND p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<Product> findRecentProducts(@Param("since") LocalDateTime since, Pageable pageable);
}
