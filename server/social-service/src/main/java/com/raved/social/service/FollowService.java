package com.raved.social.service;

import com.raved.social.dto.request.FollowRequest;
import com.raved.social.dto.response.FollowResponse;
import com.raved.social.dto.response.UserFollowStatsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * FollowService for TheRavedApp
 */
public interface FollowService {

    /**
     * Follow a user
     */
    FollowResponse followUser(FollowRequest request);

    /**
     * Unfollow a user
     */
    void unfollowUser(Long followerId, Long followingId);

    /**
     * Check if user is following another user
     */
    boolean isFollowing(Long followerId, Long followingId);

    /**
     * Get followers of a user
     */
    Page<FollowResponse> getFollowers(Long userId, Pageable pageable);

    /**
     * Get users that a user is following
     */
    Page<FollowResponse> getFollowing(Long userId, Pageable pageable);

    /**
     * Get follow statistics for a user
     */
    UserFollowStatsResponse getFollowStats(Long userId);

    /**
     * Get mutual followers between two users
     */
    List<FollowResponse> getMutualFollowers(Long userId1, Long userId2);

    /**
     * Get suggested users to follow
     */
    List<FollowResponse> getSuggestedFollows(Long userId, int limit);

    /**
     * Get recent followers
     */
    List<FollowResponse> getRecentFollowers(Long userId, int limit);

    /**
     * Block a user
     */
    void blockUser(Long blockerId, Long blockedId);

    /**
     * Unblock a user
     */
    void unblockUser(Long blockerId, Long blockedId);

    /**
     * Check if user is blocked
     */
    boolean isBlocked(Long blockerId, Long blockedId);

    /**
     * Get blocked users
     */
    Page<FollowResponse> getBlockedUsers(Long userId, Pageable pageable);
}
