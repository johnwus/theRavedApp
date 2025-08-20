package com.raved.analytics.service.impl;

import com.raved.analytics.algorithm.EngagementCalculator;
import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.exception.AnalyticsProcessingException;
import com.raved.analytics.mapper.AnalyticsEventMapper;
import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.ContentMetrics;
import com.raved.analytics.model.EventType;
import com.raved.analytics.model.UserMetrics;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.UserMetricsRepository;
import com.raved.analytics.service.AnalyticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;

/**
 * Implementation of AnalyticsService
 */
@Service
@Transactional
public class AnalyticsServiceImpl implements AnalyticsService {

    private static final Logger logger = LoggerFactory.getLogger(AnalyticsServiceImpl.class);

    @Autowired
    private AnalyticsEventRepository eventRepository;

    @Autowired
    private com.raved.analytics.service.ElasticsearchSyncService elasticsearchSyncService;

    @Autowired
    private UserMetricsRepository userMetricsRepository;

    @Autowired
    private ContentMetricsRepository contentMetricsRepository;

    @Autowired
    private AnalyticsEventMapper eventMapper;

    @org.springframework.beans.factory.annotation.Autowired(required = false)
    private com.raved.analytics.search.AnalyticsEventSearchRepository searchRepository;

    @Autowired
    private EngagementCalculator engagementCalculator;

    @Override
    public AnalyticsEventResponse trackEvent(TrackEventRequest request) {
        logger.info("Tracking event: {} for user: {}", request.getEventType(), request.getUserId());

        try {
            AnalyticsEvent event = eventMapper.toEntity(request);
            // Respect provided timestamp (for idempotence tests); default if null
            if (request.getEventTimestamp() != null) {
                event.setEventTimestamp(request.getEventTimestamp());
            } else {
                event.setEventTimestamp(LocalDateTime.now());
            }
            event.setCreatedAt(LocalDateTime.now());
            // Compute idempotence key similar to user-service patterns (keyed by user/session + type + entity + second)
            event.setDedupKey(computeDedupKey(event));

            AnalyticsEvent savedEvent;
            try {
                savedEvent = eventRepository.save(event);
            } catch (org.springframework.dao.DuplicateKeyException dup) {
                logger.info("Duplicate analytics event detected (dedupKey={}), ignoring", event.getDedupKey());
                // Load existing to return a consistent response if needed
                // Best-effort: fall back to mapping current event
                return eventMapper.toResponse(event);
            }

            // Sync to Elasticsearch for real-time analytics (best-effort; non-fatal if ES is down)
            try {
                elasticsearchSyncService.syncAnalyticsEvent(savedEvent);
            } catch (Exception esEx) {
                logger.warn("Failed to sync event to Elasticsearch for {}: {}", savedEvent.getId(), esEx.getMessage());
            }

            // Process event for metrics updates
            processEventForMetrics(savedEvent);

            logger.info("Event tracked successfully: {}", savedEvent.getId());
            return eventMapper.toResponse(savedEvent);

        } catch (Exception e) {
            logger.error("Error tracking event", e);
            throw new AnalyticsProcessingException("Failed to track event: " + e.getMessage());
        }
    }

    private String computeDedupKey(AnalyticsEvent e) {
        // Align with user-service style: prefer userId as key basis (fallback to sessionId),
        // include eventType, entityId, and epochMillis when available
        String principal = (e.getUserId() != null && !e.getUserId().isBlank())
                ? e.getUserId()
                : (e.getSessionId() != null ? e.getSessionId() : "");
        String type = e.getEventType() != null ? e.getEventType().name() : "";
        String entity = e.getEntityId() != null ? e.getEntityId() : "";
        long epochMillis = 0L;
        if (e.getEventTimestamp() != null) {
            epochMillis = e.getEventTimestamp().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        }
        String base = String.join("|", principal, type, entity, Long.toString(epochMillis));
        return Integer.toHexString(base.hashCode());
    }

    @Override
    public List<AnalyticsEventResponse> trackEvents(List<TrackEventRequest> requests) {
        logger.info("Tracking {} events", requests.size());

        List<AnalyticsEventResponse> responses = new ArrayList<>();
        for (TrackEventRequest request : requests) {
            try {
                responses.add(trackEvent(request));
            } catch (Exception e) {
                logger.error("Error tracking event in batch: {}", e.getMessage());
                // Continue with other events
            }
        }

        return responses;
    }

