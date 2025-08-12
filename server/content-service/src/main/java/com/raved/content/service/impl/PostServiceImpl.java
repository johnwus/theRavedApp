package com.raved.content.service.impl;

import com.raved.content.model.Post;
import com.raved.content.repository.PostRepository;
import com.raved.content.service.PostService;
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

/**
 * Implementation of PostService using MongoDB
 * This service handles all post operations using MongoDB as the primary
 * database
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private PostRepository postRepository;

    @Override
    public Post createPost(Post post) {
        logger.info("Creating new post for user: {}", post.getUserId());

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(post);
        logger.info("Post created successfully with ID: {}", savedPost.getId());

        return savedPost;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Post> getPostById(String id) {
        logger.debug("Getting post by ID: {}", id);
        return postRepository.findById(id);
    }

    @Override
    public Post updatePost(String id, Post postUpdate) {
        logger.info("Updating post with ID: {}", id);

        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isEmpty()) {
            throw new RuntimeException("Post not found with ID: " + id);
        }

        Post existingPost = postOpt.get();

        // Update fields if provided
        if (postUpdate.getContent() != null) {
            existingPost.setContent(postUpdate.getContent());
        }
        if (postUpdate.getTitle() != null) {
            existingPost.setTitle(postUpdate.getTitle());
        }
        if (postUpdate.getTags() != null) {
            existingPost.setTags(postUpdate.getTags());
        }
        if (postUpdate.getCategory() != null) {
            existingPost.setCategory(postUpdate.getCategory());
        }
        if (postUpdate.getVisibility() != null) {
            existingPost.setVisibility(postUpdate.getVisibility());
        }
        if (postUpdate.getPublishStatus() != null) {
            existingPost.setPublishStatus(postUpdate.getPublishStatus());
        }

        existingPost.setUpdatedAt(LocalDateTime.now());

        Post savedPost = postRepository.save(existingPost);
        logger.info("Post updated successfully with ID: {}", id);

        return savedPost;
    }

    @Override
    public void deletePost(String id) {
        logger.info("Deleting post with ID: {}", id);

        Optional<Post> postOpt = postRepository.findById(id);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setIsDeleted(true);
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
            logger.info("Post deleted successfully with ID: {}", id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByUserId(String userId, Pageable pageable) {
        logger.debug("Getting posts by user ID: {}", userId);
        return postRepository.findByUserIdAndIsDeletedFalse(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByFacultyId(String facultyId, Pageable pageable) {
        logger.debug("Getting posts by faculty ID: {}", facultyId);
        return postRepository.findByFacultyIdAndIsDeletedFalse(facultyId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPublicPosts(Pageable pageable) {
        logger.debug("Getting public posts");
        return postRepository.findByVisibilityAndIsDeletedFalse("PUBLIC", pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getTrendingPosts(int limit) {
        logger.debug("Getting trending posts with limit: {}", limit);
        return postRepository.findTopTrendingPosts(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getFeaturedPosts(int limit) {
        logger.debug("Getting featured posts with limit: {}", limit);
        return postRepository.findTopFeaturedPosts(limit);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> searchPosts(String query, Pageable pageable) {
        logger.debug("Searching posts with query: {}", query);
        return postRepository.searchPosts(query, pageable);
    }

    @Override
    public void incrementViewCount(String postId) {
        logger.debug("Incrementing view count for post ID: {}", postId);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.incrementViews();
            postRepository.save(post);
        }
    }

    @Override
    public void updateEngagementMetrics(String postId, int likesCount, int commentsCount, int sharesCount) {
        logger.debug("Updating engagement metrics for post ID: {}", postId);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setLikeCount(likesCount);
            post.setCommentCount(commentsCount);
            post.setShareCount(sharesCount);
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    public void flagPost(String postId, String reason) {
        logger.info("Flagging post with ID: {} for reason: {}", postId, reason);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setIsFlagged(true);
            post.setFlagReason(reason);
            post.setFlaggedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    public void featurePost(String postId, int durationHours) {
        logger.info("Featuring post with ID: {} for {} hours", postId, durationHours);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setIsFeatured(true);
            post.setFeaturedAt(LocalDateTime.now());
            post.setFeaturedUntil(LocalDateTime.now().plusHours(durationHours));
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    public void pinPost(String postId) {
        logger.info("Pinning post with ID: {}", postId);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setIsPinned(true);
            post.setPinnedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    public void unpinPost(String postId) {
        logger.info("Unpinning post with ID: {}", postId);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setIsPinned(false);
            post.setPinnedAt(null);
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    public void moderatePost(String postId, String status, String reason, String moderatorId) {
        logger.info("Moderating post with ID: {} to status: {}", postId, status);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            post.setModerationStatus(status);
            post.setModerationReason(reason);
            post.setModeratorId(moderatorId);
            post.setModeratedAt(LocalDateTime.now());
            post.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByCategory(String category, Pageable pageable) {
        logger.debug("Getting posts by category: {}", category);
        return postRepository.findByCategoryAndIsDeletedFalse(category, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByTags(List<String> tags, Pageable pageable) {
        logger.debug("Getting posts by tags: {}", tags);
        return postRepository.findByTagsInAndIsDeletedFalse(tags, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByStatus(String status, Pageable pageable) {
        logger.debug("Getting posts by status: {}", status);
        return postRepository.findByPublishStatusAndIsDeletedFalse(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsRequiringModeration(Pageable pageable) {
        logger.debug("Getting posts requiring moderation");
        return postRepository.findByModerationStatusAndIsDeletedFalse("PENDING", pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getDraftPosts(String userId, Pageable pageable) {
        logger.debug("Getting draft posts for user ID: {}", userId);
        return postRepository.findByUserIdAndPublishStatusAndIsDeletedFalse(userId, "DRAFT", pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getArchivedPosts(String userId, Pageable pageable) {
        logger.debug("Getting archived posts for user ID: {}", userId);
        return postRepository.findByUserIdAndPublishStatusAndIsDeletedFalse(userId, "ARCHIVED", pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getRelatedPosts(String postId, int limit) {
        logger.debug("Getting related posts for post ID: {} with limit: {}", postId, limit);

        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isPresent()) {
            Post post = postOpt.get();
            return postRepository.findRelatedPosts(post.getTags(), post.getId(), limit);
        }
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getPopularPosts(String timePeriod, int limit) {
        logger.debug("Getting popular posts for time period: {} with limit: {}", timePeriod, limit);
        LocalDateTime startDate = calculateStartDate(timePeriod);
        LocalDateTime endDate = LocalDateTime.now();
        return postRepository.findByCreatedAtBetweenOrderByEngagementScoreDesc(startDate, endDate, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByEngagementScore(double minScore, Pageable pageable) {
        logger.debug("Getting posts by engagement score >= {}", minScore);
        return postRepository.findByEngagementScoreGreaterThanEqualAndIsDeletedFalse(minScore, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByTrendingScore(double minScore, Pageable pageable) {
        logger.debug("Getting posts by trending score >= {}", minScore);
        return postRepository.findByTrendingScoreGreaterThanEqualAndIsDeletedFalse(minScore, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByViralityScore(double minScore, Pageable pageable) {
        logger.debug("Getting posts by virality score >= {}", minScore);
        return postRepository.findByViralityScoreGreaterThanEqualAndIsDeletedFalse(minScore, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsBySentiment(String sentiment, Pageable pageable) {
        logger.debug("Getting posts by sentiment: {}", sentiment);
        return postRepository.findBySentimentAndIsDeletedFalse(sentiment, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByLanguage(String language, Pageable pageable) {
        logger.debug("Getting posts by language: {}", language);
        return postRepository.findByLanguageAndIsDeletedFalse(language, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByAccessLevel(String accessLevel, Pageable pageable) {
        logger.debug("Getting posts by access level: {}", accessLevel);
        return postRepository.findByAccessLevelAndIsDeletedFalse(accessLevel, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByDateRange(String startDate, String endDate, Pageable pageable) {
        logger.debug("Getting posts by date range: {} to {}", startDate, endDate);
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);
        return postRepository.findByCreatedAtBetweenAndIsDeletedFalse(start, end, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByEngagementMetrics(int minViews, int minLikes, int minComments, Pageable pageable) {
        logger.debug("Getting posts by engagement metrics: views>={}, likes>={}, comments>={}", minViews, minLikes,
                minComments);
        return postRepository.findByEngagementMetricsAndIsDeletedFalse(minViews, minLikes, minComments, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByContentFlags(List<String> flags, Pageable pageable) {
        logger.debug("Getting posts by content flags: {}", flags);
        return postRepository.findByContentFlagsInAndIsDeletedFalse(flags, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByCrossReferences(String referenceType, String referenceId, Pageable pageable) {
        logger.debug("Getting posts by cross reference: type={}, id={}", referenceType, referenceId);
        return postRepository.findByCrossReferencesAndIsDeletedFalse(referenceType, referenceId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByVersion(int version, Pageable pageable) {
        logger.debug("Getting posts by version: {}", version);
        return postRepository.findByVersionAndIsDeletedFalse(version, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByChangeHistory(String changeType, String changeValue, Pageable pageable) {
        logger.debug("Getting posts by change history: type={}, value={}", changeType, changeValue);
        return postRepository.findByChangeHistoryAndIsDeletedFalse(changeType, changeValue, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByCustomFields(String fieldName, String fieldValue, Pageable pageable) {
        logger.debug("Getting posts by custom field: {}={}", fieldName, fieldValue);
        return postRepository.findByCustomFieldsAndIsDeletedFalse(fieldName, fieldValue, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByAnalyticsData(String metricName, String metricValue, Pageable pageable) {
        logger.debug("Getting posts by analytics data: {}={}", metricName, metricValue);
        return postRepository.findByAnalyticsDataAndIsDeletedFalse(metricName, metricValue, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsBySeoData(String seoField, String seoValue, Pageable pageable) {
        logger.debug("Getting posts by SEO data: {}={}", seoField, seoValue);
        return postRepository.findBySeoDataAndIsDeletedFalse(seoField, seoValue, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByModerationData(String moderationField, String moderationValue, Pageable pageable) {
        logger.debug("Getting posts by moderation data: {}={}", moderationField, moderationValue);
        return postRepository.findByModerationDataAndIsDeletedFalse(moderationField, moderationValue, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> getPostsByContentAnalysis(String analysisField, String analysisValue, Pageable pageable) {
        logger.debug("Getting posts by content analysis: {}={}", analysisField, analysisValue);
        return postRepository.findByContentAnalysisAndIsDeletedFalse(analysisField, analysisValue, pageable);
    }

    /**
     * Calculate start date based on time period
     */
    private LocalDateTime calculateStartDate(String timePeriod) {
        LocalDateTime now = LocalDateTime.now();
        return switch (timePeriod.toUpperCase()) {
            case "TODAY" -> now.toLocalDate().atStartOfDay();
            case "WEEK" -> now.minusWeeks(1);
            case "MONTH" -> now.minusMonths(1);
            case "QUARTER" -> now.minusMonths(3);
            case "YEAR" -> now.minusYears(1);
            default -> now.minusDays(7); // Default to last week
        };
    }
}
