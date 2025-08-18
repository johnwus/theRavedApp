package com.raved.subscription.repository;

import com.raved.subscription.model.UserSubscription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Repository for UserSubscription entities
 */
@Repository
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {

    /**
     * Find subscription by user ID
     */
    Optional<UserSubscription> findByUserId(Long userId);

    /**
     * Find active subscriptions
     */
    List<UserSubscription> findByStatusInOrderByCreatedAtDesc(List<String> statuses);

    /**
     * Find subscriptions by status
     */
    Page<UserSubscription> findByStatusOrderByCreatedAtDesc(String status, Pageable pageable);

    /**
     * Find subscriptions by plan ID
     */
    Page<UserSubscription> findByPlanIdOrderByCreatedAtDesc(Long planId, Pageable pageable);

    /**
     * Find subscriptions expiring soon
     */
    @Query("SELECT us FROM UserSubscription us WHERE us.currentPeriodEnd BETWEEN :startDate AND :endDate AND us.status = 'ACTIVE' ORDER BY us.currentPeriodEnd ASC")
    List<UserSubscription> findExpiringSoon(@Param("startDate") Instant startDate, @Param("endDate") Instant endDate);

    /**
     * Find trial subscriptions ending soon
     */
    @Query("SELECT us FROM UserSubscription us WHERE us.trialEndAt BETWEEN :startDate AND :endDate AND us.status = 'TRIALING' ORDER BY us.trialEndAt ASC")
    List<UserSubscription> findTrialEndingSoon(@Param("startDate") Instant startDate,
            @Param("endDate") Instant endDate);

    /**
     * Count active subscriptions
     */
    long countByStatus(String status);

    /**
     * Count subscriptions by plan
     */
    long countByPlanId(Long planId);

    /**
     * Check if user has active subscription
     */
    boolean existsByUserIdAndStatusIn(Long userId, List<String> statuses);
}
