package com.raved.subscription.controller;

import com.raved.subscription.dto.request.CreateSubscriptionPlanRequest;
import com.raved.subscription.dto.response.SubscriptionPlanResponse;
import com.raved.subscription.service.SubscriptionPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for managing subscription plans
 */
@RestController
@RequestMapping("/api/v1/subscription-plans")
@CrossOrigin(origins = "*")
public class SubscriptionPlanController {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionPlanController.class);

    @Autowired
    private SubscriptionPlanService planService;

    /**
     * Create a new subscription plan
     */
    @PostMapping
    public ResponseEntity<SubscriptionPlanResponse> createPlan(@RequestBody CreateSubscriptionPlanRequest request) {
        logger.info("Creating new subscription plan: {}", request.getName());

        try {
            SubscriptionPlanResponse response = planService.createPlan(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            logger.error("Error creating subscription plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get plan by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> getPlanById(@PathVariable Long id) {
        logger.debug("Getting subscription plan by ID: {}", id);

        try {
            SubscriptionPlanResponse response = planService.getPlanById(id);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting subscription plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get plan by plan code
     */
    @GetMapping("/code/{planCode}")
    public ResponseEntity<SubscriptionPlanResponse> getPlanByCode(@PathVariable String planCode) {
        logger.debug("Getting subscription plan by code: {}", planCode);

        try {
            SubscriptionPlanResponse response = planService.getPlanByCode(planCode);
            if (response != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error getting subscription plan by code: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Update an existing plan
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionPlanResponse> updatePlan(@PathVariable Long id,
            @RequestBody CreateSubscriptionPlanRequest request) {
        logger.info("Updating subscription plan with ID: {}", id);

        try {
            SubscriptionPlanResponse response = planService.updatePlan(id, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Subscription plan not found for update: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error updating subscription plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Delete a plan
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlan(@PathVariable Long id) {
        logger.info("Deleting subscription plan with ID: {}", id);

        try {
            planService.deletePlan(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting subscription plan: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get all active plans
     */
    @GetMapping("/active")
    public ResponseEntity<List<SubscriptionPlanResponse>> getActivePlans() {
        logger.debug("Getting active subscription plans");

        try {
            List<SubscriptionPlanResponse> response = planService.getActivePlans();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting active plans: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get plans by billing cycle
     */
    @GetMapping("/billing-cycle/{billingCycle}")
    public ResponseEntity<List<SubscriptionPlanResponse>> getPlansByBillingCycle(@PathVariable String billingCycle) {
        logger.debug("Getting subscription plans by billing cycle: {}", billingCycle);

        try {
            List<SubscriptionPlanResponse> response = planService.getPlansByBillingCycle(billingCycle);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting plans by billing cycle: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get plans by price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<Page<SubscriptionPlanResponse>> getPlansByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {
        logger.debug("Getting subscription plans by price range: {} - {}", minPrice, maxPrice);

        try {
            Page<SubscriptionPlanResponse> response = planService.getPlansByPriceRange(minPrice, maxPrice, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error getting plans by price range: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Search plans
     */
    @GetMapping("/search")
    public ResponseEntity<Page<SubscriptionPlanResponse>> searchPlans(
            @RequestParam String query,
            Pageable pageable) {
        logger.debug("Searching subscription plans with query: {}", query);

        try {
            Page<SubscriptionPlanResponse> response = planService.searchPlans(query, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error searching plans: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Toggle plan active status
     */
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<SubscriptionPlanResponse> togglePlanStatus(@PathVariable Long id) {
        logger.info("Toggling status for subscription plan ID: {}", id);

        try {
            SubscriptionPlanResponse response = planService.togglePlanStatus(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.warn("Subscription plan not found for status toggle: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            logger.error("Error toggling plan status: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Get plan statistics
     */
    @GetMapping("/stats/active-count")
    public ResponseEntity<Long> getActivePlanCount() {
        logger.debug("Getting active plan count");

        try {
            long count = planService.getActivePlanCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting active plan count: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/stats/billing-cycle/{billingCycle}/count")
    public ResponseEntity<Long> getPlanCountByBillingCycle(@PathVariable String billingCycle) {
        logger.debug("Getting plan count by billing cycle: {}", billingCycle);

        try {
            long count = planService.getPlanCountByBillingCycle(billingCycle);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            logger.error("Error getting plan count by billing cycle: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
