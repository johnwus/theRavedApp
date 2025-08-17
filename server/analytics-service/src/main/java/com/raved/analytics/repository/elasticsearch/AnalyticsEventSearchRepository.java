package com.raved.analytics.repository.elasticsearch;

import com.raved.analytics.model.elasticsearch.AnalyticsEventDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Elasticsearch repository for Analytics Event search documents
 * Provides methods for real-time analytics and event search
 */
@Repository
public interface AnalyticsEventSearchRepository extends ElasticsearchRepository<AnalyticsEventDocument, String> {

    /**
     * Find events by user ID
     */
    List<AnalyticsEventDocument> findByUserId(String userId);

    /**
     * Find events by event type
     */
    List<AnalyticsEventDocument> findByEventType(String eventType);

    /**
     * Find events by entity type and ID
     */
    List<AnalyticsEventDocument> findByEntityTypeAndEntityId(String entityType, String entityId);

    /**
     * Find events by user ID and event type
     */
    List<AnalyticsEventDocument> findByUserIdAndEventType(String userId, String eventType);

    /**
     * Find events within a time range
     */
    List<AnalyticsEventDocument> findByEventTimestampBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Find events by user ID within a time range
     */
    List<AnalyticsEventDocument> findByUserIdAndEventTimestampBetween(String userId, LocalDateTime start, LocalDateTime end);

    /**
     * Find events by event type within a time range
     */
    List<AnalyticsEventDocument> findByEventTypeAndEventTimestampBetween(String eventType, LocalDateTime start, LocalDateTime end);

    /**
     * Find events by entity within a time range
     */
    List<AnalyticsEventDocument> findByEntityTypeAndEntityIdAndEventTimestampBetween(
            String entityType, String entityId, LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for event aggregation by type
     */
    @Query("{\"aggs\": {\"event_types\": {\"terms\": {\"field\": \"eventType\", \"size\": 100}}}}")
    List<AnalyticsEventDocument> aggregateByEventType();

    /**
     * Custom query for hourly event counts
     */
    @Query("{\"aggs\": {\"hourly_events\": {\"date_histogram\": {\"field\": \"eventTimestamp\", \"calendar_interval\": \"hour\"}}}}")
    List<AnalyticsEventDocument> aggregateByHour();

    /**
     * Custom query for user activity analysis
     */
    @Query("{\"bool\": {\"must\": [{\"term\": {\"userId\": \"?0\"}}, {\"range\": {\"eventTimestamp\": {\"gte\": \"?1\", \"lte\": \"?2\"}}}]}}")
    List<AnalyticsEventDocument> findUserActivityInRange(String userId, LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for popular content analysis
     */
    @Query("{\"bool\": {\"must\": [{\"terms\": {\"eventType\": [\"POST_VIEWED\", \"POST_LIKED\", \"POST_SHARED\"]}}, {\"range\": {\"eventTimestamp\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}]}}")
    List<AnalyticsEventDocument> findContentEngagementEvents(LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for real-time trending analysis
     */
    @Query("{\"bool\": {\"must\": [{\"range\": {\"eventTimestamp\": {\"gte\": \"now-1h\"}}}, {\"terms\": {\"eventType\": [\"POST_VIEWED\", \"POST_LIKED\", \"POST_SHARED\", \"COMMENT_CREATED\"]}}]}}")
    List<AnalyticsEventDocument> findRecentEngagementEvents();

    /**
     * Custom query for session analysis
     */
    @Query("{\"bool\": {\"must\": [{\"term\": {\"sessionId\": \"?0\"}}, {\"range\": {\"eventTimestamp\": {\"gte\": \"?1\", \"lte\": \"?2\"}}}]}}")
    List<AnalyticsEventDocument> findSessionEvents(String sessionId, LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for geographic analysis (by IP)
     */
    @Query("{\"bool\": {\"must\": [{\"exists\": {\"field\": \"ipAddress\"}}, {\"range\": {\"eventTimestamp\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}]}}")
    List<AnalyticsEventDocument> findEventsWithLocation(LocalDateTime start, LocalDateTime end);

    /**
     * Custom query for device/platform analysis
     */
    @Query("{\"bool\": {\"must\": [{\"exists\": {\"field\": \"userAgent\"}}, {\"range\": {\"eventTimestamp\": {\"gte\": \"?0\", \"lte\": \"?1\"}}}]}}")
    List<AnalyticsEventDocument> findEventsWithUserAgent(LocalDateTime start, LocalDateTime end);

    /**
     * Index management methods for cleanup
     */
    long deleteByEventTimestampBefore(LocalDateTime cutoffDate);
}
