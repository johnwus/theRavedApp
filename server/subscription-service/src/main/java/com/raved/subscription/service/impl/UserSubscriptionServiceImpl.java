package com.raved.subscription.service.impl;

import com.raved.subscription.dto.request.CreateUserSubscriptionRequest;
import com.raved.subscription.dto.response.UserSubscriptionResponse;
import com.raved.subscription.mapper.UserSubscriptionMapper;
import com.raved.subscription.model.UserSubscription;
import com.raved.subscription.repository.UserSubscriptionRepository;
import com.raved.subscription.service.UserSubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserSubscriptionService
 */
@Service
@Transactional
public class UserSubscriptionServiceImpl implements UserSubscriptionService {

    private static final Logger logger = LoggerFactory.getLogger(UserSubscriptionServiceImpl.class);

    @Autowired
    private UserSubscriptionRepository subscriptionRepository;

    @Autowired
    private UserSubscriptionMapper subscriptionMapper;

    @Override
    public UserSubscriptionResponse createSubscription(CreateUserSubscriptionRequest request) {
        logger.info("Creating new subscription for user ID: {}", request.getUserId());

        // Check if user already has an active subscription
        if (hasActiveSubscription(request.getUserId())) {
            throw new RuntimeException("User already has an active subscription");
        }

        UserSubscription subscription = subscriptionMapper.toUserSubscription(request);
        UserSubscription savedSubscription = subscriptionRepository.save(subscription);

        logger.info("Subscription created successfully with ID: {}", savedSubscription.getId());
        return subscriptionMapper.toUserSubscriptionResponse(savedSubscription);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSubscriptionResponse getSubscriptionById(Long id) {
        logger.debug("Getting subscription by ID: {}", id);

        Optional<UserSubscription> subscriptionOpt = subscriptionRepository.findById(id);
        return subscriptionOpt.map(subscriptionMapper::toUserSubscriptionResponse).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSubscriptionResponse getSubscriptionByUserId(Long userId) {
        logger.debug("Getting subscription for user ID: {}", userId);

        Optional<UserSubscription> subscriptionOpt = subscriptionRepository.findByUserId(userId);
        return subscriptionOpt.map(subscriptionMapper::toUserSubscriptionResponse).orElse(null);
    }

    @Override
    public UserSubscriptionResponse updateSubscription(Long id, CreateUserSubscriptionRequest request) {
        logger.info("Updating subscription with ID: {}", id);

        Optional<UserSubscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) {
            throw new RuntimeException("Subscription not found with ID: " + id);
        }

        UserSubscription subscription = subscriptionOpt.get();
        subscription.setPlanId(request.getPlanId());
        subscription.setStatus(request.getStatus());
        subscription.setTrialEndAt(request.getTrialEndAt());
        subscription.setCurrentPeriodStart(request.getCurrentPeriodStart());
        subscription.setCurrentPeriodEnd(request.getCurrentPeriodEnd());
        subscription.setCancelAtPeriodEnd(request.getCancelAtPeriodEnd());

        UserSubscription savedSubscription = subscriptionRepository.save(subscription);
        logger.info("Subscription updated successfully with ID: {}", id);

        return subscriptionMapper.toUserSubscriptionResponse(savedSubscription);
    }

    @Override
    public UserSubscriptionResponse cancelSubscription(Long id) {
        logger.info("Canceling subscription with ID: {}", id);

        Optional<UserSubscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) {
            throw new RuntimeException("Subscription not found with ID: " + id);
        }

        UserSubscription subscription = subscriptionOpt.get();
        subscription.cancel();

        UserSubscription savedSubscription = subscriptionRepository.save(subscription);
        logger.info("Subscription canceled successfully with ID: {}", id);

        return subscriptionMapper.toUserSubscriptionResponse(savedSubscription);
    }

    @Override
    public UserSubscriptionResponse activateSubscription(Long id) {
        logger.info("Activating subscription with ID: {}", id);

        Optional<UserSubscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) {
            throw new RuntimeException("Subscription not found with ID: " + id);
        }

        UserSubscription subscription = subscriptionOpt.get();
        subscription.activate();

        UserSubscription savedSubscription = subscriptionRepository.save(subscription);
        logger.info("Subscription activated successfully with ID: {}", id);

        return subscriptionMapper.toUserSubscriptionResponse(savedSubscription);
    }

    @Override
    public UserSubscriptionResponse expireSubscription(Long id) {
        logger.info("Expiring subscription with ID: {}", id);

        Optional<UserSubscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) {
            throw new RuntimeException("Subscription not found with ID: " + id);
        }

        UserSubscription subscription = subscriptionOpt.get();
        subscription.expire();

        UserSubscription savedSubscription = subscriptionRepository.save(subscription);
        logger.info("Subscription expired successfully with ID: {}", id);

        return subscriptionMapper.toUserSubscriptionResponse(savedSubscription);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSubscriptionResponse> getSubscriptionsByStatus(String status, Pageable pageable) {
        logger.debug("Getting subscriptions by status: {}", status);

        Page<UserSubscription> subscriptions = subscriptionRepository.findByStatusOrderByCreatedAtDesc(status,
                pageable);
        return subscriptions.map(subscriptionMapper::toUserSubscriptionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserSubscriptionResponse> getSubscriptionsByPlan(Long planId, Pageable pageable) {
        logger.debug("Getting subscriptions by plan ID: {}", planId);

        Page<UserSubscription> subscriptions = subscriptionRepository.findByPlanIdOrderByCreatedAtDesc(planId,
                pageable);
        return subscriptions.map(subscriptionMapper::toUserSubscriptionResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSubscriptionResponse> getSubscriptionsExpiringSoon(Instant startDate, Instant endDate) {
        logger.debug("Getting subscriptions expiring between {} and {}", startDate, endDate);

        List<UserSubscription> subscriptions = subscriptionRepository.findExpiringSoon(startDate, endDate);
        return subscriptions.stream()
                .map(subscriptionMapper::toUserSubscriptionResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserSubscriptionResponse> getTrialSubscriptionsEndingSoon(Instant startDate, Instant endDate) {
        logger.debug("Getting trial subscriptions ending between {} and {}", startDate, endDate);

        List<UserSubscription> subscriptions = subscriptionRepository.findTrialEndingSoon(startDate, endDate);
        return subscriptions.stream()
                .map(subscriptionMapper::toUserSubscriptionResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasActiveSubscription(Long userId) {
        return subscriptionRepository.existsByUserIdAndStatusIn(userId, Arrays.asList("ACTIVE", "TRIALING"));
    }

    @Override
    @Transactional(readOnly = true)
    public long getSubscriptionCountByStatus(String status) {
        return subscriptionRepository.countByStatus(status);
    }

    @Override
    @Transactional(readOnly = true)
    public long getSubscriptionCountByPlan(Long planId) {
        return subscriptionRepository.countByPlanId(planId);
    }
}