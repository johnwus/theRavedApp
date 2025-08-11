package com.raved.subscription.service;

import com.raved.subscription.dto.request.CreateUserSubscriptionRequest;
import com.raved.subscription.dto.response.UserSubscriptionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.Instant;
import java.util.List;

/**
 * Service interface for managing user subscriptions
 */
public interface UserSubscriptionService {

    /**
     * Create a new user subscription
     */
    UserSubscriptionResponse createSubscription(CreateUserSubscriptionRequest request);

    /**
     * Get subscription by ID
     */
    UserSubscriptionResponse getSubscriptionById(Long id);

    /**
     * Get subscription by user ID
     */
    UserSubscriptionResponse getSubscriptionByUserId(Long userId);

    /**
     * Update an existing subscription
     */
    UserSubscriptionResponse updateSubscription(Long id, CreateUserSubscriptionRequest request);

    /**
     * Cancel a subscription
     */
    UserSubscriptionResponse cancelSubscription(Long id);

    /**
     * Activate a subscription
     */
    UserSubscriptionResponse activateSubscription(Long id);

    /**
     * Expire a subscription
     */
    UserSubscriptionResponse expireSubscription(Long id);

    /**
     * Get subscriptions by status
     */
    Page<UserSubscriptionResponse> getSubscriptionsByStatus(String status, Pageable pageable);

    /**
     * Get subscriptions by plan
     */
    Page<UserSubscriptionResponse> getSubscriptionsByPlan(Long planId, Pageable pageable);

    /**
     * Get subscriptions expiring soon
     */
    List<UserSubscriptionResponse> getSubscriptionsExpiringSoon(Instant startDate, Instant endDate);

    /**
     * Get trial subscriptions ending soon
     */
    List<UserSubscriptionResponse> getTrialSubscriptionsEndingSoon(Instant startDate, Instant endDate);

    /**
     * Check if user has active subscription
     */
    boolean hasActiveSubscription(Long userId);

    /**
     * Get subscription statistics
     */
    long getSubscriptionCountByStatus(String status);

    long getSubscriptionCountByPlan(Long planId);
}
