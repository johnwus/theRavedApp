package com.raved.content.repository;

import com.raved.content.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PostRepository for TheRavedApp MongoDB
 */
@Repository
public interface PostRepository extends MongoRepository<Post, String> {

       /**
        * Find post by ID and not deleted
        */
       Optional<Post> findByIdAndIsDeletedFalse(String id);

       /**
        * Find posts by user ID
        */
       Page<Post> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(String userId, Pageable pageable);

       /**
        * Find posts by faculty ID
        */
       Page<Post> findByFacultyIdAndIsDeletedFalseOrderByCreatedAtDesc(String facultyId, Pageable pageable);

       /**
        * Find posts by visibility
        */
       Page<Post> findByVisibilityAndIsDeletedFalseOrderByCreatedAtDesc(String visibility, Pageable pageable);

       /**
        * Find featured posts
        */
       List<Post> findByIsFeaturedTrueAndIsDeletedFalseAndFeaturedUntilAfterOrderByCreatedAtDesc(LocalDateTime now);

       /**
        * Find trending posts based on engagement
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED'}")
       List<Post> findTopTrendingPosts(int limit);

       /**
        * Find trending posts with pagination
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED'}")
       Page<Post> findTrendingPosts(Pageable pageable);

       /**
        * Find trending posts since a specific time
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'createdAt': {$gte: ?0}}")
       Page<Post> findTrendingPostsSince(LocalDateTime since, Pageable pageable);

    /**
     * Find posts by IDs ordered by creation date
     */
    @Query("{'_id': {$in: ?0}, 'isDeleted': false, 'moderationStatus': 'APPROVED'}")
    Page<Post> findByIdInOrderByCreatedAtDesc(List<String> ids, Pageable pageable);

       /**
        * Find posts by author IDs ordered by creation date
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'userId': {$in: ?0}}")
       Page<Post> findByAuthorIdInOrderByCreatedAtDesc(List<String> authorIds, Pageable pageable);

       /**
        * Find posts by author IDs not in list, ordered by creation date
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'userId': {$nin: ?0}}")
       Page<Post> findByAuthorIdNotInOrderByCreatedAtDesc(List<String> authorIds, Pageable pageable);

       /**
        * Find posts by university ID
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'universityId': ?0}")
       Page<Post> findByUniversityIdOrderByCreatedAtDesc(String universityId, Pageable pageable);

       /**
        * Find posts by faculty ID
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'facultyId': ?0}")
       Page<Post> findByFacultyIdOrderByCreatedAtDesc(String facultyId, Pageable pageable);

       /**
        * Find posts by hashtag
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'content': {$regex: ?0, $options: 'i'}}")
       Page<Post> findByHashtagOrderByCreatedAtDesc(String hashtag, Pageable pageable);

       /**
        * Find popular posts
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED'}")
       Page<Post> findPopularPosts(Pageable pageable);

       /**
        * Search posts by content
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'content': {$regex: ?0, $options: 'i'}}")
       Page<Post> searchPosts(String query, Pageable pageable);

       /**
        * Find posts by moderation status
        */
       List<Post> findByModerationStatusAndIsDeletedFalse(String status);

       /**
        * Find flagged posts
        */
       List<Post> findByIsFlaggedTrueAndIsDeletedFalse();

       /**
        * Count posts by user
        */
       long countByUserIdAndIsDeletedFalse(String userId);

       /**
        * Find recent posts for feed algorithm
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'visibility': {$in: ['PUBLIC', 'FACULTY_ONLY']}, 'createdAt': {$gte: ?0}}")
       List<Post> findRecentPostsForFeed(LocalDateTime since, Pageable pageable);

       // Additional MongoDB-specific methods for the service implementation

       /**
        * Find posts by user ID and not deleted
        */
       Page<Post> findByUserIdAndIsDeletedFalse(String userId, Pageable pageable);

       /**
        * Find posts by faculty ID and not deleted
        */
       Page<Post> findByFacultyIdAndIsDeletedFalse(String facultyId, Pageable pageable);

       /**
        * Find posts by category and not deleted
        */
       Page<Post> findByCategoryAndIsDeletedFalse(String category, Pageable pageable);

       /**
        * Find posts by tags and not deleted
        */
       @Query("{'isDeleted': false, 'tags': {$in: ?0}}")
       Page<Post> findByTagsInAndIsDeletedFalse(List<String> tags, Pageable pageable);

       /**
        * Find posts by publish status and not deleted
        */
       Page<Post> findByPublishStatusAndIsDeletedFalse(String status, Pageable pageable);

