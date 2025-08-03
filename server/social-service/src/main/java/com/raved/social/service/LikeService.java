package com.raved.social.service;

import com.raved.social.dto.request.LikeRequest;
import com.raved.social.dto.response.LikeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * LikeService for TheRavedApp
 */
public interface LikeService {

    /**
     * Like a post
     */
    LikeResponse likePost(LikeRequest request);

    /**
     * Unlike a post
     */
    void unlikePost(Long userId, Long postId);

    /**
     * Check if user liked a post
     */
    boolean hasUserLikedPost(Long userId, Long postId);

    /**
     * Get likes for a post
     */
    Page<LikeResponse> getPostLikes(Long postId, Pageable pageable);

    /**
     * Get user's likes
     */
    Page<LikeResponse> getUserLikes(Long userId, Pageable pageable);

    /**
     * Get like count for post
     */
    long getLikeCount(Long postId);

    /**
     * Get recent likes for user's posts
     */
    List<LikeResponse> getRecentLikesForUser(Long userId, int limit);
}
