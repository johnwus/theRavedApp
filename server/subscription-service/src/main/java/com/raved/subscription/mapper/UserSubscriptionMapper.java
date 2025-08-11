package com.raved.subscription.mapper;

import com.raved.subscription.dto.request.CreateUserSubscriptionRequest;
import com.raved.subscription.dto.response.UserSubscriptionResponse;
import com.raved.subscription.model.UserSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Mapper for UserSubscription entity and DTOs
 */
@Component
public class UserSubscriptionMapper {

    @Autowired
    private SubscriptionPlanMapper planMapper;

    public UserSubscriptionResponse toUserSubscriptionResponse(UserSubscription subscription) {
        if (subscription == null) {
            return null;
        }

        UserSubscriptionResponse response = new UserSubscriptionResponse();
        response.setId(subscription.getId());
        response.setUserId(subscription.getUserId());
        response.setPlanId(subscription.getPlanId());
        response.setStatus(subscription.getStatus());
        response.setTrialEndAt(subscription.getTrialEndAt());
        response.setCurrentPeriodStart(subscription.getCurrentPeriodStart());
        response.setCurrentPeriodEnd(subscription.getCurrentPeriodEnd());
        response.setCancelAtPeriodEnd(subscription.getCancelAtPeriodEnd());
        response.setCreatedAt(subscription.getCreatedAt());
        response.setUpdatedAt(subscription.getUpdatedAt());

        // Set computed fields
        response.setIsActive(subscription.isActive());
        response.setIsTrial(subscription.isTrial());
        response.setIsExpired(subscription.isExpired());
        response.setIsCanceled(subscription.isCanceled());

        // Set plan details if available
        if (subscription.getPlan() != null) {
            response.setPlan(planMapper.toSubscriptionPlanResponse(subscription.getPlan()));
        }

        return response;
    }

    public UserSubscription toUserSubscription(CreateUserSubscriptionRequest request) {
        if (request == null) {
            return null;
        }

        UserSubscription subscription = new UserSubscription();
        subscription.setUserId(request.getUserId());
        subscription.setPlanId(request.getPlanId());
        subscription.setStatus(request.getStatus());
        subscription.setTrialEndAt(request.getTrialEndAt());
        subscription.setCurrentPeriodStart(request.getCurrentPeriodStart());
        subscription.setCurrentPeriodEnd(request.getCurrentPeriodEnd());
        subscription.setCancelAtPeriodEnd(request.getCancelAtPeriodEnd());

        return subscription;
    }
}
