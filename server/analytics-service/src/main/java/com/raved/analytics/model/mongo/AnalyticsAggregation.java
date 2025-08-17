package com.raved.analytics.model.mongo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * AnalyticsAggregation Document for TheRavedApp MongoDB
 *
 * Pre-computed aggregations for faster analytics queries and reporting.
 * Stores daily, weekly, monthly, and yearly aggregations.
 */
@Document(collection = "analytics_aggregations")
@CompoundIndex(name = "idx_aggregation_type_period", def = "{'aggregationType': 1, 'periodType': 1, 'periodValue': 1}")
@CompoundIndex(name = "idx_entity_aggregation", def = "{'entityType': 1, 'entityId': 1, 'aggregationType': 1}")
@CompoundIndex(name = "idx_user_aggregation", def = "{'userId': 1, 'aggregationType': 1, 'periodType': 1}")
public class AnalyticsAggregation {

    @Id
    private String id;

    @Indexed
    @Field("aggregation_type")
    private String aggregationType; // "user_metrics", "content_metrics", "ecommerce_metrics",
                                    // "subscription_metrics"

    @Indexed
    @Field("period_type")
    private String periodType; // "daily", "weekly", "monthly", "yearly"

    @Indexed
    @Field("period_value")
    private String periodValue; // "2024-01-15", "2024-W03", "2024-01", "2024"

    @Field("user_id")
    private String userId; // nullable for global aggregations

    @Field("entity_type")
    private String entityType; // "post", "product", "subscription_plan"

    @Field("entity_id")
    private String entityId; // nullable for type-level aggregations

    // Aggregated Metrics
    @Field("total_events")
    private Long totalEvents;

    @Field("unique_users")
    private Long uniqueUsers;

    @Field("total_views")
    private Long totalViews;

    @Field("total_interactions")
    private Long totalInteractions;

    @Field("total_revenue")
    private BigDecimal totalRevenue;

    @Field("total_conversions")
    private Long totalConversions;

    @Field("conversion_rate")
    private BigDecimal conversionRate;

    @Field("engagement_rate")
    private BigDecimal engagementRate;

    // Detailed Metrics by Category
    @Field("metrics_by_category")
    private Map<String, Object> metricsByCategory;

    // E-commerce Specific Aggregations
    @Field("ecommerce_aggregations")
    private EcommerceAggregations ecommerceAggregations;

    // Subscription Specific Aggregations
    @Field("subscription_aggregations")
    private SubscriptionAggregations subscriptionAggregations;

    // Social Specific Aggregations
    @Field("social_aggregations")
    private SocialAggregations socialAggregations;

    // Timeline
    @Field("calculated_at")
    private LocalDateTime calculatedAt;

    @Field("created_at")
    private LocalDateTime createdAt;

    // Constructors
    public AnalyticsAggregation() {
        this.createdAt = LocalDateTime.now();
        this.calculatedAt = LocalDateTime.now();
        this.totalEvents = 0L;
        this.uniqueUsers = 0L;
        this.totalViews = 0L;
        this.totalInteractions = 0L;
        this.totalRevenue = BigDecimal.ZERO;
        this.totalConversions = 0L;
        this.conversionRate = BigDecimal.ZERO;
        this.engagementRate = BigDecimal.ZERO;
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

    public EcommerceAggregations getEcommerceAggregations() {
        return ecommerceAggregations;
    }

    public void setEcommerceAggregations(EcommerceAggregations ecommerceAggregations) {
        this.ecommerceAggregations = ecommerceAggregations;
    }

    public SubscriptionAggregations getSubscriptionAggregations() {
        return subscriptionAggregations;
    }

    public void setSubscriptionAggregations(SubscriptionAggregations subscriptionAggregations) {
        this.subscriptionAggregations = subscriptionAggregations;
    }

    public SocialAggregations getSocialAggregations() {
        return socialAggregations;
    }

    public void setSocialAggregations(SocialAggregations socialAggregations) {
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
    public static class EcommerceAggregations {
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

    public static class SubscriptionAggregations {
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

    public static class SocialAggregations {
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