       /**
        * Find posts by moderation status and not deleted
        */
       Page<Post> findByModerationStatusAndIsDeletedFalse(String status, Pageable pageable);

       /**
        * Find posts by user ID, publish status and not deleted
        */
       Page<Post> findByUserIdAndPublishStatusAndIsDeletedFalse(String userId, String status, Pageable pageable);

       /**
        * Find related posts by tags
        */
       @Query("{'isDeleted': false, 'tags': {$in: ?0}, '_id': {$ne: ?1}}")
       List<Post> findRelatedPosts(List<String> tags, String excludePostId, int limit);

       /**
        * Find posts by date range ordered by engagement score
        */
       @Query("{'isDeleted': false, 'createdAt': {$gte: ?0, $lte: ?1}}")
       List<Post> findByCreatedAtBetweenOrderByEngagementScoreDesc(LocalDateTime startDate, LocalDateTime endDate,
                     int limit);

       /**
        * Find posts by engagement score greater than or equal
        */
       @Query("{'isDeleted': false, 'engagementScore': {$gte: ?0}}")
       Page<Post> findByEngagementScoreGreaterThanEqualAndIsDeletedFalse(double minScore, Pageable pageable);

       /**
        * Find posts by trending score greater than or equal
        */
       @Query("{'isDeleted': false, 'trendingScore': {$gte: ?0}}")
       Page<Post> findByTrendingScoreGreaterThanEqualAndIsDeletedFalse(double minScore, Pageable pageable);

       /**
        * Find posts by virality score greater than or equal
        */
       @Query("{'isDeleted': false, 'viralityScore': {$gte: ?0}}")
       Page<Post> findByViralityScoreGreaterThanEqualAndIsDeletedFalse(double minScore, Pageable pageable);

       /**
        * Find posts by sentiment and not deleted
        */
       Page<Post> findBySentimentAndIsDeletedFalse(String sentiment, Pageable pageable);

       /**
        * Find posts by language and not deleted
        */
       Page<Post> findByLanguageAndIsDeletedFalse(String language, Pageable pageable);

       /**
        * Find posts by access level and not deleted
        */
       Page<Post> findByAccessLevelAndIsDeletedFalse(String accessLevel, Pageable pageable);

       /**
        * Find posts by date range and not deleted
        */
       @Query("{'isDeleted': false, 'createdAt': {$gte: ?0, $lte: ?1}}")
       Page<Post> findByCreatedAtBetweenAndIsDeletedFalse(LocalDateTime startDate, LocalDateTime endDate,
                     Pageable pageable);

       /**
        * Find posts by engagement metrics and not deleted
        */
       @Query("{'isDeleted': false, 'viewsCount': {$gte: ?0}, 'likesCount': {$gte: ?1}, 'commentsCount': {$gte: ?2}}")
       Page<Post> findByEngagementMetricsAndIsDeletedFalse(int minViews, int minLikes, int minComments,
                     Pageable pageable);

       /**
        * Find posts by content flags and not deleted
        */
       @Query("{'isDeleted': false, 'contentFlags': {$in: ?0}}")
       Page<Post> findByContentFlagsInAndIsDeletedFalse(List<String> flags, Pageable pageable);

       /**
        * Find posts by cross references and not deleted
        */
       @Query("{'isDeleted': false, 'crossReferences': {$elemMatch: {'type': ?0, 'id': ?1}}}")
       Page<Post> findByCrossReferencesAndIsDeletedFalse(String referenceType, String referenceId, Pageable pageable);

       /**
        * Find posts by version and not deleted
        */
       Page<Post> findByVersionAndIsDeletedFalse(int version, Pageable pageable);

       /**
        * Find posts by change history and not deleted
        */
       @Query("{'isDeleted': false, 'changeHistory': {$elemMatch: {'type': ?0, 'value': ?1}}}")
       Page<Post> findByChangeHistoryAndIsDeletedFalse(String changeType, String changeValue, Pageable pageable);

       /**
        * Find posts by custom fields and not deleted
        */
       @Query("{'isDeleted': false, 'customFields.?0': ?1}")
       Page<Post> findByCustomFieldsAndIsDeletedFalse(String fieldName, String fieldValue, Pageable pageable);

       /**
        * Find posts by analytics data and not deleted
        */
       @Query("{'isDeleted': false, 'analytics.?0': ?1}")
       Page<Post> findByAnalyticsDataAndIsDeletedFalse(String metricName, String metricValue, Pageable pageable);

