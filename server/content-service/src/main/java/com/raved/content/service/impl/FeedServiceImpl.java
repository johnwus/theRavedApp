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
    public FeedResponse getPersonalizedFeed(String userId, Pageable pageable) {
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
        List<String> followingIds = followRepository.findFollowingIdsByFollowerId(userId);
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
    public FeedResponse getDiscoverFeed(String userId, Pageable pageable) {
        logger.info("Getting discover feed for user: {}", userId);

        // Get posts from users not followed by the current user
        List<String> followingIds = followRepository.findFollowingIdsByFollowerId(userId);
        followingIds.add(userId); // Exclude user's own posts from discover

        Page<Post> posts;
        if (followingIds.isEmpty()) {
            posts = postRepository.findTrendingPosts(pageable);
        } else {
            posts = postRepository.findByAuthorIdNotInOrderByCreatedAtDesc(followingIds, pageable);
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

        Page<Post> posts = postRepository.findTrendingPosts(pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated trending feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getUniversityFeed(String userId, String universityId, Pageable pageable) {
        logger.info("Getting university feed for user: {} in university: {}", userId, universityId);

        Page<Post> posts = postRepository.findByUniversityIdAndIsDeletedFalseOrderByCreatedAtDesc(universityId,
                pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated university feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getFacultyFeed(String userId, String facultyId, Pageable pageable) {
        logger.info("Getting faculty feed for user: {} in faculty: {}", userId, facultyId);

        Page<Post> posts = postRepository.findByFacultyIdAndIsDeletedFalseOrderByCreatedAtDesc(facultyId, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated faculty feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getHashtagFeed(String hashtag, Pageable pageable) {
        logger.info("Getting hashtag feed for: {}", hashtag);

        Page<Post> posts = postRepository.findByTagsContainingAndIsDeletedFalseOrderByCreatedAtDesc(hashtag, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated hashtag feed with {} posts for hashtag: {}", postResponses.size(), hashtag);
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getRecentFeed(String userId, Pageable pageable) {
        logger.info("Getting recent feed for user: {}", userId);

        Page<Post> posts = postRepository.findByIsDeletedFalseOrderByCreatedAtDesc(pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated recent feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getPopularFeed(Pageable pageable) {
        logger.info("Getting popular feed");

        Page<Post> posts = postRepository.findByIsDeletedFalseOrderByEngagementScoreDesc(pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated popular feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getCategoryFeed(String category, Pageable pageable) {
        logger.info("Getting category feed for: {}", category);

        Page<Post> posts = postRepository.findByCategoryAndIsDeletedFalseOrderByCreatedAtDesc(category, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated category feed with {} posts for category: {}", postResponses.size(), category);
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getLanguageFeed(String language, Pageable pageable) {
        logger.info("Getting language feed for: {}", language);

        Page<Post> posts = postRepository.findByLanguageAndIsDeletedFalseOrderByCreatedAtDesc(language, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated language feed with {} posts for language: {}", postResponses.size(), language);
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getSentimentFeed(String sentiment, Pageable pageable) {
        logger.info("Getting sentiment feed for: {}", sentiment);

        Page<Post> posts = postRepository.findBySentimentAndIsDeletedFalseOrderByCreatedAtDesc(sentiment, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated sentiment feed with {} posts for sentiment: {}", postResponses.size(), sentiment);
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getEngagementFeed(double minScore, Pageable pageable) {
        logger.info("Getting engagement feed with min score: {}", minScore);

        Page<Post> posts = postRepository
                .findByEngagementScoreGreaterThanEqualAndIsDeletedFalseOrderByEngagementScoreDesc(minScore, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated engagement feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getTrendingScoreFeed(double minScore, Pageable pageable) {
        logger.info("Getting trending score feed with min score: {}", minScore);

        Page<Post> posts = postRepository
                .findByTrendingScoreGreaterThanEqualAndIsDeletedFalseOrderByTrendingScoreDesc(minScore, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated trending score feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getViralityScoreFeed(double minScore, Pageable pageable) {
        logger.info("Getting virality score feed with min score: {}", minScore);

        Page<Post> posts = postRepository
                .findByViralityScoreGreaterThanEqualAndIsDeletedFalseOrderByViralityScoreDesc(minScore, pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated virality score feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public void refreshUserFeed(String userId) {
        logger.info("Refreshing feed cache for user: {}", userId);

        // Clear user-specific cache
        Set<String> keys = redisTemplate.keys(FEED_CACHE_KEY + userId + ":*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        logger.info("Feed cache cleared for user: {}", userId);
    }

    @Override
    public void refreshGlobalFeeds() {
        logger.info("Refreshing global feeds cache");

        // Clear all feed-related cache
        Set<String> keys = redisTemplate.keys(FEED_CACHE_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        logger.info("Global feeds cache cleared");
    }

    @Override
    public FeedResponse getMixedFeed(String userId, Pageable pageable) {
        logger.info("Getting mixed feed for user: {}", userId);

        // Combine personalized and trending feeds
        FeedResponse personalizedFeed = getPersonalizedFeed(userId, pageable);
        FeedResponse trendingFeed = getTrendingFeed(pageable);

        List<PostResponse> mixedPosts = new ArrayList<>();
        mixedPosts.addAll(personalizedFeed.getPosts());
        mixedPosts.addAll(trendingFeed.getPosts());

        // Remove duplicates and limit to page size
        List<PostResponse> uniquePosts = mixedPosts.stream()
                .distinct()
                .limit(pageable.getPageSize())
                .collect(Collectors.toList());

        logger.info("Generated mixed feed with {} posts for user: {}", uniquePosts.size(), userId);
        return new FeedResponse(uniquePosts, uniquePosts.size(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getScheduledFeed(Pageable pageable) {
        logger.info("Getting scheduled feed");

        Page<Post> posts = postRepository.findByPublishStatusAndScheduledAtBeforeOrderByScheduledAtAsc("SCHEDULED",
                LocalDateTime.now(), pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated scheduled feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getFeaturedFeed(Pageable pageable) {
        logger.info("Getting featured feed");

        Page<Post> posts = postRepository.findByIsFeaturedTrueAndIsDeletedFalseOrderByFeaturedAtDesc(pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated featured feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }

    @Override
    public FeedResponse getPinnedFeed(Pageable pageable) {
        logger.info("Getting pinned feed");

        Page<Post> posts = postRepository.findByIsPinnedTrueAndIsDeletedFalseOrderByPinnedAtDesc(pageable);

        List<PostResponse> postResponses = posts.getContent().stream()
                .map(postMapper::toPostResponse)
                .collect(Collectors.toList());

        logger.info("Generated pinned feed with {} posts", postResponses.size());
        return new FeedResponse(postResponses, (int) posts.getTotalElements(), pageable.getPageNumber());
    }
}
