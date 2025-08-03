package com.raved.content.service.impl;

import com.raved.content.dto.response.FeedResponse;
import com.raved.content.dto.response.PostResponse;
import com.raved.content.mapper.PostMapper;
import com.raved.content.model.Post;
import com.raved.content.repository.FollowRepository;
import com.raved.content.repository.PostRepository;
import com.raved.content.service.FeedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Implementation of FeedService
 */
@Service
@Transactional(readOnly = true)
public class FeedServiceImpl implements FeedService {

    private static final Logger logger = LoggerFactory.getLogger(FeedServiceImpl.class);
    private static final String FEED_CACHE_KEY = "user:feed:";
    private static final int CACHE_EXPIRATION_HOURS = 1;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public FeedResponse getPersonalizedFeed(Long userId, Pageable pageable) {
        logger.info("Getting personalized feed for user: {}", userId);
        
        String cacheKey = FEED_CACHE_KEY + userId + ":" + pageable.getPageNumber();
        
        // Try to get from cache first
        @SuppressWarnings("unchecked")
        List<PostResponse> cachedFeed = (List<PostResponse>) redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedFeed != null && !cachedFeed.isEmpty()) {
            logger.debug("Returning cached feed for user: {}", userId);
            return new FeedResponse(cachedFeed, cachedFeed.size(), pageable.getPageNumber());
        }
        
        // Get following user IDs
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(userId);
        followingIds.add(userId); // Include user's own posts
        
