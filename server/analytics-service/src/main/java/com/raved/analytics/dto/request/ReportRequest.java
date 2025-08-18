package com.raved.analytics.dto.request;

import java.time.LocalDateTime;
import java.util.List;

import com.raved.analytics.model.EventType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for generating analytics reports
 *
 * Comprehensive report generation with support for ecommerce, subscription,
 * social, and performance analytics across different time periods and
 * dimensions.
 */
public class ReportRequest {

    @NotNull(message = "Report type is required")
    private ReportType reportType;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @Size(max = 255, message = "User ID must not exceed 255 characters")
    private String userId; // Optional for user-specific reports

    @Size(max = 100, message = "Entity type must not exceed 100 characters")
    private String entityType; // Optional for entity-specific reports

    @Size(max = 255, message = "Entity ID must not exceed 255 characters")
    private String entityId; // Optional for entity-specific reports

    private List<EventType> eventTypes; // Optional for filtering by specific event types

    @Size(max = 100, message = "Content type must not exceed 100 characters")
    private String contentType; // Optional for content-specific reports

    @Size(max = 100, message = "Content category must not exceed 100 characters")
    private String contentCategory; // Optional for category-specific reports

    @Size(max = 100, message = "Platform must not exceed 100 characters")
    private String platform; // Optional for platform-specific reports

    @Size(max = 100, message = "Device type must not exceed 100 characters")
    private String deviceType; // Optional for device-specific reports

    @Size(max = 100, message = "User segment must not exceed 100 characters")
    private String userSegment; // Optional for segment-specific reports

    @Size(max = 100, message = "User tier must not exceed 100 characters")
    private String userTier; // Optional for tier-specific reports

    // E-commerce specific filters
    @Size(max = 100, message = "Product category must not exceed 100 characters")
    private String productCategory;

    @Size(max = 100, message = "Product brand must not exceed 100 characters")
    private String productBrand;

    @Size(max = 100, message = "Payment method must not exceed 100 characters")
    private String paymentMethod;

    // Subscription specific filters
    @Size(max = 100, message = "Plan type must not exceed 100 characters")
    private String planType;

    @Size(max = 100, message = "Subscription status must not exceed 100 characters")
    private String subscriptionStatus;

    // Social specific filters
    @Size(max = 100, message = "Post type must not exceed 100 characters")
    private String postType;

    @Size(max = 100, message = "Interaction type must not exceed 100 characters")
    private String interactionType;

    // Geographic filters
    @Size(max = 100, message = "Country must not exceed 100 characters")
    private String country;

    @Size(max = 100, message = "Region must not exceed 100 characters")
    private String region;

    @Size(max = 100, message = "City must not exceed 100 characters")
    private String city;

    // Time granularity
    private TimeGranularity timeGranularity = TimeGranularity.DAY;

    // Grouping dimensions
    private List<String> groupBy; // e.g., ["userId", "contentType", "platform"]

    // Sorting
    private String sortBy = "eventTimestamp";
    private SortDirection sortDirection = SortDirection.DESC;

    // Pagination
    private Integer page = 0;
    private Integer size = 100;

    // Metrics to include
    private List<String> metrics; // e.g., ["views", "likes", "shares", "conversions"]

    // Filters
    private List<FilterCriteria> filters;

    // Report format
    private ReportFormat reportFormat = ReportFormat.JSON;

    // Constructors
    public ReportRequest() {
    }

    // Enums
    public enum ReportType {
        // General Analytics
        EVENT_SUMMARY, USER_ACTIVITY, CONTENT_PERFORMANCE, PLATFORM_USAGE,

        // E-commerce Analytics
        PRODUCT_PERFORMANCE, SALES_ANALYTICS, CART_ANALYTICS, CHECKOUT_ANALYTICS,
        REVENUE_ANALYTICS, CONVERSION_ANALYTICS, INVENTORY_ANALYTICS,

        // Subscription Analytics
        SUBSCRIPTION_PERFORMANCE, PLAN_ANALYTICS, CHURN_ANALYTICS, RETENTION_ANALYTICS,
        BILLING_ANALYTICS, UPGRADE_DOWNGRADE_ANALYTICS,

        // Social Analytics
        SOCIAL_ENGAGEMENT, POST_PERFORMANCE, INFLUENCER_ANALYTICS, COMMUNITY_ANALYTICS,
        HASHTAG_ANALYTICS, MENTION_ANALYTICS,

        // Content Analytics
        CONTENT_ENGAGEMENT, CONTENT_QUALITY, CONTENT_DISTRIBUTION, CONTENT_OPTIMIZATION,

        // Performance Analytics
        PERFORMANCE_METRICS, ERROR_ANALYTICS, API_PERFORMANCE, CACHE_PERFORMANCE,

        // User Analytics
        USER_SEGMENTATION, USER_BEHAVIOR, USER_JOURNEY, USER_RETENTION,

        // Custom Analytics
        CUSTOM_METRICS, FUNNEL_ANALYTICS, A_B_TEST_ANALYTICS, COHORT_ANALYTICS
    }

    public enum TimeGranularity {
        HOUR, DAY, WEEK, MONTH, QUARTER, YEAR
    }

    public enum SortDirection {
        ASC, DESC
    }

    public enum ReportFormat {
        JSON, CSV, EXCEL, PDF, HTML
    }

    // Getters and Setters
    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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

    public List<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(List<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(String contentCategory) {
        this.contentCategory = contentCategory;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getUserSegment() {
        return userSegment;
    }

    public void setUserSegment(String userSegment) {
        this.userSegment = userSegment;
    }

    public String getUserTier() {
        return userTier;
    }

    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getInteractionType() {
        return interactionType;
    }

    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public TimeGranularity getTimeGranularity() {
        return timeGranularity;
    }

    public void setTimeGranularity(TimeGranularity timeGranularity) {
        this.timeGranularity = timeGranularity;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortDirection getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<String> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<String> metrics) {
        this.metrics = metrics;
    }

    public List<FilterCriteria> getFilters() {
        return filters;
    }

    public void setFilters(List<FilterCriteria> filters) {
        this.filters = filters;
    }

    public ReportFormat getReportFormat() {
        return reportFormat;
    }

    public void setReportFormat(ReportFormat reportFormat) {
        this.reportFormat = reportFormat;
    }

    // Inner Classes
    public static class FilterCriteria {
        private String field;
        private String operator; // eq, ne, gt, gte, lt, lte, in, nin, regex
        private Object value;

        // Getters and setters
        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getOperator() {
            return operator;
        }

        public void setOperator(String operator) {
            this.operator = operator;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }
}
