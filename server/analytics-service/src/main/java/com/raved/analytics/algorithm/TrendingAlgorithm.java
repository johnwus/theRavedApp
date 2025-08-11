package com.raved.analytics.algorithm;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * TrendingAlgorithm for TheRavedApp
 */
@Component
public class TrendingAlgorithm {
    
    public double calculateTrendingScore(Long viewCount, Long likeCount, Long commentCount, Long shareCount, LocalDateTime createdAt) {
        // Simple trending score calculation
        double engagementScore = (likeCount * 2) + (commentCount * 3) + (shareCount * 4);
        double timeDecay = Math.max(0.1, 1.0 - ChronoUnit.HOURS.between(createdAt, LocalDateTime.now()) / 24.0);
        return engagementScore * timeDecay;
    }
    
    public double calculateTopicTrendingScore(Long mentionCount, Long uniqueUsers, LocalDateTime since) {
        // Topic trending score based on mentions and unique users
        double velocity = mentionCount / Math.max(1, ChronoUnit.HOURS.between(since, LocalDateTime.now()));
        return velocity * Math.log(uniqueUsers + 1);
    }
    
    public double predictTrendingPotential(Long viewCount, Long likeCount, Long commentCount, Long shareCount, LocalDateTime createdAt, int hours) {
        // Predict if content will become trending in the next N hours
        double currentScore = calculateTrendingScore(viewCount, likeCount, commentCount, shareCount, createdAt);
        double timeFactor = Math.max(0.1, 1.0 - ChronoUnit.HOURS.between(createdAt, LocalDateTime.now()) / (24.0 + hours));
        return currentScore * timeFactor / 100.0; // Normalize to 0-1 range
    }
}
