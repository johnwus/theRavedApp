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
     * Find product by ID and active
     */
    Optional<Product> findByIdAndIsActiveTrue(Long id);

    /**
     * Find products by seller
     */
    Page<Product> findBySellerIdAndIsActiveTrueOrderByCreatedAtDesc(Long sellerId, Pageable pageable);

    /**
     * Find products by category
     */
    Page<Product> findByCategoryIdAndIsActiveTrueOrderByCreatedAtDesc(Long categoryId, Pageable pageable);

    /**
     * Find products by price range
     */
    Page<Product> findByPriceBetweenAndIsActiveTrueOrderByPriceAsc(
            BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Find featured products
     */
    List<Product> findByIsFeaturedTrueAndIsActiveTrueAndFeaturedUntilAfterOrderByCreatedAtDesc(LocalDateTime now);

    /**
     * Find low stock products
     */
    List<Product> findBySellerIdAndQuantityLessThanAndIsActiveTrue(Long sellerId, int threshold);

    /**
     * Search products by title or description
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "AND (LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY p.createdAt DESC")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);

    /**
     * Find trending products based on views and likes
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "ORDER BY (p.viewsCount * 0.1 + p.likesCount * 2) DESC")
    List<Product> findTrendingProducts(@Param("limit") int limit);

    /**
     * Count products by seller
     */
    long countBySellerIdAndIsActiveTrue(Long sellerId);

    /**
     * Count products by category
     */
    long countByCategoryIdAndIsActiveTrue(Long categoryId);

    /**
     * Find recently added products
     */
    @Query("SELECT p FROM Product p WHERE p.isActive = true " +
           "AND p.createdAt >= :since ORDER BY p.createdAt DESC")
    List<Product> findRecentProducts(@Param("since") LocalDateTime since, Pageable pageable);
}
