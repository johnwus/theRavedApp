package com.raved.subscription.service;

import com.raved.subscription.dto.request.CreateSubscriptionPlanRequest;
import com.raved.subscription.dto.response.SubscriptionPlanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for managing subscription plans
 */
public interface SubscriptionPlanService {

    /**
     * Create a new subscription plan
     */
    SubscriptionPlanResponse createPlan(CreateSubscriptionPlanRequest request);

    /**
     * Get plan by ID
     */
    SubscriptionPlanResponse getPlanById(Long id);

    /**
     * Get plan by plan code
     */
    SubscriptionPlanResponse getPlanByCode(String planCode);

    /**
     * Update an existing plan
     */
    SubscriptionPlanResponse updatePlan(Long id, CreateSubscriptionPlanRequest request);

    /**
     * Delete a plan
     */
    void deletePlan(Long id);

    /**
     * Get all active plans
     */
    List<SubscriptionPlanResponse> getActivePlans();

    /**
     * Get plans by billing cycle
     */
    List<SubscriptionPlanResponse> getPlansByBillingCycle(String billingCycle);

    /**
     * Get plans by price range
     */
    Page<SubscriptionPlanResponse> getPlansByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    /**
     * Search plans
     */
    Page<SubscriptionPlanResponse> searchPlans(String query, Pageable pageable);

    /**
     * Toggle plan active status
     */
    SubscriptionPlanResponse togglePlanStatus(Long planId);

    /**
     * Get plan statistics
     */
    long getActivePlanCount();

    long getPlanCountByBillingCycle(String billingCycle);
}
