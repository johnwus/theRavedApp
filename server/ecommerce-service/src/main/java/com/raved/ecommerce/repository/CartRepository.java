package com.raved.ecommerce.repository;

import com.raved.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Cart entity
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserId(Long userId);
    
    List<Cart> findByUpdatedAtBefore(LocalDateTime cutoffDate);
    
    @Query("SELECT COUNT(c) FROM Cart c WHERE c.userId = :userId")
    long countByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(c.totalAmount) FROM Cart c WHERE c.userId = :userId")
    BigDecimal sumTotalAmountByUserId(@Param("userId") Long userId);
    
    @Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.totalAmount > :minAmount")
    List<Cart> findByUserIdAndTotalAmountGreaterThan(@Param("userId") Long userId, @Param("minAmount") BigDecimal minAmount);
    
    @Query("SELECT c FROM Cart c WHERE c.userId = :userId ORDER BY c.updatedAt DESC")
    List<Cart> findByUserIdOrderByUpdatedAtDesc(@Param("userId") Long userId);
    
    @Query("SELECT c FROM Cart c WHERE c.itemCount > :minItems")
    List<Cart> findByItemCountGreaterThan(@Param("minItems") int minItems);
    
    @Query("SELECT c FROM Cart c WHERE c.totalAmount BETWEEN :minAmount AND :maxAmount")
    List<Cart> findByTotalAmountBetween(@Param("minAmount") BigDecimal minAmount, @Param("maxAmount") BigDecimal maxAmount);
    
    @Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.discountCode IS NOT NULL")
    List<Cart> findByUserIdAndDiscountCodeNotNull(@Param("userId") Long userId);
    
    @Query("SELECT c FROM Cart c WHERE c.userId = :userId AND c.discountAmount > 0")
    List<Cart> findByUserIdAndDiscountAmountGreaterThanZero(@Param("userId") Long userId);
} 