package com.raved.analytics.service.impl;

import com.raved.analytics.algorithm.TrendingAlgorithm;
import com.raved.analytics.dto.response.TrendingContentResponse;
import com.raved.analytics.dto.response.TrendingTopicResponse;
import com.raved.analytics.model.EventType;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.service.TrendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of TrendingService
 */
@Service
@Transactional(readOnly = true)
public class TrendingServiceImpl implements TrendingService {

    private static final Logger logger = LoggerFactory.getLogger(TrendingServiceImpl.class);
    private static final String TRENDING_CACHE_PREFIX = "trending:";
    private static final int CACHE_EXPIRATION_MINUTES = 15;

    @Autowired
    private AnalyticsEventRepository eventRepository;

    @Autowired
    private ContentMetricsRepository contentMetricsRepository;

    @Autowired
    private TrendingAlgorithm trendingAlgorithm;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<TrendingContentResponse> getTrendingContent(int limit) {
        logger.debug("Getting trending content with limit: {}", limit);
        
        String cacheKey = TRENDING_CACHE_PREFIX + "content:" + limit;
        
        // Try cache first
        @SuppressWarnings("unchecked")
        List<TrendingContentResponse> cachedTrending = (List<TrendingContentResponse>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedTrending != null) {
            return cachedTrending;
        }
        
        // Calculate trending content
        LocalDateTime since = LocalDateTime.now().minusHours(24); // Last 24 hours
        List<Object[]> contentMetrics = contentMetricsRepository.getContentWithMetrics(since, limit * 2);
        
        List<TrendingContentResponse> trendingContent = new ArrayList<>();
        
        for (Object[] metrics : contentMetrics) {
            Long contentId = (Long) metrics[0];
            Long viewCount = (Long) metrics[1];
            Long likeCount = (Long) metrics[2];
            Long commentCount = (Long) metrics[3];
            Long shareCount = (Long) metrics[4];
            LocalDateTime createdAt = (LocalDateTime) metrics[5];
            
            // Calculate trending score using algorithm
            double trendingScore = trendingAlgorithm.calculateTrendingScore(
                    viewCount, likeCount, commentCount, shareCount, createdAt);
            
            if (trendingScore > 0) {
                TrendingContentResponse content = new TrendingContentResponse();
                content.setContentId(contentId);
                content.setTrendingScore(trendingScore);
                content.setViewCount(viewCount);
                content.setLikeCount(likeCount);
                content.setCommentCount(commentCount);
                content.setShareCount(shareCount);
                content.setCreatedAt(createdAt);
                content.setTrendingRank(trendingContent.size() + 1);
                
                trendingContent.add(content);
            }
        }
        
        // Sort by trending score and limit results
        List<TrendingContentResponse> result = trendingContent.stream()
                .sorted((a, b) -> Double.compare(b.getTrendingScore(), a.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // Update ranking
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setTrendingRank(i + 1);
        }
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, result, CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        
        logger.info("Found {} trending content items", result.size());
        return result;
    }

    @Override
    public List<TrendingTopicResponse> getTrendingTopics(int limit) {
        logger.debug("Getting trending topics with limit: {}", limit);
        
        String cacheKey = TRENDING_CACHE_PREFIX + "topics:" + limit;
        
        // Try cache first
        @SuppressWarnings("unchecked")
        List<TrendingTopicResponse> cachedTopics = (List<TrendingTopicResponse>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedTopics != null) {
            return cachedTopics;
        }
        
        // Get trending hashtags/topics from recent events
        LocalDateTime since = LocalDateTime.now().minusHours(6); // Last 6 hours for topics
        List<Object[]> topicData = eventRepository.getTrendingTopics(since, limit * 2);
        
        List<TrendingTopicResponse> trendingTopics = new ArrayList<>();
        
        for (Object[] data : topicData) {
            String topic = (String) data[0];
            Long mentionCount = (Long) data[1];
            Long uniqueUsers = (Long) data[2];
            
            // Calculate topic trending score
            double trendingScore = trendingAlgorithm.calculateTopicTrendingScore(
                    mentionCount, uniqueUsers, since);
            
            if (trendingScore > 0) {
                TrendingTopicResponse topicResponse = new TrendingTopicResponse();
                topicResponse.setTopic(topic);
                topicResponse.setMentionCount(mentionCount);
                topicResponse.setUniqueUsers(uniqueUsers);
                topicResponse.setTrendingScore(trendingScore);
                topicResponse.setTrendingRank(trendingTopics.size() + 1);
                
                trendingTopics.add(topicResponse);
            }
        }
        
        // Sort by trending score and limit results
        List<TrendingTopicResponse> result = trendingTopics.stream()
                .sorted((a, b) -> Double.compare(b.getTrendingScore(), a.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // Update ranking
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setTrendingRank(i + 1);
        }
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, result, CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        
        logger.info("Found {} trending topics", result.size());
        return result;
    }

    @Override
    public List<TrendingContentResponse> getTrendingContentByCategory(String category, int limit) {
        logger.debug("Getting trending content for category: {} with limit: {}", category, limit);
        
        String cacheKey = TRENDING_CACHE_PREFIX + "content:category:" + category + ":" + limit;
        
        // Try cache first
        @SuppressWarnings("unchecked")
        List<TrendingContentResponse> cachedTrending = (List<TrendingContentResponse>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedTrending != null) {
            return cachedTrending;
        }
        
        // Get trending content for specific category
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<Object[]> contentMetrics = contentMetricsRepository.getContentWithMetricsByCategory(category, since, limit * 2);
        
        List<TrendingContentResponse> trendingContent = new ArrayList<>();
        
        for (Object[] metrics : contentMetrics) {
            Long contentId = (Long) metrics[0];
            Long viewCount = (Long) metrics[1];
            Long likeCount = (Long) metrics[2];
            Long commentCount = (Long) metrics[3];
            Long shareCount = (Long) metrics[4];
            LocalDateTime createdAt = (LocalDateTime) metrics[5];
            
            double trendingScore = trendingAlgorithm.calculateTrendingScore(
                    viewCount, likeCount, commentCount, shareCount, createdAt);
            
            if (trendingScore > 0) {
                TrendingContentResponse content = new TrendingContentResponse();
                content.setContentId(contentId);
                content.setTrendingScore(trendingScore);
                content.setViewCount(viewCount);
                content.setLikeCount(likeCount);
                content.setCommentCount(commentCount);
                content.setShareCount(shareCount);
                content.setCreatedAt(createdAt);
                content.setCategory(category);
                
                trendingContent.add(content);
            }
        }
        
        // Sort and limit
        List<TrendingContentResponse> result = trendingContent.stream()
                .sorted((a, b) -> Double.compare(b.getTrendingScore(), a.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // Update ranking
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setTrendingRank(i + 1);
        }
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, result, CACHE_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        
        logger.info("Found {} trending content items for category: {}", result.size(), category);
        return result;
    }

    @Override
    public List<TrendingContentResponse> getTrendingContentByTimeframe(LocalDateTime startDate, LocalDateTime endDate, int limit) {
        logger.debug("Getting trending content from {} to {} with limit: {}", startDate, endDate, limit);
        
        List<Object[]> contentMetrics = contentMetricsRepository.getContentWithMetrics(startDate, endDate, limit * 2);
        
        List<TrendingContentResponse> trendingContent = new ArrayList<>();
        
        for (Object[] metrics : contentMetrics) {
            Long contentId = (Long) metrics[0];
            Long viewCount = (Long) metrics[1];
            Long likeCount = (Long) metrics[2];
            Long commentCount = (Long) metrics[3];
            Long shareCount = (Long) metrics[4];
            LocalDateTime createdAt = (LocalDateTime) metrics[5];
            
            double trendingScore = trendingAlgorithm.calculateTrendingScore(
                    viewCount, likeCount, commentCount, shareCount, createdAt);
            
            if (trendingScore > 0) {
                TrendingContentResponse content = new TrendingContentResponse();
                content.setContentId(contentId);
                content.setTrendingScore(trendingScore);
                content.setViewCount(viewCount);
                content.setLikeCount(likeCount);
                content.setCommentCount(commentCount);
                content.setShareCount(shareCount);
                content.setCreatedAt(createdAt);
                
                trendingContent.add(content);
            }
        }
        
        // Sort and limit
        List<TrendingContentResponse> result = trendingContent.stream()
                .sorted((a, b) -> Double.compare(b.getTrendingScore(), a.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
        
        // Update ranking
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setTrendingRank(i + 1);
        }
        
        logger.info("Found {} trending content items for timeframe", result.size());
        return result;
    }

    @Override
    public Map<String, Object> getTrendingAnalytics() {
        logger.debug("Getting trending analytics");
        
        String cacheKey = TRENDING_CACHE_PREFIX + "analytics";
        
        // Try cache first
        @SuppressWarnings("unchecked")
        Map<String, Object> cachedAnalytics = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cachedAnalytics != null) {
            return cachedAnalytics;
        }
        
        Map<String, Object> analytics = new HashMap<>();
        
        LocalDateTime last24Hours = LocalDateTime.now().minusHours(24);
        LocalDateTime last7Days = LocalDateTime.now().minusDays(7);
        
        // Trending content statistics
        long trendingContentCount = contentMetricsRepository.countTrendingContent(last24Hours);
        long totalContentCount = contentMetricsRepository.countContentCreatedAfter(last24Hours);
        double trendingContentRatio = totalContentCount > 0 ? (double) trendingContentCount / totalContentCount : 0;
        
        // Top trending categories
        List<Object[]> topCategories = contentMetricsRepository.getTopTrendingCategories(last24Hours, 10);
        Map<String, Long> categoriesMap = new HashMap<>();
        for (Object[] category : topCategories) {
            categoriesMap.put((String) category[0], (Long) category[1]);
        }
        
        // Trending velocity (how fast content becomes trending)
        double avgTrendingVelocity = calculateAverageTrendingVelocity(last7Days);
        
        // Peak trending hours
        List<Object[]> peakHours = eventRepository.getPeakEngagementHours(last7Days);
        Map<String, Long> peakHoursMap = new HashMap<>();
        for (Object[] hour : peakHours) {
            peakHoursMap.put(hour[0].toString(), (Long) hour[1]);
        }
        
        analytics.put("trendingContentCount", trendingContentCount);
        analytics.put("totalContentCount", totalContentCount);
        analytics.put("trendingContentRatio", trendingContentRatio);
        analytics.put("topCategories", categoriesMap);
        analytics.put("averageTrendingVelocity", avgTrendingVelocity);
        analytics.put("peakHours", peakHoursMap);
        analytics.put("generatedAt", LocalDateTime.now());
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, analytics, 30, TimeUnit.MINUTES);
        
        logger.info("Trending analytics generated");
        return analytics;
    }

    @Override
    public void refreshTrendingCache() {
        logger.info("Refreshing trending cache");
        
        // Clear all trending caches
        redisTemplate.delete(redisTemplate.keys(TRENDING_CACHE_PREFIX + "*"));
        
        // Pre-populate common trending data
        getTrendingContent(20);
        getTrendingTopics(10);
        getTrendingAnalytics();
        
        logger.info("Trending cache refreshed");
    }

    @Override
    public List<Long> predictTrendingContent(int hours) {
        logger.debug("Predicting trending content for next {} hours", hours);
        
        // Get recent high-engagement content that might become trending
        LocalDateTime since = LocalDateTime.now().minusHours(2);
        List<Object[]> candidates = contentMetricsRepository.getHighEngagementContent(since, 50);
        
        List<Long> predictions = new ArrayList<>();
        
        for (Object[] candidate : candidates) {
            Long contentId = (Long) candidate[0];
            Long viewCount = (Long) candidate[1];
            Long likeCount = (Long) candidate[2];
            Long commentCount = (Long) candidate[3];
            Long shareCount = (Long) candidate[4];
            LocalDateTime createdAt = (LocalDateTime) candidate[5];
            
            // Use algorithm to predict trending potential
            double trendingPotential = trendingAlgorithm.predictTrendingPotential(
                    viewCount, likeCount, commentCount, shareCount, createdAt, hours);
            
            if (trendingPotential > 0.7) { // 70% threshold for prediction
                predictions.add(contentId);
            }
        }
        
        logger.info("Predicted {} content items to become trending", predictions.size());
        return predictions;
    }

    @Override
    public void updateTrendingScores() {
        logger.info("Updating trending scores");
        
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        List<Object[]> allContent = contentMetricsRepository.getContentWithMetrics(since, 1000);
        
        int updatedCount = 0;
        for (Object[] content : allContent) {
            Long contentId = (Long) content[0];
            Long viewCount = (Long) content[1];
            Long likeCount = (Long) content[2];
            Long commentCount = (Long) content[3];
            Long shareCount = (Long) content[4];
            LocalDateTime createdAt = (LocalDateTime) content[5];
            
            double trendingScore = trendingAlgorithm.calculateTrendingScore(
                    viewCount, likeCount, commentCount, shareCount, createdAt);
            
            // Update trending score in database
            contentMetricsRepository.updateTrendingScore(contentId, trendingScore);
            updatedCount++;
        }
        
        logger.info("Updated trending scores for {} content items", updatedCount);
    }

    private double calculateAverageTrendingVelocity(LocalDateTime since) {
        // Calculate how quickly content becomes trending on average
        List<Object[]> trendingContent = contentMetricsRepository.getTrendingContentWithCreationTime(since);
        
        if (trendingContent.isEmpty()) {
            return 0.0;
        }
        
        double totalVelocity = 0.0;
        int count = 0;
        
        for (Object[] content : trendingContent) {
            LocalDateTime createdAt = (LocalDateTime) content[1];
            LocalDateTime trendingAt = (LocalDateTime) content[2];
            
            if (createdAt != null && trendingAt != null) {
                long hoursToTrending = java.time.Duration.between(createdAt, trendingAt).toHours();
                totalVelocity += hoursToTrending;
                count++;
            }
        }
        
        return count > 0 ? totalVelocity / count : 0.0;
    }
}
