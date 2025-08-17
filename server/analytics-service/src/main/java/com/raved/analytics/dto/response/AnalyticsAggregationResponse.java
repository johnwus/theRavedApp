package com.raved.analytics.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for Analytics Aggregations
 *
 * Provides aggregated analytics data for faster reporting and insights
 * with support for ecommerce, subscription, and social analytics.
 */
public class AnalyticsAggregationResponse {

    private String id;
    private String aggregationType;
    private String periodType;
    private String periodValue;
    private String userId;
    private String entityType;
    private String entityId;

    // Aggregated Metrics
    private Long totalEvents;
    private Long uniqueUsers;
    private Long totalViews;
    private Long totalInteractions;
    private BigDecimal totalRevenue;
    private Long totalConversions;
    private BigDecimal conversionRate;
    private BigDecimal engagementRate;

    // Detailed Metrics by Category
    private Map<String, Object> metricsByCategory;

    // E-commerce Specific Aggregations
    private EcommerceAggregationsResponse ecommerceAggregations;

    // Subscription Specific Aggregations
    private SubscriptionAggregationsResponse subscriptionAggregations;

    // Social Specific Aggregations
    private SocialAggregationsResponse socialAggregations;

    // Timeline
    private LocalDateTime calculatedAt;
    private LocalDateTime createdAt;

