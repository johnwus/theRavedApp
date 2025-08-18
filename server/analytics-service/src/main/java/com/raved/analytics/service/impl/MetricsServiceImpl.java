package com.raved.analytics.service.impl;

import com.raved.analytics.algorithm.EngagementCalculator;
import com.raved.analytics.dto.response.ContentMetricsResponse;
import com.raved.analytics.dto.response.UserMetricsResponse;
import com.raved.analytics.mapper.ContentMetricsMapper;
import com.raved.analytics.mapper.UserMetricsMapper;
import com.raved.analytics.model.ContentMetrics;
import com.raved.analytics.model.EventType;
import com.raved.analytics.model.UserMetrics;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.UserMetricsRepository;
import com.raved.analytics.service.MetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of MetricsService
 */
@Service
@Transactional
public class MetricsServiceImpl implements MetricsService {

    private static final Logger logger = LoggerFactory.getLogger(MetricsServiceImpl.class);
    private static final String METRICS_CACHE_PREFIX = "metrics:";
    private static final int CACHE_EXPIRATION_HOURS = 1;

    @Autowired
    private UserMetricsRepository userMetricsRepository;

    @Autowired
    private ContentMetricsRepository contentMetricsRepository;

    @Autowired
    private AnalyticsEventRepository eventRepository;

    @Autowired
    private UserMetricsMapper userMetricsMapper;

    @Autowired
    private ContentMetricsMapper contentMetricsMapper;

