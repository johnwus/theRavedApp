package com.raved.subscription.controller;

import com.raved.subscription.dto.request.CreateUserSubscriptionRequest;
import com.raved.subscription.dto.response.UserSubscriptionResponse;
import com.raved.subscription.service.UserSubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/**
 * REST controller for managing user subscriptions
 */
@RestController
@RequestMapping("/api/v1/user-subscriptions")
@CrossOrigin(origins = "*")
public class UserSubscriptionController {

    private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionController.class);

    @Autowired
    private UserSubscriptionService subscriptionService;

    /**
     * Create a new user subscription
     */
    @PostMapping
    public ResponseEntity<UserSubscriptionResponse> createSubscription(
            @RequestBody CreateUserSubscriptionRequest request) {
        logger.info("Creating new subscription for user ID: {}", request.getUserId());

        try {
            UserSubscriptionResponse response = subscriptionService.createSubscription(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating subscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get subscription by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSubscriptionResponse> getSubscriptionById(@PathVariable Long id) {
        logger.debug("Getting subscription by ID: {}", id);

        try {
            UserSubscriptionResponse response = subscriptionService.getSubscriptionById(id);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting subscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get subscription by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserSubscriptionResponse> getSubscriptionByUserId(@PathVariable Long userId) {
        logger.debug("Getting subscription for user ID: {}", userId);

        try {
            UserSubscriptionResponse response = subscriptionService.getSubscriptionByUserId(userId);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting subscription by user ID: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing subscription
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSubscriptionResponse> updateSubscription(@PathVariable Long id,
            @RequestBody CreateUserSubscriptionRequest request) {
        logger.info("Updating subscription with ID: {}", id);

        try {
            UserSubscriptionResponse response = subscriptionService.updateSubscription(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Subscription not found for update: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating subscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Cancel a subscription
     */
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<UserSubscriptionResponse> cancelSubscription(@PathVariable Long id) {
        logger.info("Canceling subscription with ID: {}", id);

        try {
            UserSubscriptionResponse response = subscriptionService.cancelSubscription(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Subscription not found for cancellation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error canceling subscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Activate a subscription
     */
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserSubscriptionResponse> activateSubscription(@PathVariable Long id) {
        logger.info("Activating subscription with ID: {}", id);

        try {
            UserSubscriptionResponse response = subscriptionService.activateSubscription(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Subscription not found for activation: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error activating subscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Expire a subscription
     */
    @PatchMapping("/{id}/expire")
    public ResponseEntity<UserSubscriptionResponse> expireSubscription(@PathVariable Long id) {
        logger.info("Expiring subscription with ID: {}", id);

        try {
            UserSubscriptionResponse response = subscriptionService.expireSubscription(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Subscription not found for expiration: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error expiring subscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get subscriptions by status
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<UserSubscriptionResponse>> getSubscriptionsByStatus(
            @PathVariable String status,
            Pageable pageable) {
        logger.debug("Getting subscriptions by status: {}", status);

        try {
            Page<UserSubscriptionResponse> response = subscriptionService.getSubscriptionsByStatus(status, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting subscriptions by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get subscriptions by plan
     */
    @GetMapping("/plan/{planId}")
    public ResponseEntity<Page<UserSubscriptionResponse>> getSubscriptionsByPlan(
            @PathVariable Long planId,
            Pageable pageable) {
        logger.debug("Getting subscriptions by plan ID: {}", planId);

        try {
            Page<UserSubscriptionResponse> response = subscriptionService.getSubscriptionsByPlan(planId, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting subscriptions by plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get subscriptions expiring soon
     */
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<UserSubscriptionResponse>> getSubscriptionsExpiringSoon(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        logger.debug("Getting subscriptions expiring between {} and {}", startDate, endDate);

        try {
            List<UserSubscriptionResponse> response = subscriptionService.getSubscriptionsExpiringSoon(startDate,
                    endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting expiring subscriptions: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get trial subscriptions ending soon
     */
    @GetMapping("/trial-ending-soon")
    public ResponseEntity<List<UserSubscriptionResponse>> getTrialSubscriptionsEndingSoon(
            @RequestParam Instant startDate,
            @RequestParam Instant endDate) {
        logger.debug("Getting trial subscriptions ending between {} and {}", startDate, endDate);

        try {
            List<UserSubscriptionResponse> response = subscriptionService.getTrialSubscriptionsEndingSoon(startDate,
                    endDate);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting trial subscriptions ending soon: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Check if user has active subscription
     */
    @GetMapping("/user/{userId}/has-active")
    public ResponseEntity<Boolean> hasActiveSubscription(@PathVariable Long userId) {
        logger.debug("Checking if user {} has active subscription", userId);

        try {
            boolean hasActive = subscriptionService.hasActiveSubscription(userId);
            return ResponseEntity.ok(hasActive);
        } catch (Exception e) {
            logger.error("Error checking active subscription: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get subscription statistics
     */
    @GetMapping("/stats/status/{status}/count")
    public ResponseEntity<Long> getSubscriptionCountByStatus(@PathVariable String status) {
        logger.debug("Getting subscription count by status: {}", status);

        try {
            long count = subscriptionService.getSubscriptionCountByStatus(status);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting subscription count by status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/plan/{planId}/count")
    public ResponseEntity<Long> getSubscriptionCountByPlan(@PathVariable Long planId) {
        logger.debug("Getting subscription count by plan ID: {}", planId);

        try {
            long count = subscriptionService.getSubscriptionCountByPlan(planId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting subscription count by plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
