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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    private UserMetricsRepository userMetricsRepository;

    @Autowired
    private ContentMetricsRepository contentMetricsRepository;

    @Autowired
    private AnalyticsEventMapper eventMapper;

    @Autowired
    private EngagementCalculator engagementCalculator;

    @Override
    public AnalyticsEventResponse trackEvent(TrackEventRequest request) {
        logger.info("Tracking event: {} for user: {}", request.getEventType(), request.getUserId());
        
        try {
            AnalyticsEvent event = eventMapper.toAnalyticsEvent(request);
            event.setTimestamp(LocalDateTime.now());
            event.setCreatedAt(LocalDateTime.now());
            
            AnalyticsEvent savedEvent = eventRepository.save(event);
            
            // Process event for metrics updates
            processEventForMetrics(savedEvent);
            
            logger.info("Event tracked successfully: {}", savedEvent.getId());
            return eventMapper.toAnalyticsEventResponse(savedEvent);
            
        } catch (Exception e) {
            logger.error("Error tracking event", e);
            throw new AnalyticsProcessingException("Failed to track event: " + e.getMessage());
        }
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
    public Page<AnalyticsEventResponse> getEventsByUser(Long userId, Pageable pageable) {
        logger.debug("Getting events for user: {}", userId);
        
        Page<AnalyticsEvent> events = eventRepository.findByUserIdOrderByTimestampDesc(userId, pageable);
        return events.map(eventMapper::toAnalyticsEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnalyticsEventResponse> getEventsByType(EventType eventType, Pageable pageable) {
        logger.debug("Getting events by type: {}", eventType);
        
        Page<AnalyticsEvent> events = eventRepository.findByEventTypeOrderByTimestampDesc(eventType, pageable);
        return events.map(eventMapper::toAnalyticsEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnalyticsEventResponse> getEventsInDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        logger.debug("Getting events between {} and {}", startDate, endDate);
        
        Page<AnalyticsEvent> events = eventRepository.findByTimestampBetweenOrderByTimestampDesc(
                startDate, endDate, pageable);
        return events.map(eventMapper::toAnalyticsEventResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getEventCountsByType(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting event counts by type between {} and {}", startDate, endDate);
        
        List<Object[]> results = eventRepository.countEventsByType(startDate, endDate);
        Map<String, Long> eventCounts = new HashMap<>();
        
        for (Object[] result : results) {
            EventType eventType = (EventType) result[0];
            Long count = (Long) result[1];
            eventCounts.put(eventType.name(), count);
        }
        
        return eventCounts;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Long> getDailyEventCounts(LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting daily event counts between {} and {}", startDate, endDate);
        
        List<Object[]> results = eventRepository.countEventsByDay(startDate, endDate);
        Map<String, Long> dailyCounts = new HashMap<>();
        
        for (Object[] result : results) {
            String date = result[0].toString();
            Long count = (Long) result[1];
            dailyCounts.put(date, count);
        }
        
        return dailyCounts;
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalEventCount() {
        return eventRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventCountForUser(Long userId) {
        return eventRepository.countByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getEventCountForUser(Long userId, EventType eventType) {
        return eventRepository.countByUserIdAndEventType(userId, eventType);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserEngagementMetrics(Long userId) {
        logger.debug("Getting engagement metrics for user: {}", userId);
        
        Optional<UserMetrics> userMetricsOpt = userMetricsRepository.findByUserId(userId);
        if (userMetricsOpt.isEmpty()) {
            return new HashMap<>();
        }
        
        UserMetrics metrics = userMetricsOpt.get();
        Map<String, Object> engagementMetrics = new HashMap<>();
        
        engagementMetrics.put("totalPosts", metrics.getTotalPosts());
        engagementMetrics.put("totalLikes", metrics.getTotalLikes());
        engagementMetrics.put("totalComments", metrics.getTotalComments());
        engagementMetrics.put("totalShares", metrics.getTotalShares());
        engagementMetrics.put("totalFollowers", metrics.getTotalFollowers());
        engagementMetrics.put("totalFollowing", metrics.getTotalFollowing());
        engagementMetrics.put("engagementRate", metrics.getEngagementRate());
        engagementMetrics.put("lastActiveAt", metrics.getLastActiveAt());
        
        return engagementMetrics;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getContentEngagementMetrics(Long contentId) {
        logger.debug("Getting engagement metrics for content: {}", contentId);
        
        Optional<ContentMetrics> contentMetricsOpt = contentMetricsRepository.findByContentId(contentId);
        if (contentMetricsOpt.isEmpty()) {
            return new HashMap<>();
        }
        
        ContentMetrics metrics = contentMetricsOpt.get();
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
    public void updateUserMetrics(Long userId) {
        logger.info("Updating metrics for user: {}", userId);
        
        try {
            Optional<UserMetrics> existingMetricsOpt = userMetricsRepository.findByUserId(userId);
            UserMetrics metrics = existingMetricsOpt.orElse(new UserMetrics());
            
            if (existingMetricsOpt.isEmpty()) {
                metrics.setUserId(userId);
                metrics.setCreatedAt(LocalDateTime.now());
            }
            
            // Calculate metrics from events
            long totalPosts = eventRepository.countByUserIdAndEventType(userId, EventType.POST_CREATED);
            long totalLikes = eventRepository.countByUserIdAndEventType(userId, EventType.POST_LIKED);
            long totalComments = eventRepository.countByUserIdAndEventType(userId, EventType.COMMENT_CREATED);
            long totalShares = eventRepository.countByUserIdAndEventType(userId, EventType.POST_SHARED);
            
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
    public void updateContentMetrics(Long contentId) {
        logger.info("Updating metrics for content: {}", contentId);
        
        try {
            Optional<ContentMetrics> existingMetricsOpt = contentMetricsRepository.findByContentId(contentId);
            ContentMetrics metrics = existingMetricsOpt.orElse(new ContentMetrics());
            
            if (existingMetricsOpt.isEmpty()) {
                metrics.setContentId(contentId);
                metrics.setCreatedAt(LocalDateTime.now());
            }
            
            // Calculate metrics from events
            long viewCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.POST_VIEWED);
            long likeCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.POST_LIKED);
            long commentCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.COMMENT_CREATED);
            long shareCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.POST_SHARED);
            
            metrics.setViewCount(viewCount);
            metrics.setLikeCount(likeCount);
            metrics.setCommentCount(commentCount);
            metrics.setShareCount(shareCount);
            metrics.setEngagementScore(engagementCalculator.calculateContentEngagementScore(contentId));
            metrics.setUpdatedAt(LocalDateTime.now());
            
            contentMetricsRepository.save(metrics);
            logger.info("Content metrics updated for content: {}", contentId);
            
        } catch (Exception e) {
            logger.error("Error updating content metrics for content: {}", contentId, e);
        }
    }

    @Override
    public void cleanupOldEvents(LocalDateTime cutoffDate) {
        logger.info("Cleaning up events older than: {}", cutoffDate);
        
        long deletedCount = eventRepository.deleteByTimestampBefore(cutoffDate);
        logger.info("Cleaned up {} old events", deletedCount);
    }

    private void processEventForMetrics(AnalyticsEvent event) {
        try {
            // Update user metrics
            updateUserMetrics(event.getUserId());
            
            // Update content metrics if applicable
            if (event.getTargetId() != null) {
                updateContentMetrics(event.getTargetId());
            }
            
        } catch (Exception e) {
            logger.error("Error processing event for metrics: {}", event.getId(), e);
        }
    }

    private Map<String, Object> parseEventMessage(String eventMessage) {
        // TODO: Implement JSON parsing
        // For now, return null to avoid errors
        return null;
    }

    private TrackEventRequest createTrackEventRequest(Map<String, Object> eventData) {
        // TODO: Implement event data to request conversion
        // For now, return null to avoid errors
        return null;
    }
}
