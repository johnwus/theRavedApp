package com.raved.social.service.impl;

import com.raved.social.dto.request.LikeRequest;
import com.raved.social.dto.response.LikeResponse;
import com.raved.social.exception.DuplicateLikeException;
import com.raved.social.mapper.LikeMapper;
import com.raved.social.model.Like;
import com.raved.social.repository.LikeRepository;
import com.raved.social.service.LikeService;
import com.raved.social.util.MongoIdConverter;
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
 * Implementation of LikeService
 */
@Service
@Transactional
public class LikeServiceImpl implements LikeService {

    private static final Logger logger = LoggerFactory.getLogger(LikeServiceImpl.class);

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private LikeMapper likeMapper;

    @Override
    public LikeResponse likeTarget(LikeRequest request) {
        logger.info("User {} liking target {} of type {}", request.getUserId(), request.getTargetId(), request.getTargetType());
        
        // Convert string target type to enum
        Like.TargetType targetType = Like.TargetType.valueOf(request.getTargetType().toUpperCase());
        
        // Check if already liked
        if (likeRepository.existsByUserIdAndTargetIdAndTargetType(request.getUserId(), request.getTargetId(), targetType)) {
            throw new DuplicateLikeException("User has already liked this target");
        }
        
        Like like = new Like();
        like.setUserId(request.getUserId());
        like.setTargetId(request.getTargetId());
        like.setTargetType(targetType);
        like.setCreatedAt(LocalDateTime.now());
        
        Like savedLike = likeRepository.save(like);
        logger.info("Like created successfully: {}", savedLike.getId());
        
        return likeMapper.toLikeResponse(savedLike);
    }

    @Override
    public void unlikeTarget(Long userId, Long targetId, String targetType) {
        logger.info("User {} unliking target {} of type {}", userId, targetId, targetType);
        
        Like.TargetType targetTypeEnum = Like.TargetType.valueOf(targetType.toUpperCase());
        Optional<Like> likeOpt = likeRepository.findByUserIdAndTargetIdAndTargetType(MongoIdConverter.toStringId(userId), MongoIdConverter.toStringId(targetId), targetTypeEnum);
        if (likeOpt.isPresent()) {
            likeRepository.delete(likeOpt.get());
            logger.info("Like removed successfully");
        } else {
            logger.warn("Like not found for user {} and target {} of type {}", userId, targetId, targetType);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedTarget(Long userId, Long targetId, String targetType) {
        Like.TargetType targetTypeEnum = Like.TargetType.valueOf(targetType.toUpperCase());
        return likeRepository.existsByUserIdAndTargetIdAndTargetType(MongoIdConverter.toStringId(userId), MongoIdConverter.toStringId(targetId), targetTypeEnum);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeResponse> getTargetLikes(Long targetId, String targetType, Pageable pageable) {
        logger.debug("Getting likes for target: {} of type: {}", targetId, targetType);

        Like.TargetType targetTypeEnum = Like.TargetType.valueOf(targetType.toUpperCase());
        Page<Like> likes = likeRepository.findByTargetIdAndTargetTypeOrderByCreatedAtDesc(MongoIdConverter.toStringId(targetId), targetTypeEnum, pageable);
        return likes.map(likeMapper::toLikeResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeResponse> getUserLikes(Long userId, Pageable pageable) {
        logger.debug("Getting likes for user: {}", userId);

        Page<Like> likes = likeRepository.findByUserIdOrderByCreatedAtDesc(MongoIdConverter.toStringId(userId), pageable);
        return likes.map(likeMapper::toLikeResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLikeCount(Long targetId, String targetType) {
        Like.TargetType targetTypeEnum = Like.TargetType.valueOf(targetType.toUpperCase());
        return likeRepository.countByTargetIdAndTargetType(MongoIdConverter.toStringId(targetId), targetTypeEnum);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LikeResponse> getRecentLikesForUser(Long userId, int limit) {
        logger.debug("Getting recent likes for user: {} with limit: {}", userId, limit);

        List<Like> likes = likeRepository.findRecentLikesForUserPosts(MongoIdConverter.toStringId(userId), limit);
        return likes.stream()
                .map(likeMapper::toLikeResponse)
                .collect(Collectors.toList());
    }

    // Legacy methods for backward compatibility (deprecated)
    @Override
    @Deprecated
    public LikeResponse likePost(LikeRequest request) {
        // Convert post request to generic target request
        LikeRequest targetRequest = new LikeRequest(request.getUserId(), request.getTargetId(), "POST");
        return likeTarget(targetRequest);
    }

    @Override
    @Deprecated
    public void unlikePost(Long userId, Long postId) {
        unlikeTarget(userId, postId, "POST");
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(Long userId, Long postId) {
        return hasUserLikedTarget(userId, postId, "POST");
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public Page<LikeResponse> getPostLikes(Long postId, Pageable pageable) {
        return getTargetLikes(postId, "POST", pageable);
    }

    @Override
    @Deprecated
    @Transactional(readOnly = true)
    public long getLikeCount(Long postId) {
        return getLikeCount(postId, "POST");
    }
}
