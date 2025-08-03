package com.raved.ecommerce.repository;

import com.raved.ecommerce.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * OrderRepository for TheRavedApp
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find orders by buyer
     */
    Page<Order> findByBuyerIdOrderByCreatedAtDesc(Long buyerId, Pageable pageable);

    /**
     * Find orders by seller
     */
    Page<Order> findBySellerIdOrderByCreatedAtDesc(Long sellerId, Pageable pageable);

    /**
     * Find orders by status
     */
    Page<Order> findByStatusOrderByCreatedAtDesc(Order.OrderStatus status, Pageable pageable);

    /**
     * Find orders by date range
     */
    Page<Order> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Count orders by seller and date range
     */
    long countBySellerIdAndCreatedAtBetween(Long sellerId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Sum total amount by seller and date range
     */
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.sellerId = :sellerId " +
           "AND o.createdAt BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalAmountBySellerIdAndCreatedAtBetween(@Param("sellerId") Long sellerId,
                                                          @Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);

    /**
     * Count orders by seller, status and date range
     */
    long countBySellerIdAndStatusAndCreatedAtBetween(Long sellerId, Order.OrderStatus status,
                                                    LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Find recent orders
     */
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(@Param("limit") int limit);

    /**
     * Find orders by product
     */
    List<Order> findByProductIdOrderByCreatedAtDesc(Long productId);

    /**
     * Find pending orders older than specified time
     */
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.createdAt < :cutoffTime")
    List<Order> findPendingOrdersOlderThan(@Param("cutoffTime") LocalDateTime cutoffTime);

    /**
     * Find orders by buyer and status
     */
    List<Order> findByBuyerIdAndStatusOrderByCreatedAtDesc(Long buyerId, Order.OrderStatus status);

    /**
     * Find orders by seller and status
     */
    List<Order> findBySellerIdAndStatusOrderByCreatedAtDesc(Long sellerId, Order.OrderStatus status);

    /**
     * Count total orders by buyer
     */
    long countByBuyerId(Long buyerId);

    /**
     * Count total orders by seller
     */
    long countBySellerId(Long sellerId);
}
