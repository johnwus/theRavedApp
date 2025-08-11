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
     * Like a target (post, comment, or product)
     */
    LikeResponse likeTarget(LikeRequest request);

    /**
     * Unlike a target
     */
    void unlikeTarget(Long userId, Long targetId, String targetType);

    /**
     * Check if user liked a target
     */
    boolean hasUserLikedTarget(Long userId, Long targetId, String targetType);

    /**
     * Get likes for a target
     */
    Page<LikeResponse> getTargetLikes(Long targetId, String targetType, Pageable pageable);

    /**
     * Get user's likes
     */
    Page<LikeResponse> getUserLikes(Long userId, Pageable pageable);

    /**
     * Get like count for target
     */
    long getLikeCount(Long targetId, String targetType);

    /**
     * Get recent likes for user's posts
     */
    List<LikeResponse> getRecentLikesForUser(Long userId, int limit);

    // Legacy methods for backward compatibility (deprecated)
    @Deprecated
    LikeResponse likePost(LikeRequest request);

    @Deprecated
    void unlikePost(Long userId, Long postId);

    @Deprecated
    boolean hasUserLikedPost(Long userId, Long postId);

    @Deprecated
    Page<LikeResponse> getPostLikes(Long postId, Pageable pageable);

    @Deprecated
    long getLikeCount(Long postId);
}
