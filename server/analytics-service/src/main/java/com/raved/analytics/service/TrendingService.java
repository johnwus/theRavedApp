package com.raved.analytics.service;

import com.raved.analytics.dto.response.TrendingContentResponse;
import com.raved.analytics.dto.response.TrendingTopicResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * TrendingService for TheRavedApp
 */
public interface TrendingService {

    /**
     * Get trending content
     */
    List<TrendingContentResponse> getTrendingContent(int limit);

    /**
     * Get trending topics
     */
    List<TrendingTopicResponse> getTrendingTopics(int limit);

    /**
     * Get trending content by category
     */
    List<TrendingContentResponse> getTrendingContentByCategory(String category, int limit);

    /**
     * Get trending content by timeframe
     */
    List<TrendingContentResponse> getTrendingContentByTimeframe(LocalDateTime startDate, LocalDateTime endDate, int limit);

    /**
     * Get trending analytics
     */
    Map<String, Object> getTrendingAnalytics();

    /**
     * Refresh trending cache
     */
    void refreshTrendingCache();

    /**
     * Predict trending content
     */
    List<Long> predictTrendingContent(int hours);

    /**
     * Update trending scores
     */
    void updateTrendingScores();
}