       /**
        * Find posts by SEO data and not deleted
        */
       @Query("{'isDeleted': false, 'seoTitle': {$regex: ?0, $options: 'i'}}")
       Page<Post> findBySeoDataAndIsDeletedFalse(String seoField, String seoValue, Pageable pageable);

       /**
        * Find posts by moderation data and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': ?0, 'moderationReason': {$regex: ?1, $options: 'i'}}")
       Page<Post> findByModerationDataAndIsDeletedFalse(String moderationField, String moderationValue,
                     Pageable pageable);

       /**
        * Find posts by content analysis and not deleted
        */
       @Query("{'isDeleted': false, 'sentiment': ?0, 'language': ?1}")
       Page<Post> findByContentAnalysisAndIsDeletedFalse(String analysisField, String analysisValue, Pageable pageable);

       /**
        * Find posts by visibility and not deleted
        */
       Page<Post> findByVisibilityAndIsDeletedFalse(String visibility, Pageable pageable);

       /**
        * Find top featured posts with limit
        */
       List<Post> findTopFeaturedPosts(int limit);

       /**
        * Find posts by university ID and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'universityId': ?0}")
       Page<Post> findByUniversityIdAndIsDeletedFalseOrderByCreatedAtDesc(String universityId, Pageable pageable);

       /**
        * Find posts by tags containing and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'tags': {$regex: ?0, $options: 'i'}}")
       Page<Post> findByTagsContainingAndIsDeletedFalseOrderByCreatedAtDesc(String tag, Pageable pageable);

       /**
        * Find posts by category and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'category': ?0}")
       Page<Post> findByCategoryAndIsDeletedFalseOrderByCreatedAtDesc(String category, Pageable pageable);

       /**
        * Find posts by language and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'language': ?0}")
       Page<Post> findByLanguageAndIsDeletedFalseOrderByCreatedAtDesc(String language, Pageable pageable);

       /**
        * Find posts by sentiment and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'sentiment': ?0}")
       Page<Post> findBySentimentAndIsDeletedFalseOrderByCreatedAtDesc(String sentiment, Pageable pageable);

       /**
        * Find posts by engagement score greater than or equal and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'engagementScore': {$gte: ?0}}")
       Page<Post> findByEngagementScoreGreaterThanEqualAndIsDeletedFalseOrderByEngagementScoreDesc(double minScore,
                     Pageable pageable);

       /**
        * Find posts by trending score greater than or equal and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'trendingScore': {$gte: ?0}}")
       Page<Post> findByTrendingScoreGreaterThanEqualAndIsDeletedFalseOrderByTrendingScoreDesc(double minScore,
                     Pageable pageable);

       /**
        * Find posts by virality score greater than or equal and not deleted
        */
       @Query("{'isDeleted': false, 'moderationStatus': 'APPROVED', 'viralityScore': {$gte: ?0}}")
       Page<Post> findByViralityScoreGreaterThanEqualAndIsDeletedFalseOrderByViralityScoreDesc(double minScore,
                     Pageable pageable);

       /**
        * Find posts by publish status and scheduled at before, ordered by scheduled at
        */
       @Query("{'publishStatus': ?0, 'scheduledAt': {$lte: ?1}}")
       Page<Post> findByPublishStatusAndScheduledAtBeforeOrderByScheduledAtAsc(String publishStatus,
                     LocalDateTime before, Pageable pageable);

       /**
        * Find posts by is featured true and not deleted, ordered by featured at
        */
       @Query("{'isFeatured': true, 'isDeleted': false}")
       Page<Post> findByIsFeaturedTrueAndIsDeletedFalseOrderByFeaturedAtDesc(Pageable pageable);

       /**
        * Find posts by is pinned true and not deleted, ordered by pinned at
        */
       @Query("{'isPinned': true, 'isDeleted': false}")
       Page<Post> findByIsPinnedTrueAndIsDeletedFalseOrderByPinnedAtDesc(Pageable pageable);

       /**
        * Find posts by is deleted false, ordered by created at
        */
       Page<Post> findByIsDeletedFalseOrderByCreatedAtDesc(Pageable pageable);

       /**
        * Find posts by is deleted false, ordered by engagement score
        */
       Page<Post> findByIsDeletedFalseOrderByEngagementScoreDesc(Pageable pageable);

       /**
        * Find posts by moderation status with pagination
        */
       Page<Post> findByModerationStatus(String status, Pageable pageable);

       /**
        * Find flagged posts
        */
       List<Post> findByIsFlaggedTrue();

       /**
        * Count posts by moderation status
        */
       long countByModerationStatus(String status);

       /**
        * Count flagged posts
        */
       long countByIsFlaggedTrue();
}