    // Constructors
    public AnalyticsAggregationResponse() {
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(String aggregationType) {
        this.aggregationType = aggregationType;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public String getPeriodValue() {
        return periodValue;
    }

    public void setPeriodValue(String periodValue) {
        this.periodValue = periodValue;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Long getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(Long totalEvents) {
        this.totalEvents = totalEvents;
    }

    public Long getUniqueUsers() {
        return uniqueUsers;
    }

    public void setUniqueUsers(Long uniqueUsers) {
        this.uniqueUsers = uniqueUsers;
    }

    public Long getTotalViews() {
        return totalViews;
    }

    public void setTotalViews(Long totalViews) {
        this.totalViews = totalViews;
    }

    public Long getTotalInteractions() {
        return totalInteractions;
    }

    public void setTotalInteractions(Long totalInteractions) {
        this.totalInteractions = totalInteractions;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalConversions() {
        return totalConversions;
    }

    public void setTotalConversions(Long totalConversions) {
        this.totalConversions = totalConversions;
    }

    public BigDecimal getConversionRate() {
        return conversionRate;
    }

    public void setConversionRate(BigDecimal conversionRate) {
        this.conversionRate = conversionRate;
    }

    public BigDecimal getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(BigDecimal engagementRate) {
        this.engagementRate = engagementRate;
    }

    public Map<String, Object> getMetricsByCategory() {
        return metricsByCategory;
    }

    public void setMetricsByCategory(Map<String, Object> metricsByCategory) {
        this.metricsByCategory = metricsByCategory;
    }

    public EcommerceAggregationsResponse getEcommerceAggregations() {
        return ecommerceAggregations;
    }

    public void setEcommerceAggregations(EcommerceAggregationsResponse ecommerceAggregations) {
        this.ecommerceAggregations = ecommerceAggregations;
    }

    public SubscriptionAggregationsResponse getSubscriptionAggregations() {
        return subscriptionAggregations;
    }

    public void setSubscriptionAggregations(SubscriptionAggregationsResponse subscriptionAggregations) {
        this.subscriptionAggregations = subscriptionAggregations;
    }

    public SocialAggregationsResponse getSocialAggregations() {
        return socialAggregations;
    }

    public void setSocialAggregations(SocialAggregationsResponse socialAggregations) {
        this.socialAggregations = socialAggregations;
    }

    public LocalDateTime getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(LocalDateTime calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Inner Classes for Specific Aggregations
    public static class EcommerceAggregationsResponse {
        private BigDecimal totalSales;
        private Long totalOrders;
        private BigDecimal averageOrderValue;
        private Long totalProducts;
        private BigDecimal conversionRate;
        private Map<String, BigDecimal> salesByCategory;
        private Map<String, Long> ordersByStatus;

        // Getters and setters
        public BigDecimal getTotalSales() {
            return totalSales;
        }

        public void setTotalSales(BigDecimal totalSales) {
            this.totalSales = totalSales;
        }

        public Long getTotalOrders() {
            return totalOrders;
        }

        public void setTotalOrders(Long totalOrders) {
            this.totalOrders = totalOrders;
        }

        public BigDecimal getAverageOrderValue() {
            return averageOrderValue;
        }

        public void setAverageOrderValue(BigDecimal averageOrderValue) {
            this.averageOrderValue = averageOrderValue;
        }

        public Long getTotalProducts() {
            return totalProducts;
        }

        public void setTotalProducts(Long totalProducts) {
            this.totalProducts = totalProducts;
        }

        public BigDecimal getConversionRate() {
            return conversionRate;
        }

        public void setConversionRate(BigDecimal conversionRate) {
            this.conversionRate = conversionRate;
        }

        public Map<String, BigDecimal> getSalesByCategory() {
            return salesByCategory;
        }

        public void setSalesByCategory(Map<String, BigDecimal> salesByCategory) {
            this.salesByCategory = salesByCategory;
        }

        public Map<String, Long> getOrdersByStatus() {
            return ordersByStatus;
        }

        public void setOrdersByStatus(Map<String, Long> ordersByStatus) {
            this.ordersByStatus = ordersByStatus;
        }
    }

    public static class SubscriptionAggregationsResponse {
        private BigDecimal totalRevenue;
        private Long totalSubscriptions;
        private Long activeSubscriptions;
        private BigDecimal churnRate;
        private BigDecimal retentionRate;
        private Map<String, Long> subscriptionsByPlan;
        private Map<String, BigDecimal> revenueByPlan;

        // Getters and setters
        public BigDecimal getTotalRevenue() {
            return totalRevenue;
        }

        public void setTotalRevenue(BigDecimal totalRevenue) {
            this.totalRevenue = totalRevenue;
        }

        public Long getTotalSubscriptions() {
            return totalSubscriptions;
        }

        public void setTotalSubscriptions(Long totalSubscriptions) {
            this.totalSubscriptions = totalSubscriptions;
        }

        public Long getActiveSubscriptions() {
            return activeSubscriptions;
        }

        public void setActiveSubscriptions(Long activeSubscriptions) {
            this.activeSubscriptions = activeSubscriptions;
        }

        public BigDecimal getChurnRate() {
            return churnRate;
        }

        public void setChurnRate(BigDecimal churnRate) {
            this.churnRate = churnRate;
        }

        public BigDecimal getRetentionRate() {
            return retentionRate;
        }

        public void setRetentionRate(BigDecimal retentionRate) {
            this.retentionRate = retentionRate;
        }

        public Map<String, Long> getSubscriptionsByPlan() {
            return subscriptionsByPlan;
        }

        public void setSubscriptionsByPlan(Map<String, Long> subscriptionsByPlan) {
            this.subscriptionsByPlan = subscriptionsByPlan;
        }

        public Map<String, BigDecimal> getRevenueByPlan() {
            return revenueByPlan;
        }

        public void setRevenueByPlan(Map<String, BigDecimal> revenueByPlan) {
            this.revenueByPlan = revenueByPlan;
        }
    }

    public static class SocialAggregationsResponse {
        private Long totalPosts;
        private Long totalInteractions;
        private Long totalUsers;
        private BigDecimal engagementRate;
        private Map<String, Long> interactionsByType;
        private Map<String, Long> contentByType;

        // Getters and setters
        public Long getTotalPosts() {
            return totalPosts;
        }

        public void setTotalPosts(Long totalPosts) {
            this.totalPosts = totalPosts;
        }

        public Long getTotalInteractions() {
            return totalInteractions;
        }

        public void setTotalInteractions(Long totalInteractions) {
            this.totalInteractions = totalInteractions;
        }

        public Long getTotalUsers() {
            return totalUsers;
        }

        public void setTotalUsers(Long totalUsers) {
            this.totalUsers = totalUsers;
        }

        public BigDecimal getEngagementRate() {
            return engagementRate;
        }

        public void setEngagementRate(BigDecimal engagementRate) {
            this.engagementRate = engagementRate;
        }

        public Map<String, Long> getInteractionsByType() {
            return interactionsByType;
        }

        public void setInteractionsByType(Map<String, Long> interactionsByType) {
            this.interactionsByType = interactionsByType;
        }

        public Map<String, Long> getContentByType() {
            return contentByType;
        }

        public void setContentByType(Map<String, Long> contentByType) {
            this.contentByType = contentByType;
        }
    }
}