    @Override
    @KafkaListener(topics = {"user-events", "social-events", "ecommerce-events", "content-events"})
    public void processKafkaEvent(String eventMessage) {
        logger.debug("Processing Kafka event: {}", eventMessage);

        try {
            // Parse and process the event
            // This would typically involve JSON parsing and event processing
            Map<String, Object> eventData = parseEventMessage(eventMessage);

            if (eventData != null) {
                TrackEventRequest request = createTrackEventRequest(eventData);
                trackEvent(request);
            }

        } catch (Exception e) {
            logger.error("Error processing Kafka event: {}", eventMessage, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "user-analytics", key = "#userId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<AnalyticsEventResponse> getEventsByUser(String userId, Pageable pageable) {
        logger.debug("Getting events for user: {}", userId);

        List<AnalyticsEvent> eventsList = eventRepository.findByUserIdAndEventTimestampBetween(userId,
                LocalDateTime.now().minusYears(1), LocalDateTime.now());

        // Convert to responses manually
        List<AnalyticsEventResponse> responses = eventsList.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        // Create a page manually (simplified)
        return new PageImpl<>(responses, pageable, responses.size());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnalyticsEventResponse> getEventsByType(EventType eventType, Pageable pageable) {
        logger.debug("Getting events by type: {}", eventType);

        Page<AnalyticsEvent> events = eventRepository.findByEventType(eventType, pageable);
        return events.map(eventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnalyticsEventResponse> getEventsInDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        logger.debug("Getting events between {} and {}", startDate, endDate);

        Page<AnalyticsEvent> events = eventRepository.findByEventTimestampBetween(
                startDate, endDate, pageable);
        return events.map(eventMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getEventCountsByType(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting event counts by type between {} and {}", startDate, endDate);

        Map<String, Long> eventCounts = new HashMap<>();

        // Get counts for each event type
        for (EventType eventType : EventType.values()) {
            long count = eventRepository.countByEventTypeAndEventTimestampBetween(eventType, startDate, endDate);
            if (count > 0) {
                eventCounts.put(eventType.name(), count);
            }
        }

        return eventCounts;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getDailyEventCounts(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting daily event counts between {} and {}", startDate, endDate);

        Map<String, Long> dailyCounts = new HashMap<>();

        // Get events in the date range and group by date
        List<AnalyticsEvent> events = eventRepository.findByEventTimestampBetween(startDate, endDate);

        for (AnalyticsEvent event : events) {
            String date = event.getEventTimestamp().toLocalDate().toString();
            dailyCounts.merge(date, 1L, Long::sum);
        }

        return dailyCounts;
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventCountForUser(String userId) {
        return eventRepository.countByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventCountForUser(String userId, EventType eventType) {
        return eventRepository.countByUserIdAndEventType(userId, eventType);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserEngagementMetrics(String userId) {
        logger.debug("Getting engagement metrics for user: {}", userId);

        Optional<UserMetrics> userMetricsOpt = userMetricsRepository.findByUserId(userId);
        if (userMetricsOpt.isEmpty()) {
            return new HashMap<>();
        }

        UserMetrics metrics = userMetricsOpt.get();
        Map<String, Object> engagementMetrics = new HashMap<>();

        // Get social metrics
        UserMetrics.SocialMetrics socialMetrics = metrics.getSocialMetrics();
        if (socialMetrics != null) {
            engagementMetrics.put("totalPosts", socialMetrics.getPostsCount());
            engagementMetrics.put("totalLikes", socialMetrics.getLikesReceived());
            engagementMetrics.put("totalComments", socialMetrics.getCommentsReceived());
            engagementMetrics.put("totalShares", socialMetrics.getSharesReceived());
            engagementMetrics.put("totalFollowers", socialMetrics.getFollowersCount());
            engagementMetrics.put("totalFollowing", socialMetrics.getFollowingCount());
        } else {
            engagementMetrics.put("totalPosts", 0);
            engagementMetrics.put("totalLikes", 0);
            engagementMetrics.put("totalComments", 0);
            engagementMetrics.put("totalShares", 0);
            engagementMetrics.put("totalFollowers", 0);
            engagementMetrics.put("totalFollowing", 0);
        }

        engagementMetrics.put("engagementScore", metrics.getEngagementScore());
        engagementMetrics.put("lastActiveAt", metrics.getLastActiveDate());

        return engagementMetrics;
    }

    @Override
    public List<Map<String, Object>> getRealTimeContentPerformance(int limit) {
        logger.debug("Getting real-time content performance with limit: {}", limit);

        try {
            // Get top performing content in real-time
            List<ContentMetrics> topContent = contentMetricsRepository
                    .findTopContentByEngagement(PageRequest.of(0, limit));

            return topContent.stream()
                    .map(metrics -> {
                        Map<String, Object> data = new HashMap<>();
                        data.put("contentId", metrics.getContentId());
                        data.put("engagementScore", metrics.getEngagementRate());
                        data.put("viewCount", metrics.getViewsCount());
                        data.put("likeCount", metrics.getLikesCount());
                        data.put("commentCount", metrics.getCommentsCount());
                        data.put("shareCount", metrics.getSharesCount());
                        return data;
                    })
                    .collect(Collectors.toList());

        } catch (Exception e) {
            logger.error("Error getting real-time content performance", e);
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getContentEngagementMetrics(String contentId) {
        logger.debug("Getting engagement metrics for content: {}", contentId);

        List<ContentMetrics> contentMetricsList = contentMetricsRepository.findByContentId(contentId);
        if (contentMetricsList.isEmpty()) {
            return new HashMap<>();
        }

        ContentMetrics metrics = contentMetricsList.get(0);
        Map<String, Object> engagementMetrics = new HashMap<>();

        engagementMetrics.put("viewCount", metrics.getViewCount());
        engagementMetrics.put("likeCount", metrics.getLikeCount());
        engagementMetrics.put("commentCount", metrics.getCommentCount());
        engagementMetrics.put("shareCount", metrics.getShareCount());
        engagementMetrics.put("engagementScore", metrics.getEngagementScore());
        engagementMetrics.put("reachCount", metrics.getReachCount());
        engagementMetrics.put("impressionCount", metrics.getImpressionCount());

        return engagementMetrics;
    }

    @Override
    public void updateSubscriptionMetrics(String subscriptionId) {
        logger.info("Updating subscription metrics for: {}", subscriptionId);
        // TODO: Implement subscription metrics update
    }

    @Override
    public void updateProductMetrics(String productId) {
        logger.info("Updating product metrics for: {}", productId);
        // TODO: Implement product metrics update
    }

    @Override
    public void updateUserMetrics(String userId) {
        logger.info("Updating metrics for user: {}", userId);

        try {
            Optional<UserMetrics> existingMetricsOpt = userMetricsRepository.findByUserId(userId);
            UserMetrics metrics = existingMetricsOpt.orElse(new UserMetrics());

            if (existingMetricsOpt.isEmpty()) {
                metrics.setUserId(userId);
                metrics.setCreatedAt(LocalDateTime.now());
            }

            // Calculate metrics from events
            long totalPosts = eventRepository.countByUserIdAndEventType(userId, EventType.POST_CREATE);
            long totalLikes = eventRepository.countByUserIdAndEventType(userId, EventType.POST_LIKE);
            long totalComments = eventRepository.countByUserIdAndEventType(userId, EventType.POST_COMMENT);
            long totalShares = eventRepository.countByUserIdAndEventType(userId, EventType.POST_SHARE);

            metrics.setTotalPosts(totalPosts);
            metrics.setTotalLikes(totalLikes);
            metrics.setTotalComments(totalComments);
            metrics.setTotalShares(totalShares);
            metrics.setEngagementRate(engagementCalculator.calculateUserEngagementRate(userId));
            metrics.setLastActiveAt(LocalDateTime.now());
            metrics.setUpdatedAt(LocalDateTime.now());

            userMetricsRepository.save(metrics);
            logger.info("User metrics updated for user: {}", userId);

        } catch (Exception e) {
            logger.error("Error updating user metrics for user: {}", userId, e);
        }
    }

    @Override
    public void updateContentMetrics(String contentId) {
        logger.info("Updating metrics for content: {}", contentId);

        try {
            List<ContentMetrics> existingMetricsList = contentMetricsRepository.findByContentId(contentId);
            ContentMetrics metrics = existingMetricsList.isEmpty() ? new ContentMetrics() : existingMetricsList.get(0);

            if (existingMetricsList.isEmpty()) {
                metrics.setContentId(contentId);
                metrics.setCreatedAt(LocalDateTime.now());
            }

            // Calculate metrics from events
            long viewCount = eventRepository.countByEntityIdAndEventType(contentId, EventType.POST_VIEW);
            long likeCount = eventRepository.countByEntityIdAndEventType(contentId, EventType.POST_LIKE);
            long commentCount = eventRepository.countByEntityIdAndEventType(contentId, EventType.POST_COMMENT);
            long shareCount = eventRepository.countByEntityIdAndEventType(contentId, EventType.POST_SHARE);

            metrics.setViewsCount((int) viewCount);
            metrics.setLikesCount((int) likeCount);
            metrics.setCommentsCount((int) commentCount);
            metrics.setSharesCount((int) shareCount);
            metrics.setEngagementRate(engagementCalculator.calculateContentEngagementScore(contentId));
            metrics.setUpdatedAt(LocalDateTime.now());

            contentMetricsRepository.save(metrics);
            logger.info("Content metrics updated for content: {}", contentId);

        } catch (Exception e) {
            logger.error("Error updating content metrics for content: {}", contentId, e);
        }
    }

    // Missing methods - stub implementations
    @Override
    public List<Map<String, Object>> getRealTimeUserActivity(int limit) {
        // TODO: Implement real-time user activity
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getRealTimeMetrics() {
        // TODO: Implement real-time metrics
        return new HashMap<>();
    }

    @Override
    public Page<AnalyticsEventResponse> getEventsByEntity(String entityType, String entityId, Pageable pageable) {
        List<AnalyticsEvent> events = eventRepository.findByEntityTypeAndEntityId(entityType, entityId);
        List<AnalyticsEventResponse> responses = events.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, responses.size());
    }

    @Override
    public List<AnalyticsEventResponse> getEventsBySession(String sessionId) {
        List<AnalyticsEvent> events = eventRepository.findBySessionId(sessionId);
        return events.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public Map<String, Long> getHourlyEventCounts(String date) {
        Map<String, Long> hourly = new HashMap<>();
        try {
            java.time.LocalDate ld = java.time.LocalDate.parse(date);
            LocalDateTime start = ld.atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            List<AnalyticsEvent> events = eventRepository.findByEventTimestampBetween(start, end);
            for (AnalyticsEvent e : events) {
                String hourKey = String.valueOf(e.getEventTimestamp().getHour());
                hourly.merge(hourKey, 1L, Long::sum);
            }
        } catch (Exception ex) {
            logger.error("Failed to compute hourly event counts for date {}", date, ex);
        }
        return hourly;
    }

    @Override
    public Map<String, Long> getWeeklyEventCounts(int year, int week) {
        Map<String, Long> daily = new HashMap<>();
        try {
            java.time.temporal.WeekFields wf = java.time.temporal.WeekFields.ISO;
            java.time.LocalDate startDate = java.time.LocalDate
                    .now()
                    .with(java.time.temporal.IsoFields.WEEK_BASED_YEAR, year)
                    .with(wf.weekOfWeekBasedYear(), week)
                    .with(java.time.DayOfWeek.MONDAY);
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = start.plusDays(7);
            List<AnalyticsEvent> events = eventRepository.findByEventTimestampBetween(start, end);
            for (AnalyticsEvent e : events) {
                String key = e.getEventTimestamp().toLocalDate().toString();
                daily.merge(key, 1L, Long::sum);
            }
        } catch (Exception ex) {
            logger.error("Failed to compute weekly event counts for {}-W{}", year, week, ex);
        }
        return daily;
    }

    @Override
    public Map<String, Long> getMonthlyEventCounts(int year, int month) {
        Map<String, Long> daily = new HashMap<>();
        try {
            java.time.LocalDate startDate = java.time.LocalDate.of(year, month, 1);
            LocalDateTime start = startDate.atStartOfDay();
            LocalDateTime end = start.plusMonths(1);
            List<AnalyticsEvent> events = eventRepository.findByEventTimestampBetween(start, end);
            for (AnalyticsEvent e : events) {
                String key = e.getEventTimestamp().toLocalDate().toString();
                daily.merge(key, 1L, Long::sum);
            }
        } catch (Exception ex) {
            logger.error("Failed to compute monthly event counts for {}-{}", year, month, ex);
        }
        return daily;
    }

    @Override
    public long getTotalEventCount() {
        return eventRepository.count();
    }

    @Override
    public Map<String, Object> getUserBehaviorPatterns(String userId) {
        throw new com.raved.analytics.exception.NotImplementedFeatureException("User behavior patterns not implemented yet");
    }

    @Override
    public Map<String, Object> getUserJourneyAnalysis(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        throw new com.raved.analytics.exception.NotImplementedFeatureException("User journey analysis not implemented yet");
    }

    @Override
    public Map<String, Object> getUserSegmentationData() {
        throw new com.raved.analytics.exception.NotImplementedFeatureException("User segmentation not implemented yet");
    }

    @Override
    public Map<String, Object> getContentPerformanceAnalysis(String contentId, LocalDateTime startDate, LocalDateTime endDate) {
        throw new com.raved.analytics.exception.NotImplementedFeatureException("Content performance analysis not implemented yet");
    }

    @Override
    public Map<String, Object> getContentQualityMetrics(String contentId) {
        // TODO: Implement quality metrics
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getTrendingContent(String contentType, int limit) {
        // TODO: Implement trending content
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getPredictiveAnalytics(String entityId, LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement predictive analytics
        logger.info("Getting predictive analytics for entity: {} from {} to {}", entityId, startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getAbTestAnalytics(String testId, LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement A/B test analytics
        logger.info("Getting A/B test analytics for test: {} from {} to {}", testId, startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getFunnelAnalysis(String funnelId, LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement funnel analysis
        logger.info("Getting funnel analysis for funnel: {} from {} to {}", funnelId, startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getCohortAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement cohort analysis
        logger.info("Getting cohort analysis from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getPlatformAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement platform analytics
        logger.info("Getting platform analytics from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getDeviceAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement device analytics
        logger.info("Getting device analytics from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getLocationBasedInsights(String country, String region, LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement location-based insights
        logger.info("Getting location-based insights for {}, {} from {} to {}", country, region, startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getGeographicAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement geographic analytics
        logger.info("Getting geographic analytics from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getApiPerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement API performance metrics
        logger.info("Getting API performance metrics from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getErrorAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement error analytics
        logger.info("Getting error analytics from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getPerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement performance metrics
        logger.info("Getting performance metrics from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getHashtagPerformance(String hashtag, LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement hashtag performance analytics
        logger.info("Getting hashtag performance for {} from {} to {}", hashtag, startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getCommunityAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement community analytics
        logger.info("Getting community analytics from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getInfluencerAnalytics(String influencerId, LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement influencer analytics
        logger.info("Getting influencer analytics for {} from {} to {}", influencerId, startDate, endDate);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getSocialEngagementMetrics(String platformId) {
        // TODO: Implement social engagement metrics
        logger.info("Getting social engagement metrics for platform: {}", platformId);
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getPlanPerformanceComparison(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement plan performance comparison
        logger.info("Getting plan performance comparison from {} to {}", startDate, endDate);
        return new HashMap<>();
    }

    // E-commerce Analytics Implementation
    @Override
    public Map<String, Object> getProductPerformanceMetrics(String productId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting product performance metrics for product: {} from {} to {}", productId, startDate, endDate);

        Map<String, Object> metrics = new HashMap<>();

        try {
            // Get product-related events by filtering from date range
            List<AnalyticsEvent> allEvents = eventRepository.findByEventTimestampBetween(startDate, endDate);

            long viewCount = allEvents.stream()
                    .filter(event -> productId.equals(event.getEntityId()) && event.getEventType() == EventType.PRODUCT_VIEW)
                    .count();
            long purchaseCount = allEvents.stream()
                    .filter(event -> productId.equals(event.getEntityId()) && event.getEventType() == EventType.PRODUCT_PURCHASE)
                    .count();
            long addToCartCount = allEvents.stream()
                    .filter(event -> productId.equals(event.getEntityId()) && event.getEventType() == EventType.CART_ADD)
                    .count();

            metrics.put("productId", productId);
            metrics.put("viewCount", viewCount);
            metrics.put("purchaseCount", purchaseCount);
            metrics.put("addToCartCount", addToCartCount);
            metrics.put("conversionRate", viewCount > 0 ? (double) purchaseCount / viewCount * 100 : 0.0);
            metrics.put("cartConversionRate", addToCartCount > 0 ? (double) purchaseCount / addToCartCount * 100 : 0.0);

        } catch (Exception e) {
            logger.error("Error getting product performance metrics for product: {}", productId, e);
        }

        return metrics;
    }

    @Override
    public Map<String, Object> getSalesAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting sales analytics from {} to {}", startDate, endDate);

        Map<String, Object> analytics = new HashMap<>();

        try {
            // Get purchase events
            long totalSales = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.PRODUCT_PURCHASE, startDate, endDate);

            // Get daily sales breakdown
            Map<String, Long> dailySales = getDailyEventCounts(startDate, endDate);

            analytics.put("totalSales", totalSales);
            analytics.put("dailySales", dailySales);
            analytics.put("averageDailySales", dailySales.values().stream().mapToLong(Long::longValue).average().orElse(0.0));

        } catch (Exception e) {
            logger.error("Error getting sales analytics", e);
        }

        return analytics;
    }

    @Override
    public Map<String, Object> getConversionAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting conversion analytics from {} to {}", startDate, endDate);

        Map<String, Object> analytics = new HashMap<>();

        try {
            long totalViews = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.PRODUCT_VIEW, startDate, endDate);
            long totalPurchases = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.PRODUCT_PURCHASE, startDate, endDate);
            long totalCartAdds = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.CART_ADD, startDate, endDate);

            double overallConversionRate = totalViews > 0 ? (double) totalPurchases / totalViews * 100 : 0.0;
            double cartConversionRate = totalCartAdds > 0 ? (double) totalPurchases / totalCartAdds * 100 : 0.0;

            analytics.put("totalViews", totalViews);
            analytics.put("totalPurchases", totalPurchases);
            analytics.put("totalCartAdds", totalCartAdds);
            analytics.put("overallConversionRate", overallConversionRate);
            analytics.put("cartConversionRate", cartConversionRate);

        } catch (Exception e) {
            logger.error("Error getting conversion analytics", e);
        }

        return analytics;
    }

    @Override
    public Map<String, Object> getCartAbandonmentAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting cart abandonment analysis from {} to {}", startDate, endDate);

        Map<String, Object> analysis = new HashMap<>();

        try {
            long totalCartAdds = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.CART_ADD, startDate, endDate);
            long totalPurchases = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.PRODUCT_PURCHASE, startDate, endDate);

            long abandonedCarts = totalCartAdds - totalPurchases;
            double abandonmentRate = totalCartAdds > 0 ? (double) abandonedCarts / totalCartAdds * 100 : 0.0;

            analysis.put("totalCartAdds", totalCartAdds);
            analysis.put("totalPurchases", totalPurchases);
            analysis.put("abandonedCarts", abandonedCarts);
            analysis.put("abandonmentRate", abandonmentRate);

        } catch (Exception e) {
            logger.error("Error getting cart abandonment analysis", e);
        }

        return analysis;
    }

    @Override
    public Map<String, Object> getRevenueAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting revenue analytics from {} to {}", startDate, endDate);

        Map<String, Object> analytics = new HashMap<>();

        try {
            // Get purchase events and calculate revenue
            List<AnalyticsEvent> purchaseEvents = eventRepository.findByEventTimestampBetween(startDate, endDate)
                    .stream()
                    .filter(event -> event.getEventType() == EventType.PRODUCT_PURCHASE)
                    .collect(Collectors.toList());

            double totalRevenue = purchaseEvents.stream()
                    .mapToDouble(event -> {
                        Map<String, Object> eventData = event.getEventData();
                        if (eventData != null && eventData.containsKey("amount")) {
                            return Double.parseDouble(eventData.get("amount").toString());
                        }
                        return 0.0;
                    })
                    .sum();

            analytics.put("totalRevenue", totalRevenue);
            analytics.put("totalTransactions", purchaseEvents.size());
            analytics.put("averageOrderValue", purchaseEvents.size() > 0 ? totalRevenue / purchaseEvents.size() : 0.0);

        } catch (Exception e) {
            logger.error("Error getting revenue analytics", e);
        }

        return analytics;
    }

    @Override
    public Map<String, Object> getProductCategoryPerformance(String category, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting product category performance for category: {} from {} to {}", category, startDate, endDate);

        Map<String, Object> performance = new HashMap<>();

        try {
            // Get category-related events (assuming category is stored in event data)
            List<AnalyticsEvent> categoryEvents = eventRepository.findByEventTimestampBetween(startDate, endDate)
                    .stream()
                    .filter(event -> {
                        Map<String, Object> eventData = event.getEventData();
                        return eventData != null && category.equals(eventData.get("category"));
                    })
                    .collect(Collectors.toList());

            long viewCount = categoryEvents.stream()
                    .filter(event -> event.getEventType() == EventType.PRODUCT_VIEW)
                    .count();
            long purchaseCount = categoryEvents.stream()
                    .filter(event -> event.getEventType() == EventType.PRODUCT_PURCHASE)
                    .count();

            performance.put("category", category);
            performance.put("viewCount", viewCount);
            performance.put("purchaseCount", purchaseCount);
            performance.put("conversionRate", viewCount > 0 ? (double) purchaseCount / viewCount * 100 : 0.0);

        } catch (Exception e) {
            logger.error("Error getting product category performance for category: {}", category, e);
        }

        return performance;
    }

    @Override
    public Map<String, Object> getCheckoutFunnelAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting checkout funnel analysis from {} to {}", startDate, endDate);

        Map<String, Object> analysis = new HashMap<>();

        try {
            long productViews = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.PRODUCT_VIEW, startDate, endDate);
            long cartAdds = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.CART_ADD, startDate, endDate);
            long checkoutStarts = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.CHECKOUT_START, startDate, endDate);
            long purchases = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.PRODUCT_PURCHASE, startDate, endDate);

            analysis.put("productViews", productViews);
            analysis.put("cartAdds", cartAdds);
            analysis.put("checkoutStarts", checkoutStarts);
            analysis.put("purchases", purchases);

            // Calculate conversion rates between funnel steps
            analysis.put("viewToCartRate", productViews > 0 ? (double) cartAdds / productViews * 100 : 0.0);
            analysis.put("cartToCheckoutRate", cartAdds > 0 ? (double) checkoutStarts / cartAdds * 100 : 0.0);
            analysis.put("checkoutToPurchaseRate", checkoutStarts > 0 ? (double) purchases / checkoutStarts * 100 : 0.0);
            analysis.put("overallConversionRate", productViews > 0 ? (double) purchases / productViews * 100 : 0.0);

        } catch (Exception e) {
            logger.error("Error getting checkout funnel analysis", e);
        }

        return analysis;
    }

    // Subscription Analytics Implementation
    @Override
    public Map<String, Object> getSubscriptionPerformanceMetrics(String planId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting subscription performance metrics for plan: {} from {} to {}", planId, startDate, endDate);

        Map<String, Object> metrics = new HashMap<>();

        try {
            // Get subscription-related events
            long subscriptionStarts = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_START, startDate, endDate);
            long subscriptionCancellations = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_CANCELLATION, startDate, endDate);
            long subscriptionRenewals = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_RENEWAL, startDate, endDate);

            metrics.put("planId", planId);
            metrics.put("subscriptionStarts", subscriptionStarts);
            metrics.put("subscriptionCancellations", subscriptionCancellations);
            metrics.put("subscriptionRenewals", subscriptionRenewals);
            metrics.put("churnRate", subscriptionStarts > 0 ? (double) subscriptionCancellations / subscriptionStarts * 100 : 0.0);
            metrics.put("renewalRate", subscriptionStarts > 0 ? (double) subscriptionRenewals / subscriptionStarts * 100 : 0.0);

        } catch (Exception e) {
            logger.error("Error getting subscription performance metrics for plan: {}", planId, e);
        }

        return metrics;
    }

    @Override
    public Map<String, Object> getSubscriptionAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting subscription analytics from {} to {}", startDate, endDate);

        Map<String, Object> analytics = new HashMap<>();

        try {
            long totalSubscriptions = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_START, startDate, endDate);
            long totalCancellations = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_CANCELLATION, startDate, endDate);
            long totalUpgrades = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_UPGRADE, startDate, endDate);
            long totalDowngrades = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_DOWNGRADE, startDate, endDate);

            analytics.put("totalSubscriptions", totalSubscriptions);
            analytics.put("totalCancellations", totalCancellations);
            analytics.put("totalUpgrades", totalUpgrades);
            analytics.put("totalDowngrades", totalDowngrades);
            analytics.put("netSubscriptions", totalSubscriptions - totalCancellations);
            analytics.put("churnRate", totalSubscriptions > 0 ? (double) totalCancellations / totalSubscriptions * 100 : 0.0);

        } catch (Exception e) {
            logger.error("Error getting subscription analytics", e);
        }

        return analytics;
    }

    @Override
    public Map<String, Object> getChurnAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting churn analysis from {} to {}", startDate, endDate);

        Map<String, Object> analysis = new HashMap<>();

        try {
            long totalCancellations = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_CANCELLATION, startDate, endDate);
            long totalActiveSubscriptions = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_START, startDate, endDate);

            double churnRate = totalActiveSubscriptions > 0 ? (double) totalCancellations / totalActiveSubscriptions * 100 : 0.0;

            analysis.put("totalCancellations", totalCancellations);
            analysis.put("totalActiveSubscriptions", totalActiveSubscriptions);
            analysis.put("churnRate", churnRate);
            analysis.put("retentionRate", 100.0 - churnRate);

        } catch (Exception e) {
            logger.error("Error getting churn analysis", e);
        }

        return analysis;
    }

    @Override
    public Map<String, Object> getRetentionAnalytics(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting retention analytics from {} to {}", startDate, endDate);

        Map<String, Object> analytics = new HashMap<>();

        try {
            long totalRenewals = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_RENEWAL, startDate, endDate);
            long totalExpirations = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_EXPIRED, startDate, endDate);

            analytics.put("totalRenewals", totalRenewals);
            analytics.put("totalExpirations", totalExpirations);
            analytics.put("retentionRate", (totalRenewals + totalExpirations) > 0
                    ? (double) totalRenewals / (totalRenewals + totalExpirations) * 100 : 0.0);

        } catch (Exception e) {
            logger.error("Error getting retention analytics", e);
        }

        return analytics;
    }

    @Override
    public Map<String, Object> getSubscriptionFunnelAnalysis(LocalDateTime startDate, LocalDateTime endDate) {
        logger.info("Getting subscription funnel analysis from {} to {}", startDate, endDate);

        Map<String, Object> analysis = new HashMap<>();

        try {
            long subscriptionViews = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_VIEW, startDate, endDate);
            long trialStarts = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_TRIAL_START, startDate, endDate);
            long subscriptionStarts = eventRepository.countByEventTypeAndEventTimestampBetween(
                    EventType.SUBSCRIPTION_START, startDate, endDate);

            analysis.put("subscriptionViews", subscriptionViews);
            analysis.put("trialStarts", trialStarts);
            analysis.put("subscriptionStarts", subscriptionStarts);

            // Calculate conversion rates
            analysis.put("viewToTrialRate", subscriptionViews > 0 ? (double) trialStarts / subscriptionViews * 100 : 0.0);
            analysis.put("trialToSubscriptionRate", trialStarts > 0 ? (double) subscriptionStarts / trialStarts * 100 : 0.0);
            analysis.put("overallConversionRate", subscriptionViews > 0 ? (double) subscriptionStarts / subscriptionViews * 100 : 0.0);

        } catch (Exception e) {
            logger.error("Error getting subscription funnel analysis", e);
        }

        return analysis;
    }

    @Override
    public byte[] exportAnalyticsData(LocalDateTime startDate, LocalDateTime endDate, String format) {
        // TODO: Implement analytics data export
        logger.info("Exporting analytics data from {} to {} in format {}", startDate, endDate, format);
        return "Export functionality not yet implemented".getBytes();
    }

    @Override
    public void archiveAnalyticsData(LocalDateTime cutoffDate) {
        // TODO: Implement analytics data archiving
        logger.info("Archiving analytics data older than {}", cutoffDate);
    }

    @Override
    public void cleanupOldEvents(LocalDateTime cutoffDate) {
        logger.info("Cleaning up events older than: {}", cutoffDate);

        long deletedCount = eventRepository.deleteByEventTimestampBefore(cutoffDate);
        logger.info("Cleaned up {} old events", deletedCount);
    }

    private void processEventForMetrics(AnalyticsEvent event) {
        try {
            // Update user metrics
            updateUserMetrics(event.getUserId());

            // Update content metrics if applicable
            if (event.getEntityId() != null) {
                updateContentMetrics(event.getEntityId());
            }

        } catch (Exception e) {
            logger.error("Error processing event for metrics: {}", event.getId(), e);
        }
    }

    private Map<String, Object> parseEventMessage(String eventMessage) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(eventMessage, new com.fasterxml.jackson.core.type.TypeReference<Map<String, Object>>() {
            });
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            logger.warn("Failed to parse event message as Map due to JSON error, message: {}", eventMessage, e);
            return null;
        } catch (RuntimeException e) {
            logger.warn("Failed to parse event message as Map, message: {}", eventMessage, e);
            return null;
        }
    }

