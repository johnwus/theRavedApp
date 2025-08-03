package com.raved.analytics.service;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.model.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * AnalyticsService for TheRavedApp
 */
public interface AnalyticsService {

    /**
     * Track an analytics event
     */
    AnalyticsEventResponse trackEvent(TrackEventRequest request);

    /**
     * Process Kafka event
     */
    void processKafkaEvent(String eventMessage);

    /**
     * Get events by user
     */
    Page<AnalyticsEventResponse> getEventsByUser(Long userId, Pageable pageable);

    /**
     * Get events by type
     */
    Page<AnalyticsEventResponse> getEventsByType(EventType eventType, Pageable pageable);

    /**
     * Get events in date range
     */
    Page<AnalyticsEventResponse> getEventsInDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    /**
     * Get event counts by type
     */
    Map<String, Long> getEventCountsByType(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get daily event counts
     */
    Map<String, Long> getDailyEventCounts(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Get total event count
     */
    long getTotalEventCount();

    /**
     * Get event count for user
     */
    long getEventCountForUser(Long userId);

    /**
     * Get event count for user by type
     */
    long getEventCountForUser(Long userId, EventType eventType);

    /**
     * Get user engagement metrics
     */
    Map<String, Object> getUserEngagementMetrics(Long userId);

    /**
     * Get content engagement metrics
     */
    Map<String, Object> getContentEngagementMetrics(Long contentId);

    /**
     * Update user metrics
     */
    void updateUserMetrics(Long userId);

    /**
     * Update content metrics
     */
    void updateContentMetrics(Long contentId);

    /**
     * Cleanup old events
     */
    void cleanupOldEvents(LocalDateTime cutoffDate);
}
