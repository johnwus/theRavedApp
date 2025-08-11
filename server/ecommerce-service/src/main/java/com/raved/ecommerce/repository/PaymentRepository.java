package com.raved.ecommerce.repository;

import com.raved.ecommerce.model.Payment;
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
 * PaymentRepository for TheRavedApp
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    Page<Payment> findByOrderIdOrderByInitiatedAtDesc(Long orderId, Pageable pageable);

    List<Payment> findByStatusOrderByInitiatedAtDesc(String status);

    List<Payment> findByStatusAndInitiatedAtAfter(String status, LocalDateTime after);

    long countByStatus(String status);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.orderId IN (SELECT o.id FROM Order o WHERE o.buyerId = :userId) AND p.status = 'SUCCESS'")
    BigDecimal getTotalPaymentAmountByUser(@Param("userId") Long userId);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS'")
    BigDecimal getTotalPaymentAmount();

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.status = 'SUCCESS' AND p.amount < 0")
    BigDecimal getTotalRefundAmount();

    /**
     * Find payments by user ID
     */
    Page<Payment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find payments by status
     */
    Page<Payment> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    /**
     * Find payments by payment ID
     */
    Optional<Payment> findByPaymentId(String paymentId);

    /**
     * Find payments by status and created at before
     */
    List<Payment> findByStatusAndCreatedAtBefore(String status, LocalDateTime dateTime);

    /**
     * Find payments by status and created at after
     */
    List<Payment> findByStatusAndCreatedAtAfter(String status, LocalDateTime dateTime);

    /**
     * Find payments by order ID
     */
    Page<Payment> findByOrderIdOrderByCreatedAtDesc(Long orderId, Pageable pageable);
}
