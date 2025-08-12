package com.raved.content.service;

import com.raved.content.dto.response.FeedResponse;
import org.springframework.data.domain.Pageable;

/**
 * FeedService for TheRavedApp MongoDB
 */
public interface FeedService {

    /**
     * Get personalized feed for a user
     */
    FeedResponse getPersonalizedFeed(String userId, Pageable pageable);

    /**
     * Get discover feed for a user
     */
    FeedResponse getDiscoverFeed(String userId, Pageable pageable);

    /**
     * Get trending feed
     */
    FeedResponse getTrendingFeed(Pageable pageable);

    /**
     * Get university feed
     */
    FeedResponse getUniversityFeed(String userId, String universityId, Pageable pageable);

    /**
     * Get faculty feed
     */
    FeedResponse getFacultyFeed(String userId, String facultyId, Pageable pageable);

    /**
     * Get hashtag feed
     */
    FeedResponse getHashtagFeed(String hashtag, Pageable pageable);

    /**
     * Get recent feed
     */
    FeedResponse getRecentFeed(String userId, Pageable pageable);

    /**
     * Get popular feed
     */
    FeedResponse getPopularFeed(Pageable pageable);

    /**
     * Get category feed
     */
    FeedResponse getCategoryFeed(String category, Pageable pageable);

    /**
     * Get language-specific feed
     */
    FeedResponse getLanguageFeed(String language, Pageable pageable);

    /**
     * Get sentiment-based feed
     */
    FeedResponse getSentimentFeed(String sentiment, Pageable pageable);

    /**
     * Get engagement-based feed
     */
    FeedResponse getEngagementFeed(double minScore, Pageable pageable);

    /**
     * Get trending score feed
     */
    FeedResponse getTrendingScoreFeed(double minScore, Pageable pageable);

    /**
     * Get virality score feed
     */
    FeedResponse getViralityScoreFeed(double minScore, Pageable pageable);

    /**
     * Refresh user feed cache
     */
    void refreshUserFeed(String userId);

    /**
     * Refresh global feeds cache
     */
    void refreshGlobalFeeds();

    /**
     * Get mixed feed
     */
    FeedResponse getMixedFeed(String userId, Pageable pageable);

    /**
     * Get scheduled posts feed
     */
    FeedResponse getScheduledFeed(Pageable pageable);

    /**
     * Get featured posts feed
     */
    FeedResponse getFeaturedFeed(Pageable pageable);

    /**
     * Get pinned posts feed
     */
    FeedResponse getPinnedFeed(Pageable pageable);
}