    @Autowired
    private EngagementCalculator engagementCalculator;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public UserMetricsResponse getUserMetrics(String userId) {
        logger.debug("Getting metrics for user: {}", userId);
        
        String cacheKey = METRICS_CACHE_PREFIX + "user:" + userId;
        
        // Try cache first
        UserMetricsResponse cachedMetrics = (UserMetricsResponse) redisTemplate.opsForValue().get(cacheKey);
        if (cachedMetrics != null) {
            return cachedMetrics;
        }
        
        Optional<UserMetrics> metricsOpt = userMetricsRepository.findByUserId(userId);
        if (metricsOpt.isEmpty()) {
            // Create default metrics
            UserMetrics defaultMetrics = createDefaultUserMetrics(userId);
            return userMetricsMapper.toUserMetricsResponse(defaultMetrics);
        }
        
        UserMetricsResponse response = userMetricsMapper.toUserMetricsResponse(metricsOpt.get());
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, response, CACHE_EXPIRATION_HOURS, TimeUnit.HOURS);
        
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ContentMetricsResponse getContentMetrics(String contentId) {
        logger.debug("Getting metrics for content: {}", contentId);
        
        String cacheKey = METRICS_CACHE_PREFIX + "content:" + contentId;
        
        // Try cache first
        ContentMetricsResponse cachedMetrics = (ContentMetricsResponse) redisTemplate.opsForValue().get(cacheKey);
        if (cachedMetrics != null) {
            return cachedMetrics;
        }
        
        List<ContentMetrics> metricsList = contentMetricsRepository.findByContentId(contentId);
        if (metricsList.isEmpty()) {
            // Create default metrics
            ContentMetrics defaultMetrics = createDefaultContentMetrics(contentId);
            return contentMetricsMapper.toContentMetricsResponse(defaultMetrics);
        }

        // Get the most recent metrics (assuming the first one is the most recent)
        ContentMetrics metrics = metricsList.get(0);
        ContentMetricsResponse response = contentMetricsMapper.toContentMetricsResponse(metrics);

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserMetricsResponse> getTopUsersByEngagement(Pageable pageable) {
        logger.debug("Getting top users by engagement");
        
        Page<UserMetrics> topUsers = userMetricsRepository.findTopByEngagementRate(pageable);
        return topUsers.map(userMetricsMapper::toUserMetricsResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContentMetricsResponse> getTopContentByEngagement(Pageable pageable) {
        logger.debug("Getting top content by engagement");
        
        Page<ContentMetrics> topContent = contentMetricsRepository.findTopByEngagementScore(pageable);
        return topContent.map(contentMetricsMapper::toContentMetricsResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getPlatformMetrics() {
        logger.debug("Getting platform-wide metrics");
        
        String cacheKey = METRICS_CACHE_PREFIX + "platform";
        
        // Try cache first
        @SuppressWarnings("unchecked")
        Map<String, Object> cachedMetrics = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedMetrics != null) {
            return cachedMetrics;
        }
        
        Map<String, Object> platformMetrics = new HashMap<>();
        
        // User metrics
        long totalUsers = userMetricsRepository.count();
        long activeUsers = userMetricsRepository.countActiveUsers(LocalDateTime.now().minusDays(30));
        double avgEngagementRate = userMetricsRepository.getAverageEngagementRate();
        
        // Content metrics
        long totalContent = contentMetricsRepository.count();
        long totalViews = contentMetricsRepository.getTotalViews();
        long totalLikes = contentMetricsRepository.getTotalLikes();
        long totalComments = contentMetricsRepository.getTotalComments();
        long totalShares = contentMetricsRepository.getTotalShares();
        
        // Event metrics
        long totalEvents = eventRepository.count();
        long todayEvents = eventRepository.countByTimestampAfter(LocalDateTime.now().minusDays(1));
        
        platformMetrics.put("totalUsers", totalUsers);
        platformMetrics.put("activeUsers", activeUsers);
        platformMetrics.put("averageEngagementRate", avgEngagementRate);
        platformMetrics.put("totalContent", totalContent);
        platformMetrics.put("totalViews", totalViews);
        platformMetrics.put("totalLikes", totalLikes);
        platformMetrics.put("totalComments", totalComments);
        platformMetrics.put("totalShares", totalShares);
        platformMetrics.put("totalEvents", totalEvents);
        platformMetrics.put("todayEvents", todayEvents);
        platformMetrics.put("generatedAt", LocalDateTime.now());
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, platformMetrics, 30, TimeUnit.MINUTES);
        
        return platformMetrics;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getUserEngagementTrends(String userId, LocalDateTime startDate, LocalDateTime endDate) {
        logger.debug("Getting engagement trends for user: {} between {} and {}", userId, startDate, endDate);
        
        Map<String, Object> trends = new HashMap<>();
        
        // Get daily engagement data
        List<Object[]> dailyLikes = eventRepository.getDailyEventCounts(userId, EventType.POST_LIKE, startDate,
                endDate);
        List<Object[]> dailyComments = eventRepository.getDailyEventCounts(userId, EventType.POST_COMMENT, startDate,
                endDate);
        List<Object[]> dailyShares = eventRepository.getDailyEventCounts(userId, EventType.POST_SHARE, startDate,
                endDate);
        List<Object[]> dailyPosts = eventRepository.getDailyEventCounts(userId, EventType.POST_CREATE, startDate,
                endDate);
        
        trends.put("dailyLikes", convertToMap(dailyLikes));
        trends.put("dailyComments", convertToMap(dailyComments));
        trends.put("dailyShares", convertToMap(dailyShares));
        trends.put("dailyPosts", convertToMap(dailyPosts));
        
        // Calculate growth rates
        double likesGrowthRate = calculateGrowthRate(dailyLikes);
        double commentsGrowthRate = calculateGrowthRate(dailyComments);
        double sharesGrowthRate = calculateGrowthRate(dailyShares);
        double postsGrowthRate = calculateGrowthRate(dailyPosts);
        
        trends.put("likesGrowthRate", likesGrowthRate);
        trends.put("commentsGrowthRate", commentsGrowthRate);
        trends.put("sharesGrowthRate", sharesGrowthRate);
        trends.put("postsGrowthRate", postsGrowthRate);
        
        return trends;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getContentPerformanceMetrics(String contentId, LocalDateTime startDate,
            LocalDateTime endDate) {
        logger.debug("Getting performance metrics for content: {} between {} and {}", contentId, startDate, endDate);
        
        Map<String, Object> performance = new HashMap<>();
        
        // Get hourly performance data
        List<Object[]> hourlyViews = eventRepository.getHourlyEventCounts(contentId, EventType.POST_VIEW, startDate,
                endDate);
        List<Object[]> hourlyLikes = eventRepository.getHourlyEventCounts(contentId, EventType.POST_LIKE, startDate,
                endDate);
        List<Object[]> hourlyComments = eventRepository.getHourlyEventCounts(contentId, EventType.POST_COMMENT,
                startDate, endDate);
        List<Object[]> hourlyShares = eventRepository.getHourlyEventCounts(contentId, EventType.POST_SHARE, startDate,
                endDate);
        
        performance.put("hourlyViews", convertToMap(hourlyViews));
        performance.put("hourlyLikes", convertToMap(hourlyLikes));
        performance.put("hourlyComments", convertToMap(hourlyComments));
        performance.put("hourlyShares", convertToMap(hourlyShares));
        
        // Calculate engagement rate over time
        double engagementRate = engagementCalculator.calculateContentEngagementScore(contentId).doubleValue();
        performance.put("engagementRate", engagementRate);
        
        // Calculate reach and impressions
        long uniqueViewers = eventRepository.countUniqueUsers(contentId, EventType.POST_VIEW, startDate, endDate);
        long totalImpressions = eventRepository.countByTargetIdAndEventTypeAndTimestampBetween(
                contentId, EventType.POST_VIEW, startDate, endDate);
        
        performance.put("uniqueViewers", uniqueViewers);
        performance.put("totalImpressions", totalImpressions);
        performance.put("reachRate", uniqueViewers > 0 ? (double) totalImpressions / uniqueViewers : 0);
        
        return performance;
    }

    @Override
    public void recalculateUserMetrics(String userId) {
        logger.info("Recalculating metrics for user: {}", userId);
        
        try {
            Optional<UserMetrics> existingMetricsOpt = userMetricsRepository.findByUserId(userId);
            UserMetrics metrics = existingMetricsOpt.orElse(new UserMetrics());
            
            if (existingMetricsOpt.isEmpty()) {
                metrics.setUserId(userId);
                metrics.setCreatedAt(LocalDateTime.now());
            }
            
            // Recalculate all metrics from scratch
            long totalPosts = eventRepository.countByUserIdAndEventType(userId, EventType.POST_CREATE);
            long totalLikes = eventRepository.countByUserIdAndEventType(userId, EventType.POST_LIKE);
            long totalComments = eventRepository.countByUserIdAndEventType(userId, EventType.POST_COMMENT);
            long totalShares = eventRepository.countByUserIdAndEventType(userId, EventType.POST_SHARE);
            long totalViews = eventRepository.countByUserIdAndEventType(userId, EventType.POST_VIEW);
            
            metrics.setTotalPosts(totalPosts);
            metrics.setTotalLikes(totalLikes);
            metrics.setTotalComments(totalComments);
            metrics.setTotalShares(totalShares);
            metrics.setTotalViews(totalViews);
            metrics.setEngagementRate(engagementCalculator.calculateUserEngagementRate(userId));
            metrics.setLastActiveAt(getLastActiveTime(userId));
            metrics.setUpdatedAt(LocalDateTime.now());
            
            userMetricsRepository.save(metrics);
            
            // Clear cache
            String cacheKey = METRICS_CACHE_PREFIX + "user:" + userId;
            redisTemplate.delete(cacheKey);
            
            logger.info("User metrics recalculated for user: {}", userId);
            
        } catch (Exception e) {
            logger.error("Error recalculating user metrics for user: {}", userId, e);
        }
    }

    @Override
    public void recalculateContentMetrics(String contentId) {
        logger.info("Recalculating metrics for content: {}", contentId);
        
        try {
            List<ContentMetrics> existingMetricsList = contentMetricsRepository.findByContentId(contentId);
            ContentMetrics metrics = existingMetricsList.isEmpty() ? new ContentMetrics() : existingMetricsList.get(0);

            if (existingMetricsList.isEmpty()) {
                metrics.setContentId(contentId);
                metrics.setCreatedAt(LocalDateTime.now());
            }
            
            // Recalculate all metrics from scratch
            long viewCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.POST_VIEW);
            long likeCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.POST_LIKE);
            long commentCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.POST_COMMENT);
            long shareCount = eventRepository.countByTargetIdAndEventType(contentId, EventType.POST_SHARE);
            
            metrics.setViewCount(viewCount);
            metrics.setLikeCount(likeCount);
            metrics.setCommentCount(commentCount);
            metrics.setShareCount(shareCount);
            metrics.setEngagementScore(engagementCalculator.calculateContentEngagementScore(contentId));
            
            // Calculate reach and impressions
            LocalDateTime last30Days = LocalDateTime.now().minusDays(30);
            long reachCount = eventRepository.countUniqueUsers(contentId, EventType.POST_VIEW, last30Days, LocalDateTime.now());
            long impressionCount = eventRepository.countByTargetIdAndEventTypeAndTimestampAfter(
                    contentId, EventType.POST_VIEW, last30Days);
            
            metrics.setReachCount(reachCount);
            metrics.setImpressionCount(impressionCount);
            metrics.setUpdatedAt(LocalDateTime.now());
            
            contentMetricsRepository.save(metrics);
            
            // Clear cache
            String cacheKey = METRICS_CACHE_PREFIX + "content:" + contentId;
            redisTemplate.delete(cacheKey);
            
            logger.info("Content metrics recalculated for content: {}", contentId);
            
        } catch (Exception e) {
            logger.error("Error recalculating content metrics for content: {}", contentId, e);
        }
    }

    @Override
    public void refreshPlatformMetrics() {
        logger.info("Refreshing platform metrics");
        
        // Clear platform metrics cache
        String cacheKey = METRICS_CACHE_PREFIX + "platform";
        redisTemplate.delete(cacheKey);
        
        // Regenerate platform metrics
        getPlatformMetrics();
        
        logger.info("Platform metrics refreshed");
    }

    private UserMetrics createDefaultUserMetrics(String userId) {
        UserMetrics metrics = new UserMetrics();
        metrics.setUserId(userId);
        metrics.setTotalPosts(0L);
        metrics.setTotalLikes(0L);
        metrics.setTotalComments(0L);
        metrics.setTotalShares(0L);
        metrics.setTotalViews(0L);
        metrics.setTotalFollowers(0L);
        metrics.setTotalFollowing(0L);
        metrics.setEngagementRate(0.0);
        metrics.setCreatedAt(LocalDateTime.now());
        metrics.setUpdatedAt(LocalDateTime.now());
        return metrics;
    }

    private ContentMetrics createDefaultContentMetrics(String contentId) {
        ContentMetrics metrics = new ContentMetrics();
        metrics.setContentId(contentId);
        metrics.setViewCount(0L);
        metrics.setLikeCount(0L);
        metrics.setCommentCount(0L);
        metrics.setShareCount(0L);
        metrics.setEngagementScore(0.0);
        metrics.setReachCount(0L);
        metrics.setImpressionCount(0L);
        metrics.setCreatedAt(LocalDateTime.now());
        metrics.setUpdatedAt(LocalDateTime.now());
        return metrics;
    }

    private Map<String, Long> convertToMap(List<Object[]> results) {
        Map<String, Long> map = new HashMap<>();
        for (Object[] result : results) {
            String key = result[0].toString();
            Long value = (Long) result[1];
            map.put(key, value);
        }
        return map;
    }

    private double calculateGrowthRate(List<Object[]> dailyData) {
        if (dailyData.size() < 2) {
            return 0.0;
        }
        
        Long firstValue = (Long) dailyData.get(0)[1];
        Long lastValue = (Long) dailyData.get(dailyData.size() - 1)[1];
        
        if (firstValue == 0) {
            return lastValue > 0 ? 100.0 : 0.0;
        }
        
        return ((double) (lastValue - firstValue) / firstValue) * 100.0;
    }

    private LocalDateTime getLastActiveTime(String userId) {
        Optional<LocalDateTime> lastActive = eventRepository.findLastEventTimeForUser(userId);
        return lastActive.orElse(LocalDateTime.now());
    }
}
