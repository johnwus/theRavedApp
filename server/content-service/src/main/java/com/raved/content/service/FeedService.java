package com.raved.content.service;

import com.raved.content.dto.response.FeedResponse;
import org.springframework.data.domain.Pageable;

/**
 * FeedService for TheRavedApp
 */
public interface FeedService {
    
    /**
     * Get personalized feed for a user
     */
    FeedResponse getPersonalizedFeed(Long userId, Pageable pageable);
    
    /**
     * Get discover feed for a user
     */
    FeedResponse getDiscoverFeed(Long userId, Pageable pageable);
    
    /**
     * Get trending feed
     */
    FeedResponse getTrendingFeed(Pageable pageable);
    
    /**
     * Get university feed
     */
    FeedResponse getUniversityFeed(Long userId, Long universityId, Pageable pageable);
    
    /**
     * Get faculty feed
     */
    FeedResponse getFacultyFeed(Long userId, Long facultyId, Pageable pageable);
    
    /**
     * Get hashtag feed
     */
    FeedResponse getHashtagFeed(String hashtag, Pageable pageable);
    
    /**
     * Get recent feed
     */
    FeedResponse getRecentFeed(Long userId, Pageable pageable);
    
    /**
     * Get popular feed
     */
    FeedResponse getPopularFeed(Pageable pageable);
    
    /**
     * Refresh user feed cache
     */
    void refreshUserFeed(Long userId);
    
    /**
     * Refresh global feeds cache
     */
    void refreshGlobalFeeds();
    
    /**
     * Get mixed feed
     */
    FeedResponse getMixedFeed(Long userId, Pageable pageable);
}