        // Get posts from followed users
        Page<Post> posts;
        if (followingIds.isEmpty()) {
            // If user doesn't follow anyone, show trending/popular posts
            posts = postRepository.findTrendingPosts(pageable);
        } else {
            posts = postRepository.findByAuthorIdInOrderByCreatedAtDesc(followingIds, pageable);
        }
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, postResponses, CACHE_EXPIRATION_HOURS, TimeUnit.HOURS);
        
        logger.info("Generated personalized feed with {} posts for user: {}", postResponses.size(), userId);
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getDiscoverFeed(Long userId, Pageable pageable) {
        logger.info("Getting discover feed for user: {}", userId);
        
        // Get posts from users not followed by the current user
        List<Long> followingIds = followRepository.findFollowingIdsByFollowerId(userId);
        followingIds.add(userId); // Exclude user's own posts from discover
        
        Page<Post> posts;
        if (followingIds.isEmpty()) {
            posts = postRepository.findTrendingPosts(pageable);
        } else {
            posts = postRepository.findByAuthorIdNotInOrderByEngagementScoreDesc(followingIds, pageable);
        }
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        logger.info("Generated discover feed with {} posts for user: {}", postResponses.size(), userId);
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getTrendingFeed(Pageable pageable) {
        logger.info("Getting trending feed");
        
        String cacheKey = "trending:feed:" + pageable.getPageNumber();
        
        // Try to get from cache first
        @SuppressWarnings("unchecked")
        List<PostResponse> cachedFeed = (List<PostResponse>) redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedFeed != null && !cachedFeed.isEmpty()) {
            logger.debug("Returning cached trending feed");
            return new FeedResponse(cachedFeed, cachedFeed.size(), pageable.getPageNumber());
        }
        
        // Get trending posts (high engagement in last 24 hours)
        LocalDateTime since = LocalDateTime.now().minusHours(24);
        Page<Post> posts = postRepository.findTrendingPostsSince(since, pageable);
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        // Cache the result for shorter time (30 minutes for trending)
        redisTemplate.opsForValue().set(cacheKey, postResponses, 30, TimeUnit.MINUTES);
        
        logger.info("Generated trending feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getUniversityFeed(Long userId, Long universityId, Pageable pageable) {
        logger.info("Getting university feed for user: {} at university: {}", userId, universityId);
        
        Page<Post> posts = postRepository.findByUniversityIdOrderByCreatedAtDesc(universityId, pageable);
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        logger.info("Generated university feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getFacultyFeed(Long userId, Long facultyId, Pageable pageable) {
        logger.info("Getting faculty feed for user: {} at faculty: {}", userId, facultyId);
        
        Page<Post> posts = postRepository.findByFacultyIdOrderByCreatedAtDesc(facultyId, pageable);
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        logger.info("Generated faculty feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getHashtagFeed(String hashtag, Pageable pageable) {
        logger.info("Getting hashtag feed for: #{}", hashtag);
        
        Page<Post> posts = postRepository.findByHashtagOrderByCreatedAtDesc(hashtag, pageable);
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        logger.info("Generated hashtag feed with {} posts for #{}", postResponses.size(), hashtag);
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getRecentFeed(Long userId, Pageable pageable) {
        logger.info("Getting recent feed for user: {}", userId);
        
        // Get most recent posts from all users
        Pageable recentPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), 
                Sort.by(Sort.Direction.DESC, "createdAt"));
        
        Page<Post> posts = postRepository.findAll(recentPageable);
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        logger.info("Generated recent feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getPopularFeed(Pageable pageable) {
        logger.info("Getting popular feed");
        
        String cacheKey = "popular:feed:" + pageable.getPageNumber();
        
        // Try to get from cache first
        @SuppressWarnings("unchecked")
        List<PostResponse> cachedFeed = (List<PostResponse>) redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedFeed != null && !cachedFeed.isEmpty()) {
            logger.debug("Returning cached popular feed");
            return new FeedResponse(cachedFeed, cachedFeed.size(), pageable.getPageNumber());
        }
        
        // Get popular posts (high engagement overall)
        Page<Post> posts = postRepository.findPopularPosts(pageable);
        
        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());
        
        // Cache the result
        redisTemplate.opsForValue().set(cacheKey, postResponses, 2, TimeUnit.HOURS);
        
        logger.info("Generated popular feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public void refreshUserFeed(Long userId) {
        logger.info("Refreshing feed cache for user: {}", userId);
        
        // Clear cached feed for user
        Set<String> keys = redisTemplate.keys(FEED_CACHE_KEY + userId + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
            logger.info("Cleared {} cached feed entries for user: {}", keys.size(), userId);
        }
    }

    @Override
    public void refreshGlobalFeeds() {
        logger.info("Refreshing global feed caches");
        
        // Clear trending and popular feed caches
        Set<String> trendingKeys = redisTemplate.keys("trending:feed:*");
        Set<String> popularKeys = redisTemplate.keys("popular:feed:*");
        
        List<String> allKeys = new ArrayList<>();
        if (trendingKeys != null) allKeys.addAll(trendingKeys);
        if (popularKeys != null) allKeys.addAll(popularKeys);
        
        if (!allKeys.isEmpty()) {
            redisTemplate.delete(allKeys);
            logger.info("Cleared {} global feed cache entries", allKeys.size());
        }
    }

    @Override
    public FeedResponse getMixedFeed(Long userId, Pageable pageable) {
        logger.info("Getting mixed feed for user: {}", userId);
        
        // Get a mix of personalized, trending, and discover content
        int pageSize = pageable.getPageSize();
        int personalizedSize = (int) (pageSize * 0.6); // 60% personalized
        int trendingSize = (int) (pageSize * 0.25);    // 25% trending
        int discoverSize = pageSize - personalizedSize - trendingSize; // 15% discover
        
        List<PostResponse> mixedPosts = new ArrayList<>();
        
        // Get personalized posts
        FeedResponse personalizedFeed = getPersonalizedFeed(userId, 
                PageRequest.of(0, personalizedSize, pageable.getSort()));
        mixedPosts.addAll(personalizedFeed.getPosts());
        
        // Get trending posts
        FeedResponse trendingFeed = getTrendingFeed(
                PageRequest.of(0, trendingSize, pageable.getSort()));
        mixedPosts.addAll(trendingFeed.getPosts());
        
        // Get discover posts
        FeedResponse discoverFeed = getDiscoverFeed(userId, 
                PageRequest.of(0, discoverSize, pageable.getSort()));
        mixedPosts.addAll(discoverFeed.getPosts());
        
        // Shuffle the mixed posts to avoid predictable patterns
        // Collections.shuffle(mixedPosts);
        
        logger.info("Generated mixed feed with {} posts for user: {}", mixedPosts.size(), userId);
        return new FeedResponse(mixedPosts, mixedPosts.size(), pageable.getPageNumber());
    }
}
