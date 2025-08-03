package com.raved.social.service.impl;

import com.raved.social.dto.request.FollowRequest;
import com.raved.social.dto.response.FollowResponse;
import com.raved.social.dto.response.UserFollowStatsResponse;
import com.raved.social.exception.DuplicateFollowException;
import com.raved.social.exception.SelfFollowException;
import com.raved.social.mapper.FollowMapper;
import com.raved.social.model.Follow;
import com.raved.social.repository.FollowRepository;
import com.raved.social.service.FollowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of FollowService
 */
@Service
@Transactional
public class FollowServiceImpl implements FollowService {

    private static final Logger logger = LoggerFactory.getLogger(FollowServiceImpl.class);

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private FollowMapper followMapper;

    @Override
    public FollowResponse followUser(FollowRequest request) {
        logger.info("User {} following user {}", request.getFollowerId(), request.getFollowingId());
        
        // Check if trying to follow self
        if (request.getFollowerId().equals(request.getFollowingId())) {
            throw new SelfFollowException("Cannot follow yourself");
        }
        
        // Check if already following
        if (followRepository.existsByFollowerIdAndFollowingId(request.getFollowerId(), request.getFollowingId())) {
            throw new DuplicateFollowException("Already following this user");
        }
        
        Follow follow = new Follow();
        follow.setFollowerId(request.getFollowerId());
        follow.setFollowingId(request.getFollowingId());
        follow.setCreatedAt(LocalDateTime.now());
        
        Follow savedFollow = followRepository.save(follow);
        logger.info("Follow relationship created successfully: {}", savedFollow.getId());
        
        return followMapper.toFollowResponse(savedFollow);
    }

    @Override
    public void unfollowUser(Long followerId, Long followingId) {
        logger.info("User {} unfollowing user {}", followerId, followingId);
        
        Optional<Follow> followOpt = followRepository.findByFollowerIdAndFollowingId(followerId, followingId);
        if (followOpt.isPresent()) {
            followRepository.delete(followOpt.get());
            logger.info("Follow relationship removed successfully");
        } else {
            logger.warn("Follow relationship not found for follower {} and following {}", followerId, followingId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFollowing(Long followerId, Long followingId) {
        return followRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowResponse> getFollowers(Long userId, Pageable pageable) {
        logger.debug("Getting followers for user: {}", userId);
        
        Page<Follow> followers = followRepository.findByFollowingIdOrderByCreatedAtDesc(userId, pageable);
        return followers.map(followMapper::toFollowResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowResponse> getFollowing(Long userId, Pageable pageable) {
        logger.debug("Getting following for user: {}", userId);
        
        Page<Follow> following = followRepository.findByFollowerIdOrderByCreatedAtDesc(userId, pageable);
        return following.map(followMapper::toFollowResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public UserFollowStatsResponse getFollowStats(Long userId) {
        logger.debug("Getting follow stats for user: {}", userId);
        
        long followersCount = followRepository.countByFollowingId(userId);
        long followingCount = followRepository.countByFollowerId(userId);
        
        UserFollowStatsResponse stats = new UserFollowStatsResponse();
        stats.setUserId(userId);
        stats.setFollowersCount(followersCount);
        stats.setFollowingCount(followingCount);
        
        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowResponse> getMutualFollowers(Long userId1, Long userId2) {
        logger.debug("Getting mutual followers between users: {} and {}", userId1, userId2);
        
        List<Follow> mutualFollows = followRepository.findMutualFollowers(userId1, userId2);
        return mutualFollows.stream()
                .map(followMapper::toFollowResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowResponse> getSuggestedFollows(Long userId, int limit) {
        logger.debug("Getting suggested follows for user: {} with limit: {}", userId, limit);
        
        // Simple suggestion algorithm: users followed by people you follow
        List<Follow> suggestions = followRepository.findSuggestedFollows(userId, limit);
        return suggestions.stream()
                .map(followMapper::toFollowResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<FollowResponse> getRecentFollowers(Long userId, int limit) {
        logger.debug("Getting recent followers for user: {} with limit: {}", userId, limit);
        
        List<Follow> recentFollowers = followRepository.findRecentFollowers(userId, limit);
        return recentFollowers.stream()
                .map(followMapper::toFollowResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void blockUser(Long blockerId, Long blockedId) {
        logger.info("User {} blocking user {}", blockerId, blockedId);
        
        // Remove existing follow relationships
        followRepository.deleteByFollowerIdAndFollowingId(blockerId, blockedId);
        followRepository.deleteByFollowerIdAndFollowingId(blockedId, blockerId);
        
        // Create block relationship (could be a separate Block entity)
        // For now, we'll use a special Follow record with a flag
        Follow block = new Follow();
        block.setFollowerId(blockerId);
        block.setFollowingId(blockedId);
        block.setIsBlocked(true);
        block.setCreatedAt(LocalDateTime.now());
        
        followRepository.save(block);
        logger.info("User blocked successfully");
    }

    @Override
    public void unblockUser(Long blockerId, Long blockedId) {
        logger.info("User {} unblocking user {}", blockerId, blockedId);
        
        Optional<Follow> blockOpt = followRepository.findByFollowerIdAndFollowingIdAndIsBlockedTrue(blockerId, blockedId);
        if (blockOpt.isPresent()) {
            followRepository.delete(blockOpt.get());
            logger.info("User unblocked successfully");
        } else {
            logger.warn("Block relationship not found for blocker {} and blocked {}", blockerId, blockedId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBlocked(Long blockerId, Long blockedId) {
        return followRepository.existsByFollowerIdAndFollowingIdAndIsBlockedTrue(blockerId, blockedId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FollowResponse> getBlockedUsers(Long userId, Pageable pageable) {
        logger.debug("Getting blocked users for user: {}", userId);
        
        Page<Follow> blockedUsers = followRepository.findByFollowerIdAndIsBlockedTrueOrderByCreatedAtDesc(userId, pageable);
        return blockedUsers.map(followMapper::toFollowResponse);
    }
}
