package com.raved.analytics.service;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.dto.response.AnalyticsSearchHitResponse;
import com.raved.analytics.model.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * AnalyticsService for TheRavedApp
 *
 * Comprehensive analytics service providing event tracking, metrics
 * calculation, and insights for ecommerce, subscriptions, social content, and
 * user behavior.
 */
public interface AnalyticsService {

    // Event Tracking
    /**
     * Track an analytics event
     */
    AnalyticsEventResponse trackEvent(TrackEventRequest request);

    /**
     * Track multiple analytics events in batch
     */
    List<AnalyticsEventResponse> trackEvents(List<TrackEventRequest> requests);

    /**
     * Process Kafka event
     */
    void processKafkaEvent(String eventMessage);

    // Event Retrieval
    /**
     * Get events by user
     */
    Page<AnalyticsEventResponse> getEventsByUser(String userId, Pageable pageable);

    /**
     * Get events by type
     */
    Page<AnalyticsEventResponse> getEventsByType(EventType eventType, Pageable pageable);

    /**
     * Get events in date range
     */
    Page<AnalyticsEventResponse> getEventsInDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Get events by entity
     */
    Page<AnalyticsEventResponse> getEventsByEntity(String entityType, String entityId, Pageable pageable);

    /**
     * Get events by session
     */
    List<AnalyticsEventResponse> getEventsBySession(String sessionId);

    // Event Analytics
    /**
     * Get event counts by type
     */
    Map<String, Long> getEventCountsByType(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get daily event counts
     */
    Map<String, Long> getDailyEventCounts(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get hourly event counts
     */
    Map<String, Long> getHourlyEventCounts(String date);

    /**
     * Get weekly event counts
     */
    Map<String, Long> getWeeklyEventCounts(int year, int week);

    /**
     * Get monthly event counts
     */
    Map<String, Long> getMonthlyEventCounts(int year, int month);

    /**
     * Get total event count
     */
    long getTotalEventCount();

    /**
     * Get event count for user
     */
    long getEventCountForUser(String userId);

    /**
     * Get event count for user by type
     */
    long getEventCountForUser(String userId, EventType eventType);

    // User Analytics
    /**
     * Get user engagement metrics
     */
    Map<String, Object> getUserEngagementMetrics(String userId);

    /**
     * Get user behavior patterns
     */
    Map<String, Object> getUserBehaviorPatterns(String userId);

    /**
     * Get user journey analysis
     */
    Map<String, Object> getUserJourneyAnalysis(String userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get user segmentation data
     */
    Map<String, Object> getUserSegmentationData();

    // Content Analytics
    /**
     * Get content engagement metrics
     */
    Map<String, Object> getContentEngagementMetrics(String contentId);

    /**
     * Get content performance analysis
     */
    Map<String, Object> getContentPerformanceAnalysis(String contentId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get content quality metrics
     */
    Map<String, Object> getContentQualityMetrics(String contentId);

    /**
     * Get trending content
     */
    List<Map<String, Object>> getTrendingContent(String contentType, int limit);

    // E-commerce Analytics
    /**
     * Get product performance metrics
     */
    Map<String, Object> getProductPerformanceMetrics(String productId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get sales analytics
     */
    Map<String, Object> getSalesAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get conversion analytics
     */
    Map<String, Object> getConversionAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get cart abandonment analysis
     */
    Map<String, Object> getCartAbandonmentAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get revenue analytics
     */
    Map<String, Object> getRevenueAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get product category performance
     */
    Map<String, Object> getProductCategoryPerformance(String category, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get checkout funnel analysis
     */
    Map<String, Object> getCheckoutFunnelAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    // Subscription Analytics
    /**
     * Get subscription performance metrics
     */
    Map<String, Object> getSubscriptionPerformanceMetrics(String planId, LocalDateTime startDate,
            LocalDateTime endDate);

    /**
     * Get subscription analytics
     */
    Map<String, Object> getSubscriptionAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get churn analysis
     */
    Map<String, Object> getChurnAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get retention analytics
     */
    Map<String, Object> getRetentionAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get subscription funnel analysis
     */
    Map<String, Object> getSubscriptionFunnelAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get plan performance comparison
     */
    Map<String, Object> getPlanPerformanceComparison(LocalDateTime startDate, LocalDateTime endDate);

    // Social Analytics
    /**
     * Get social engagement metrics
     */
    Map<String, Object> getSocialEngagementMetrics(String postId);

    /**
     * Get influencer analytics
     */
    Map<String, Object> getInfluencerAnalytics(String userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get community analytics
     */
    Map<String, Object> getCommunityAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get hashtag performance
     */
    Map<String, Object> getHashtagPerformance(String hashtag, LocalDateTime startDate, LocalDateTime endDate);

    // Performance Analytics
    /**
     * Get performance metrics
     */
    Map<String, Object> getPerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get error analytics
     */
    Map<String, Object> getErrorAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get API performance metrics
     */
    Map<String, Object> getApiPerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate);

    // Geographic Analytics
    /**
     * Get geographic analytics
     */
    Map<String, Object> getGeographicAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get location-based insights
     */
    Map<String, Object> getLocationBasedInsights(String country, String region, LocalDateTime startDate,
            LocalDateTime endDate);

    // Device and Platform Analytics
    /**
     * Get device analytics
     */
    Map<String, Object> getDeviceAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get platform analytics
     */
    Map<String, Object> getPlatformAnalytics(LocalDateTime startDate, LocalDateTime endDate);

    // Advanced Analytics
    /**
     * Get cohort analysis
     */
    Map<String, Object> getCohortAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get funnel analysis
     */
    Map<String, Object> getFunnelAnalysis(String funnelName, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get A/B test analytics
     */
    Map<String, Object> getAbTestAnalytics(String testId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get predictive analytics
     */
    Map<String, Object> getPredictiveAnalytics(String metric, LocalDateTime startDate, LocalDateTime endDate);

    // Metrics Updates
    /**
     * Update user metrics
     */
    void updateUserMetrics(String userId);

    /**
     * Update content metrics
     */
    void updateContentMetrics(String contentId);

    /**
     * Update product metrics
     */
    void updateProductMetrics(String productId);

    /**
     * Update subscription metrics
     */
    void updateSubscriptionMetrics(String subscriptionId);

    // Data Management
    /**
     * Cleanup old events
     */
    void cleanupOldEvents(LocalDateTime cutoffDate);

    /**
     * Archive analytics data
     */
    void archiveAnalyticsData(LocalDateTime cutoffDate);

    /**
     * Export analytics data
     */
    byte[] exportAnalyticsData(LocalDateTime startDate, LocalDateTime endDate, String format);

    // Real-time Analytics
    /**
     * Get real-time metrics
     */
    Map<String, Object> getRealTimeMetrics();

    /**
     * Get real-time user activity
     */
    List<Map<String, Object>> getRealTimeUserActivity(int limit);

    /**
     * Get real-time content performance
     */
    List<Map<String, Object>> getRealTimeContentPerformance(int limit);

    // Search (Elasticsearch)
    Page<AnalyticsSearchHitResponse> searchByUser(String userId, Pageable pageable);

    Page<AnalyticsSearchHitResponse> searchByEventType(String eventType, Pageable pageable);

    Page<AnalyticsSearchHitResponse> searchByHashtag(String hashtag, Pageable pageable);

    Page<AnalyticsSearchHitResponse> searchByContentTag(String tag, Pageable pageable);
}
