package com.raved.content.service;

import com.raved.content.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * PostService for TheRavedApp
 * This service handles all post operations using MongoDB as the primary
 * database
 */
public interface PostService {

    /**
     * Create a new post
     */
    Post createPost(Post post);

    /**
     * Get post by ID
     */
    Optional<Post> getPostById(String id);

    /**
     * Update post
     */
    Post updatePost(String id, Post post);

    /**
     * Delete post
     */
    void deletePost(String id);

    /**
     * Get posts by user ID
     */
    Page<Post> getPostsByUserId(String userId, Pageable pageable);

    /**
     * Get posts by faculty ID
     */
    Page<Post> getPostsByFacultyId(String facultyId, Pageable pageable);

    /**
     * Get public posts
     */
    Page<Post> getPublicPosts(Pageable pageable);

    /**
     * Get trending posts
     */
    List<Post> getTrendingPosts(int limit);

    /**
     * Get featured posts
     */
    List<Post> getFeaturedPosts(int limit);

    /**
     * Search posts
     */
    Page<Post> searchPosts(String query, Pageable pageable);

    /**
     * Increment view count
     */
    void incrementViewCount(String postId);

    /**
     * Update engagement metrics
     */
    void updateEngagementMetrics(String postId, int likesCount, int commentsCount, int sharesCount);

    /**
     * Flag post for moderation
     */
    void flagPost(String postId, String reason);

    /**
     * Feature post
     */
    void featurePost(String postId, int durationHours);

    /**
     * Pin post
     */
    void pinPost(String postId);

    /**
     * Unpin post
     */
    void unpinPost(String postId);

    /**
     * Moderate post
     */
    void moderatePost(String postId, String status, String reason, String moderatorId);

    /**
     * Get posts by category
     */
    Page<Post> getPostsByCategory(String category, Pageable pageable);

    /**
     * Get posts by tags
     */
    Page<Post> getPostsByTags(List<String> tags, Pageable pageable);

    /**
     * Get posts by status
     */
    Page<Post> getPostsByStatus(String status, Pageable pageable);

    /**
     * Get posts requiring moderation
     */
    Page<Post> getPostsRequiringModeration(Pageable pageable);

    /**
     * Get user's draft posts
     */
    Page<Post> getDraftPosts(String userId, Pageable pageable);

    /**
     * Get user's archived posts
     */
    Page<Post> getArchivedPosts(String userId, Pageable pageable);

    /**
     * Get related posts
     */
    List<Post> getRelatedPosts(String postId, int limit);

    /**
     * Get popular posts by time period
     */
    List<Post> getPopularPosts(String timePeriod, int limit);

    /**
     * Get posts by engagement score
     */
    Page<Post> getPostsByEngagementScore(double minScore, Pageable pageable);

    /**
     * Get posts by sentiment
     */
    Page<Post> getPostsBySentiment(String sentiment, Pageable pageable);

    /**
     * Get posts by language
     */
    Page<Post> getPostsByLanguage(String language, Pageable pageable);

    /**
     * Get posts by access level
     */
    Page<Post> getPostsByAccessLevel(String accessLevel, Pageable pageable);

    /**
     * Get posts by date range
     */
    Page<Post> getPostsByDateRange(String startDate, String endDate, Pageable pageable);

    /**
     * Get posts by engagement metrics
     */
    Page<Post> getPostsByEngagementMetrics(int minViews, int minLikes, int minComments, Pageable pageable);

    /**
     * Get posts by trending score
     */
    Page<Post> getPostsByTrendingScore(double minScore, Pageable pageable);

    /**
     * Get posts by virality score
     */
    Page<Post> getPostsByViralityScore(double minScore, Pageable pageable);

    /**
     * Get posts by content flags
     */
    Page<Post> getPostsByContentFlags(List<String> flags, Pageable pageable);

    /**
     * Get posts by cross references
     */
    Page<Post> getPostsByCrossReferences(String referenceType, String referenceId, Pageable pageable);

    /**
     * Get posts by version
     */
    Page<Post> getPostsByVersion(int version, Pageable pageable);

    /**
     * Get posts by change history
     */
    Page<Post> getPostsByChangeHistory(String changeType, String changeValue, Pageable pageable);

    /**
     * Get posts by custom fields
     */
    Page<Post> getPostsByCustomFields(String fieldName, String fieldValue, Pageable pageable);

    /**
     * Get posts by analytics data
     */
    Page<Post> getPostsByAnalyticsData(String metricName, String metricValue, Pageable pageable);

    /**
     * Get posts by SEO data
     */
    Page<Post> getPostsBySeoData(String seoField, String seoValue, Pageable pageable);

    /**
     * Get posts by moderation data
     */
    Page<Post> getPostsByModerationData(String moderationField, String moderationValue, Pageable pageable);

    /**
     * Get posts by content analysis
     */
    Page<Post> getPostsByContentAnalysis(String analysisField, String analysisValue, Pageable pageable);
}
