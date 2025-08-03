package com.raved.social.service.impl;

import com.raved.social.dto.request.LikeRequest;
import com.raved.social.dto.response.LikeResponse;
import com.raved.social.exception.DuplicateLikeException;
import com.raved.social.mapper.LikeMapper;
import com.raved.social.model.Like;
import com.raved.social.repository.LikeRepository;
import com.raved.social.service.LikeService;
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
    public LikeResponse likePost(LikeRequest request) {
        logger.info("User {} liking post {}", request.getUserId(), request.getPostId());
        
        // Check if already liked
        if (likeRepository.existsByUserIdAndPostId(request.getUserId(), request.getPostId())) {
            throw new DuplicateLikeException("User has already liked this post");
        }
        
        Like like = new Like();
        like.setUserId(request.getUserId());
        like.setPostId(request.getPostId());
        like.setCreatedAt(LocalDateTime.now());
        
        Like savedLike = likeRepository.save(like);
        logger.info("Like created successfully: {}", savedLike.getId());
        
        return likeMapper.toLikeResponse(savedLike);
    }

    @Override
    public void unlikePost(Long userId, Long postId) {
        logger.info("User {} unliking post {}", userId, postId);
        
        Optional<Like> likeOpt = likeRepository.findByUserIdAndPostId(userId, postId);
        if (likeOpt.isPresent()) {
            likeRepository.delete(likeOpt.get());
            logger.info("Like removed successfully");
        } else {
            logger.warn("Like not found for user {} and post {}", userId, postId);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserLikedPost(Long userId, Long postId) {
        return likeRepository.existsByUserIdAndPostId(userId, postId);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeResponse> getPostLikes(Long postId, Pageable pageable) {
        logger.debug("Getting likes for post: {}", postId);
        
        Page<Like> likes = likeRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);
        return likes.map(likeMapper::toLikeResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LikeResponse> getUserLikes(Long userId, Pageable pageable) {
        logger.debug("Getting likes for user: {}", userId);
        
        Page<Like> likes = likeRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
        return likes.map(likeMapper::toLikeResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LikeResponse> getRecentLikesForUser(Long userId, int limit) {
        logger.debug("Getting recent likes for user: {} with limit: {}", userId, limit);
        
        List<Like> likes = likeRepository.findRecentLikesForUserPosts(userId, limit);
        return likes.stream()
                .map(likeMapper::toLikeResponse)
                .collect(Collectors.toList());
    }
}
