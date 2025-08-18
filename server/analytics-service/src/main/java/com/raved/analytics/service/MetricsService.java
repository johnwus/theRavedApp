package com.raved.analytics.service;

import com.raved.analytics.dto.response.ContentMetricsResponse;
import com.raved.analytics.dto.response.UserMetricsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * MetricsService for TheRavedApp
 */
public interface MetricsService {

    /**
     * Get user metrics
     */
    UserMetricsResponse getUserMetrics(String userId);

    /**
     * Get content metrics
     */
    ContentMetricsResponse getContentMetrics(String contentId);

    /**
     * Get top users by engagement
     */
    Page<UserMetricsResponse> getTopUsersByEngagement(Pageable pageable);

    /**
     * Get top content by engagement
     */
    Page<ContentMetricsResponse> getTopContentByEngagement(Pageable pageable);

    /**
     * Get platform metrics
     */
    Map<String, Object> getPlatformMetrics();

    /**
     * Get user engagement trends
     */
    Map<String, Object> getUserEngagementTrends(String userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get content performance metrics
     */
    Map<String, Object> getContentPerformanceMetrics(String contentId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Recalculate user metrics
     */
    void recalculateUserMetrics(String userId);

    /**
     * Recalculate content metrics
     */
    void recalculateContentMetrics(String contentId);

    /**
     * Refresh platform metrics
     */
    void refreshPlatformMetrics();
}
