package com.raved.content.algorithm;

import com.raved.content.model.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FeedAlgorithm for TheRavedApp MongoDB
 */
@Component
public class FeedAlgorithm {

    /**
     * Generate personalized feed for a user
     */
    public List<Post> generatePersonalizedFeed(List<Post> posts, String userId, Map<String, Double> userPreferences) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .map(post -> calculatePersonalizationScore(post, userPreferences))
                .sorted((p1, p2) -> Double.compare(p2.getEngagementScore(), p1.getEngagementScore()))
                .collect(Collectors.toList());
    }

    /**
     * Generate discover feed for a user
     */
    public List<Post> generateDiscoverFeed(List<Post> posts, String userId, List<String> userInterests) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .map(post -> calculateDiscoveryScore(post, userInterests))
                .sorted((p1, p2) -> Double.compare(p2.getTrendingScore(), p1.getTrendingScore()))
                .collect(Collectors.toList());
    }

    /**
     * Generate trending feed
     */
    public List<Post> generateTrendingFeed(List<Post> posts, int limit) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .sorted((p1, p2) -> Double.compare(p2.getTrendingScore(), p1.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Generate category-based feed
     */
    public List<Post> generateCategoryFeed(List<Post> posts, String category, int limit) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .filter(post -> category.equals(post.getCategory()))
                .sorted((p1, p2) -> Double.compare(p2.getEngagementScore(), p1.getEngagementScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Generate language-specific feed
     */
    public List<Post> generateLanguageFeed(List<Post> posts, String language, int limit) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .filter(post -> language.equals(post.getLanguage()))
                .sorted((p1, p2) -> Double.compare(p2.getCreatedAt().compareTo(p1.getCreatedAt()), 0))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Generate sentiment-based feed
     */
    public List<Post> generateSentimentFeed(List<Post> posts, String sentiment, int limit) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .filter(post -> sentiment.equals(post.getSentiment()))
                .sorted((p1, p2) -> Double.compare(p2.getEngagementScore(), p1.getEngagementScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Calculate personalization score for a post
     */
    private Post calculatePersonalizationScore(Post post, Map<String, Double> userPreferences) {
        double score = 0.0;

        // Category preference
        if (post.getCategory() != null && userPreferences.containsKey(post.getCategory())) {
            score += userPreferences.get(post.getCategory()) * 0.3;
        }

        // Language preference
        if (post.getLanguage() != null && userPreferences.containsKey(post.getLanguage())) {
            score += userPreferences.get(post.getLanguage()) * 0.2;
        }

        // Engagement score
        score += (post.getEngagementScore() != null ? post.getEngagementScore() : 0.0) * 0.3;

        // Trending score
        score += (post.getTrendingScore() != null ? post.getTrendingScore() : 0.0) * 0.2;

        post.setEngagementScore(score);
        return post;
    }

    /**
     * Calculate discovery score for a post
     */
    private Post calculateDiscoveryScore(Post post, List<String> userInterests) {
        double score = 0.0;

        // Interest matching
        if (post.getTags() != null && userInterests != null) {
            long matchingTags = post.getTags().stream()
                    .filter(userInterests::contains)
                    .count();
            score += (matchingTags * 10.0);
        }

        // Trending score
        score += (post.getTrendingScore() != null ? post.getTrendingScore() : 0.0) * 0.4;

        // Virality score
        score += (post.getViralityScore() != null ? post.getViralityScore() : 0.0) * 0.3;

        // Time decay
        score += calculateTimeDecayScore(post.getCreatedAt()) * 0.3;

        post.setTrendingScore(score);
        return post;
    }

    /**
     * Calculate time decay score
     */
    private double calculateTimeDecayScore(LocalDateTime createdAt) {
        if (createdAt == null) {
            return 0.0;
        }

        long hoursSinceCreation = java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
        return Math.exp(-hoursSinceCreation / 24.0) * 100.0;
    }
}
