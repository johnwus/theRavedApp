package com.raved.subscription.service.impl;

import com.raved.subscription.dto.request.CreateSubscriptionPlanRequest;
import com.raved.subscription.dto.response.SubscriptionPlanResponse;
import com.raved.subscription.mapper.SubscriptionPlanMapper;
import com.raved.subscription.model.SubscriptionPlan;
import com.raved.subscription.repository.SubscriptionPlanRepository;
import com.raved.subscription.service.SubscriptionPlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of SubscriptionPlanService
 */
@Service
@Transactional
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private static final Logger logger = LoggerFactory.getLogger(SubscriptionPlanServiceImpl.class);

    @Autowired
    private SubscriptionPlanRepository planRepository;

    @Autowired
    private SubscriptionPlanMapper planMapper;

    @Override
    public SubscriptionPlanResponse createPlan(CreateSubscriptionPlanRequest request) {
        logger.info("Creating new subscription plan: {}", request.getName());

        // Check if plan code already exists
        if (planRepository.findByPlanCode(request.getPlanCode()).isPresent()) {
            throw new RuntimeException("Plan code already exists: " + request.getPlanCode());
        }

        SubscriptionPlan plan = planMapper.toSubscriptionPlan(request);
        SubscriptionPlan savedPlan = planRepository.save(plan);

        logger.info("Subscription plan created successfully with ID: {}", savedPlan.getId());
        return planMapper.toSubscriptionPlanResponse(savedPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanResponse getPlanById(Long id) {
        logger.debug("Getting subscription plan by ID: {}", id);

        Optional<SubscriptionPlan> planOpt = planRepository.findById(id);
        return planOpt.map(planMapper::toSubscriptionPlanResponse).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanResponse getPlanByCode(String planCode) {
        logger.debug("Getting subscription plan by code: {}", planCode);

        Optional<SubscriptionPlan> planOpt = planRepository.findByPlanCode(planCode);
        return planOpt.map(planMapper::toSubscriptionPlanResponse).orElse(null);
    }

    @Override
    public SubscriptionPlanResponse updatePlan(Long id, CreateSubscriptionPlanRequest request) {
        logger.info("Updating subscription plan with ID: {}", id);

        Optional<SubscriptionPlan> planOpt = planRepository.findById(id);
        if (planOpt.isEmpty()) {
            throw new RuntimeException("Subscription plan not found with ID: " + id);
        }

        SubscriptionPlan plan = planOpt.get();
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPriceAmount(request.getPriceAmount());
        plan.setCurrency(request.getCurrency());
        plan.setBillingCycle(request.getBillingCycle());
        plan.setFeatures(request.getFeatures());
        plan.setIsActive(request.getIsActive());
        plan.setUpdatedAt(Instant.now());

        SubscriptionPlan savedPlan = planRepository.save(plan);
        logger.info("Subscription plan updated successfully with ID: {}", id);

        return planMapper.toSubscriptionPlanResponse(savedPlan);
    }

    @Override
    public void deletePlan(Long id) {
        logger.info("Deleting subscription plan with ID: {}", id);

        if (planRepository.existsById(id)) {
            planRepository.deleteById(id);
            logger.info("Subscription plan deleted successfully with ID: {}", id);
        } else {
            logger.warn("Subscription plan not found for deletion with ID: {}", id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getActivePlans() {
        logger.debug("Getting active subscription plans");

        List<SubscriptionPlan> plans = planRepository.findByIsActiveTrueOrderByPriceAmountAsc();
        return plans.stream()
                .map(planMapper::toSubscriptionPlanResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getPlansByBillingCycle(String billingCycle) {
        logger.debug("Getting subscription plans by billing cycle: {}", billingCycle);

        List<SubscriptionPlan> plans = planRepository.findByBillingCycleAndIsActiveTrueOrderByPriceAmountAsc(billingCycle);
        return plans.stream()
                .map(planMapper::toSubscriptionPlanResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionPlanResponse> getPlansByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        logger.debug("Getting subscription plans by price range: {} - {}", minPrice, maxPrice);

        Page<SubscriptionPlan> plans = planRepository.findByPriceRange(minPrice, maxPrice, pageable);
        return plans.map(planMapper::toSubscriptionPlanResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubscriptionPlanResponse> searchPlans(String query, Pageable pageable) {
        logger.debug("Searching subscription plans with query: {}", query);

        Page<SubscriptionPlan> plans = planRepository.searchPlans(query, pageable);
        return plans.map(planMapper::toSubscriptionPlanResponse);
    }

    @Override
    public SubscriptionPlanResponse togglePlanStatus(Long planId) {
        logger.info("Toggling status for subscription plan ID: {}", planId);

        Optional<SubscriptionPlan> planOpt = planRepository.findById(planId);
        if (planOpt.isEmpty()) {
            throw new RuntimeException("Subscription plan not found with ID: " + planId);
        }

        SubscriptionPlan plan = planOpt.get();
        plan.setIsActive(!plan.getIsActive());
        plan.setUpdatedAt(Instant.now());

        SubscriptionPlan savedPlan = planRepository.save(plan);
        logger.info("Subscription plan status toggled to {} for ID: {}", plan.getIsActive(), planId);

        return planMapper.toSubscriptionPlanResponse(savedPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public long getActivePlanCount() {
        return planRepository.countByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public long getPlanCountByBillingCycle(String billingCycle) {
        return planRepository.countByBillingCycleAndIsActiveTrue(billingCycle);
    }
}