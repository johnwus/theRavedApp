package com.raved.analytics.service;

import com.raved.analytics.dto.response.ContentMetricsResponse;
import com.raved.analytics.dto.response.UserMetricsResponse;
import com.raved.analytics.model.PaymentStatus;
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
    UserMetricsResponse getUserMetrics(Long userId);

    /**
     * Get content metrics
     */
    ContentMetricsResponse getContentMetrics(Long contentId);

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
    Map<String, Object> getUserEngagementTrends(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get content performance metrics
     */
    Map<String, Object> getContentPerformanceMetrics(Long contentId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Recalculate user metrics
     */
    void recalculateUserMetrics(Long userId);

    /**
     * Recalculate content metrics
     */
    void recalculateContentMetrics(Long contentId);

    /**
     * Refresh platform metrics
     */
    void refreshPlatformMetrics();
}
