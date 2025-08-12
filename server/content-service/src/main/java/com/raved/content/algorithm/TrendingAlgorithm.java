package com.raved.content.algorithm;

import com.raved.content.model.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * TrendingAlgorithm for TheRavedApp MongoDB
 */
@Component
public class TrendingAlgorithm {

    /**
     * Calculate trending score for a post
     */
    public double calculateTrendingScore(Post post) {
        if (post == null) {
            return 0.0;
        }

        double engagementScore = calculateEngagementScore(post);
        double timeDecayScore = calculateTimeDecayScore(post.getCreatedAt());
        double viralityScore = calculateViralityScore(post);

        // Weighted combination of scores
        return (engagementScore * 0.4) + (timeDecayScore * 0.3) + (viralityScore * 0.3);
    }

    /**
     * Calculate engagement score based on likes, comments, shares, views
     */
    private double calculateEngagementScore(Post post) {
        int likes = post.getLikesCount() != null ? post.getLikesCount() : 0;
        int comments = post.getCommentsCount() != null ? post.getCommentsCount() : 0;
        int shares = post.getSharesCount() != null ? post.getSharesCount() : 0;
        int views = post.getViewsCount() != null ? post.getViewsCount() : 0;

        // Weighted engagement calculation
        double engagement = (likes * 3.0) + (comments * 2.0) + (shares * 4.0) + (views * 0.1);

        // Normalize to 0-100 scale
        return Math.min(engagement / 100.0, 100.0);
    }

    /**
     * Calculate time decay score (newer posts get higher scores)
     */
    private double calculateTimeDecayScore(LocalDateTime createdAt) {
        if (createdAt == null) {
            return 0.0;
        }

        long hoursSinceCreation = java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();

        // Exponential decay: score = e^(-hours/24) * 100
        // This gives newer posts higher scores that decay over time
        return Math.exp(-hoursSinceCreation / 24.0) * 100.0;
    }

    /**
     * Calculate virality score based on growth rate
     */
    private double calculateViralityScore(Post post) {
        // This would typically use historical data to calculate growth rate
        // For now, we'll use a simplified approach based on current metrics

        int totalEngagement = (post.getLikesCount() != null ? post.getLikesCount() : 0) +
                (post.getCommentsCount() != null ? post.getCommentsCount() : 0) +
                (post.getSharesCount() != null ? post.getSharesCount() : 0);

        // Simple virality score based on engagement relative to post age
        if (post.getCreatedAt() != null) {
            long hoursSinceCreation = java.time.Duration.between(post.getCreatedAt(), LocalDateTime.now()).toHours();
            if (hoursSinceCreation > 0) {
                return Math.min((double) totalEngagement / hoursSinceCreation, 100.0);
            }
        }

        return 0.0;
    }

    /**
     * Calculate trending score for multiple posts
     */
    public List<Post> calculateTrendingScores(List<Post> posts) {
        posts.forEach(post -> {
            double trendingScore = calculateTrendingScore(post);
            post.setTrendingScore(trendingScore);
        });

        // Sort by trending score (descending)
        posts.sort((p1, p2) -> Double.compare(p2.getTrendingScore(), p1.getTrendingScore()));

        return posts;
    }

    /**
     * Get trending posts with minimum score threshold
     */
    public List<Post> getTrendingPosts(List<Post> posts, double minScore, int limit) {
        var trendingPosts = calculateTrendingScores(posts);

        return trendingPosts.stream()
                .filter(post -> post.getTrendingScore() >= minScore)
                .limit(limit)
                .toList();
    }

    /**
     * Calculate trending score by category
     */
    public Map<String, Double> calculateTrendingScoresByCategory(List<Post> posts) {
        return posts.stream()
                .filter(post -> post.getCategory() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        Post::getCategory,
                        java.util.stream.Collectors.averagingDouble(this::calculateTrendingScore)));
    }

    /**
     * Calculate trending score by time period
     */
    public Map<String, Double> calculateTrendingScoresByTimePeriod(List<Post> posts) {
        LocalDateTime now = LocalDateTime.now();

        return posts.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        post -> getTimePeriod(post.getCreatedAt(), now),
                        java.util.stream.Collectors.averagingDouble(this::calculateTrendingScore)));
    }

    /**
     * Get time period for a post
     */
    private String getTimePeriod(LocalDateTime createdAt, LocalDateTime now) {
        if (createdAt == null) {
            return "UNKNOWN";
        }

        long hoursSinceCreation = java.time.Duration.between(createdAt, now).toHours();

        if (hoursSinceCreation < 1) {
            return "LAST_HOUR";
        } else if (hoursSinceCreation < 24) {
            return "LAST_DAY";
        } else if (hoursSinceCreation < 168) { // 7 days
            return "LAST_WEEK";
        } else if (hoursSinceCreation < 720) { // 30 days
            return "LAST_MONTH";
        } else {
            return "OLDER";
        }
    }
}
