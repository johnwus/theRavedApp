package com.raved.subscription.repository;

import com.raved.subscription.model.SubscriptionPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for SubscriptionPlan entities
 */
@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

    /**
     * Find plan by plan code
     */
    Optional<SubscriptionPlan> findByPlanCode(String planCode);

    /**
     * Find active plans
     */
    List<SubscriptionPlan> findByIsActiveTrueOrderByPriceAmountAsc();

    /**
     * Find plans by billing cycle
     */
    List<SubscriptionPlan> findByBillingCycleAndIsActiveTrueOrderByPriceAmountAsc(String billingCycle);

    /**
     * Find plans by price range
     */
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.isActive = true AND p.priceAmount BETWEEN :minPrice AND :maxPrice ORDER BY p.priceAmount ASC")
    Page<SubscriptionPlan> findByPriceRange(@Param("minPrice") java.math.BigDecimal minPrice, 
                                           @Param("maxPrice") java.math.BigDecimal maxPrice, 
                                           Pageable pageable);

    /**
     * Search plans by name or description
     */
    @Query("SELECT p FROM SubscriptionPlan p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY p.priceAmount ASC")
    Page<SubscriptionPlan> searchPlans(@Param("query") String query, Pageable pageable);

    /**
     * Count active plans
     */
    long countByIsActiveTrue();

    /**
     * Count plans by billing cycle
     */
    long countByBillingCycleAndIsActiveTrue(String billingCycle);
}

