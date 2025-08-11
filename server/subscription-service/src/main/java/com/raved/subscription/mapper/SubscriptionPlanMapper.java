package com.raved.subscription.mapper;

import com.raved.subscription.dto.request.CreateSubscriptionPlanRequest;
import com.raved.subscription.dto.response.SubscriptionPlanResponse;
import com.raved.subscription.model.SubscriptionPlan;
import org.springframework.stereotype.Component;

/**
 * Mapper for SubscriptionPlan entity and DTOs
 */
@Component
public class SubscriptionPlanMapper {

    public SubscriptionPlanResponse toSubscriptionPlanResponse(SubscriptionPlan plan) {
        if (plan == null) {
            return null;
        }

        SubscriptionPlanResponse response = new SubscriptionPlanResponse();
        response.setId(plan.getId());
        response.setPlanCode(plan.getPlanCode());
        response.setName(plan.getName());
        response.setDescription(plan.getDescription());
        response.setPriceAmount(plan.getPriceAmount());
        response.setCurrency(plan.getCurrency());
        response.setBillingCycle(plan.getBillingCycle());
        response.setFeatures(plan.getFeatures());
        response.setIsActive(plan.getIsActive());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());

        return response;
    }

    public SubscriptionPlan toSubscriptionPlan(CreateSubscriptionPlanRequest request) {
        if (request == null) {
            return null;
        }

        SubscriptionPlan plan = new SubscriptionPlan();
        plan.setPlanCode(request.getPlanCode());
        plan.setName(request.getName());
        plan.setDescription(request.getDescription());
        plan.setPriceAmount(request.getPriceAmount());
        plan.setCurrency(request.getCurrency());
        plan.setBillingCycle(request.getBillingCycle());
        plan.setFeatures(request.getFeatures());
        plan.setIsActive(request.getIsActive());

        return plan;
    }
}