    private TrackEventRequest createTrackEventRequest(Map<String, Object> eventData) {
        if (eventData == null) {
            return null;
        }
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            mapper.findAndRegisterModules();
            // Align with user-service payloads: if 'timestamp' (epoch millis) is present and 'eventTimestamp' absent, map it
            if (!eventData.containsKey("eventTimestamp") && eventData.containsKey("timestamp")) {
                Object tsObj = eventData.get("timestamp");
                try {
                    long epochMillis;
                    if (tsObj instanceof Number) {
                        epochMillis = ((Number) tsObj).longValue();
                    } else {
                        epochMillis = Long.parseLong(String.valueOf(tsObj));
                    }
                    java.time.Instant inst = java.time.Instant.ofEpochMilli(epochMillis);
                    java.time.LocalDateTime ldt = java.time.LocalDateTime.ofInstant(inst, java.time.ZoneOffset.UTC);
                    eventData = new java.util.HashMap<>(eventData);
                    eventData.put("eventTimestamp", ldt);
                } catch (Exception ignore) {
                    // leave as-is if conversion fails; downstream will default timestamp
                }
            }
            return mapper.convertValue(eventData, TrackEventRequest.class);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to convert event data to TrackEventRequest: {}", eventData, e);
            return null;
        }
    }

    // Manual conversion method to replace MapStruct
    private AnalyticsEventResponse convertToResponse(AnalyticsEvent event) {
        AnalyticsEventResponse response = new AnalyticsEventResponse();
        response.setId(event.getId());
        response.setUserId(event.getUserId());
        // Convert from AnalyticsEvent.EventType to standalone EventType
        response.setEventType(convertEventType(event.getEventType()));
        response.setEntityType(event.getEntityType());
        response.setEntityId(event.getEntityId());
        response.setEventTimestamp(event.getEventTimestamp());
        response.setSessionId(event.getSessionId());
        response.setUserAgent(event.getUserAgent());
        response.setIpAddress(event.getIpAddress());
        response.setEventData(event.getEventData());
        response.setCreatedAt(event.getCreatedAt());
        return response;
    }

    private com.raved.analytics.dto.response.AnalyticsSearchHitResponse toSearchHitResponse(
            com.raved.analytics.search.AnalyticsEventDocument doc) {
        com.raved.analytics.dto.response.AnalyticsSearchHitResponse r = new com.raved.analytics.dto.response.AnalyticsSearchHitResponse();
        r.setId(doc.getId());
        r.setUserId(doc.getUserId());
        r.setSessionId(doc.getSessionId());
        r.setEventType(doc.getEventType());
        r.setHashtags(doc.getHashtags());
        r.setContentTags(doc.getContentTags());
        r.setEventDate(doc.getEventDate());
        r.setEventHour(doc.getEventHour());
        r.setEventTimestamp(doc.getEventTimestamp());
        r.setPlatform(doc.getPlatform());
        r.setDeviceType(doc.getDeviceType());
        r.setEntityType(doc.getEntityType());
        r.setEntityId(doc.getEntityId());
        r.setProductId(doc.getProductId());
        r.setProductName(doc.getProductName());
        r.setCategory(doc.getCategory());
        r.setPrice(doc.getPrice());
        r.setCurrency(doc.getCurrency());
        r.setPostId(doc.getPostId());
        r.setInteractionType(doc.getInteractionType());
        r.setContentType(doc.getContentType());
        r.setContentCategory(doc.getContentCategory());
        r.setCountry(doc.getCountry());
        r.setCity(doc.getCity());
        return r;
    }

    // Helper method to convert between EventType enums
    private EventType convertEventType(EventType eventType) {
        return eventType; // direct mapping within same enum
    }

    // Search implementations
    @Override
    @Transactional(readOnly = true)
    public Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse> searchByUser(String userId, Pageable pageable) {
        if (searchRepository == null) {
            return Page.empty(pageable);
        }
        return searchRepository.findByUserId(userId, pageable)
                .map(this::toSearchHitResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse> searchByEventType(String eventType, Pageable pageable) {
        if (searchRepository == null) {
            return Page.empty(pageable);
        }
        return searchRepository.findByEventType(eventType, pageable)
                .map(this::toSearchHitResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse> searchByHashtag(String hashtag, Pageable pageable) {
        if (searchRepository == null) {
            return Page.empty(pageable);
        }
        return searchRepository.findByHashtagsContaining(hashtag, pageable)
                .map(this::toSearchHitResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<com.raved.analytics.dto.response.AnalyticsSearchHitResponse> searchByContentTag(String tag, Pageable pageable) {
        if (searchRepository == null) {
            return Page.empty(pageable);
        }
        return searchRepository.findByContentTagsContaining(tag, pageable)
                .map(this::toSearchHitResponse);
    }
}
