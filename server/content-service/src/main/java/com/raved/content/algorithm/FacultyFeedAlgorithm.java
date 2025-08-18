package com.raved.content.algorithm;

import com.raved.content.model.Post;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * FacultyFeedAlgorithm for TheRavedApp MongoDB
 */
@Component
public class FacultyFeedAlgorithm {

    /**
     * Generate faculty-specific feed
     */
    public List<Post> generateFacultyFeed(List<Post> posts, String facultyId, Map<String, Double> facultyPreferences) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .filter(post -> facultyId.equals(post.getFacultyId()))
                .map(post -> calculateFacultyScore(post, facultyPreferences))
                .sorted((p1, p2) -> Double.compare(p2.getEngagementScore(), p1.getEngagementScore()))
                .collect(Collectors.toList());
    }

    /**
     * Generate university-wide feed with faculty context
     */
    public List<Post> generateUniversityFeed(List<Post> posts, String universityId, String facultyId,
            Map<String, Double> facultyPreferences) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .map(post -> calculateUniversityScore(post, facultyId, facultyPreferences))
                .sorted((p1, p2) -> Double.compare(p2.getEngagementScore(), p1.getEngagementScore()))
                .collect(Collectors.toList());
    }

    /**
     * Generate cross-faculty feed for collaboration
     */
    public List<Post> generateCrossFacultyFeed(List<Post> posts, List<String> facultyIds,
            Map<String, Double> collaborationPreferences) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .filter(post -> facultyIds.contains(post.getFacultyId()))
                .map(post -> calculateCollaborationScore(post, collaborationPreferences))
                .sorted((p1, p2) -> Double.compare(p2.getTrendingScore(), p1.getTrendingScore()))
                .collect(Collectors.toList());
    }

    /**
     * Generate faculty trending feed
     */
    public List<Post> generateFacultyTrendingFeed(List<Post> posts, String facultyId, int limit) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .filter(post -> facultyId.equals(post.getFacultyId()))
                .sorted((p1, p2) -> Double.compare(p2.getTrendingScore(), p1.getTrendingScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Generate faculty popular feed
     */
    public List<Post> generateFacultyPopularFeed(List<Post> posts, String facultyId, int limit) {
        if (posts == null || posts.isEmpty()) {
            return List.of();
        }

        return posts.stream()
                .filter(post -> !post.getIsDeleted())
                .filter(post -> "PUBLISHED".equals(post.getPublishStatus()))
                .filter(post -> facultyId.equals(post.getFacultyId()))
                .sorted((p1, p2) -> Double.compare(p2.getEngagementScore(), p1.getEngagementScore()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Calculate faculty-specific score for a post
     */
    private Post calculateFacultyScore(Post post, Map<String, Double> facultyPreferences) {
        double score = 0.0;

        // Faculty-specific preferences
        if (post.getCategory() != null && facultyPreferences.containsKey(post.getCategory())) {
            score += facultyPreferences.get(post.getCategory()) * 0.4;
        }

        // Engagement score
        score += (post.getEngagementScore() != null ? post.getEngagementScore() : 0.0) * 0.3;

        // Trending score
        score += (post.getTrendingScore() != null ? post.getTrendingScore() : 0.0) * 0.2;

        // Faculty relevance (posts from same faculty get bonus)
        score += 10.0;

        post.setEngagementScore(score);
        return post;
    }

    /**
     * Calculate university-wide score with faculty context
     */
    private Post calculateUniversityScore(Post post, String facultyId, Map<String, Double> facultyPreferences) {
        double score = 0.0;

        // Faculty-specific preferences
        if (post.getCategory() != null && facultyPreferences.containsKey(post.getCategory())) {
            score += facultyPreferences.get(post.getCategory()) * 0.3;
        }

        // Engagement score
        score += (post.getEngagementScore() != null ? post.getEngagementScore() : 0.0) * 0.3;

        // Trending score
        score += (post.getTrendingScore() != null ? post.getTrendingScore() : 0.0) * 0.2;

        // Faculty relevance
        if (facultyId.equals(post.getFacultyId())) {
            score += 5.0; // Same faculty bonus
        }

        // Time decay
        score += calculateTimeDecayScore(post.getCreatedAt()) * 0.2;

        post.setEngagementScore(score);
        return post;
    }

    /**
     * Calculate collaboration score for cross-faculty posts
     */
    private Post calculateCollaborationScore(Post post, Map<String, Double> collaborationPreferences) {
        double score = 0.0;

        // Collaboration preferences
        if (post.getCategory() != null && collaborationPreferences.containsKey(post.getCategory())) {
            score += collaborationPreferences.get(post.getCategory()) * 0.4;
        }

        // Trending score
        score += (post.getTrendingScore() != null ? post.getTrendingScore() : 0.0) * 0.3;

        // Virality score
        score += (post.getViralityScore() != null ? post.getViralityScore() : 0.0) * 0.2;

        // Cross-faculty bonus
        score += 15.0;

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
