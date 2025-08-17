package com.raved.analytics.mapper;

import com.raved.analytics.dto.response.AnalyticsAggregationResponse;
import com.raved.analytics.model.mongo.AnalyticsAggregation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

/**
 * Mapper for AnalyticsAggregation entities and DTOs
 *
 * Provides comprehensive mapping between AnalyticsAggregation models and response DTOs
 * with support for ecommerce, subscription, and social analytics aggregations.
 */
@Mapper(componentModel = "spring")
public interface AnalyticsAggregationMapper {

    // Entity to Response DTO mapping
    @Mapping(target = "ecommerceAggregations", source = "ecommerceAggregations", qualifiedByName = "mapEcommerceAggregations")
    @Mapping(target = "subscriptionAggregations", source = "subscriptionAggregations", qualifiedByName = "mapSubscriptionAggregations")
    @Mapping(target = "socialAggregations", source = "socialAggregations", qualifiedByName = "mapSocialAggregations")
    AnalyticsAggregationResponse toResponse(AnalyticsAggregation entity);

    // List mappings
    List<AnalyticsAggregationResponse> toResponseList(List<AnalyticsAggregation> entities);

    // Custom mapping methods for complex nested objects
    @Named("mapEcommerceAggregations")
    default AnalyticsAggregationResponse.EcommerceAggregationsResponse mapEcommerceAggregations(
            AnalyticsAggregation.EcommerceAggregations aggregations) {
        if (aggregations == null) {
            return null;
        }
        AnalyticsAggregationResponse.EcommerceAggregationsResponse response =
            new AnalyticsAggregationResponse.EcommerceAggregationsResponse();
        response.setTotalSales(aggregations.getTotalSales());
        response.setTotalOrders(aggregations.getTotalOrders());
        response.setAverageOrderValue(aggregations.getAverageOrderValue());
        response.setTotalProducts(aggregations.getTotalProducts());
        response.setConversionRate(aggregations.getConversionRate());
        response.setSalesByCategory(aggregations.getSalesByCategory());
        response.setOrdersByStatus(aggregations.getOrdersByStatus());
        return response;
    }

    @Named("mapSubscriptionAggregations")
    default AnalyticsAggregationResponse.SubscriptionAggregationsResponse mapSubscriptionAggregations(
            AnalyticsAggregation.SubscriptionAggregations aggregations) {
        if (aggregations == null) {
            return null;
        }
        AnalyticsAggregationResponse.SubscriptionAggregationsResponse response =
            new AnalyticsAggregationResponse.SubscriptionAggregationsResponse();
        response.setTotalRevenue(aggregations.getTotalRevenue());
        response.setTotalSubscriptions(aggregations.getTotalSubscriptions());
        response.setActiveSubscriptions(aggregations.getActiveSubscriptions());
        response.setChurnRate(aggregations.getChurnRate());
        response.setRetentionRate(aggregations.getRetentionRate());
        response.setSubscriptionsByPlan(aggregations.getSubscriptionsByPlan());
        response.setRevenueByPlan(aggregations.getRevenueByPlan());
        return response;
    }

    @Named("mapSocialAggregations")
    default AnalyticsAggregationResponse.SocialAggregationsResponse mapSocialAggregations(
            AnalyticsAggregation.SocialAggregations aggregations) {
        if (aggregations == null) {
            return null;
        }
        AnalyticsAggregationResponse.SocialAggregationsResponse response =
            new AnalyticsAggregationResponse.SocialAggregationsResponse();
        response.setTotalPosts(aggregations.getTotalPosts());
        response.setTotalInteractions(aggregations.getTotalInteractions());
        response.setTotalUsers(aggregations.getTotalUsers());
        response.setEngagementRate(aggregations.getEngagementRate());
        response.setInteractionsByType(aggregations.getInteractionsByType());
        response.setContentByType(aggregations.getContentByType());
        return response;
    }
}