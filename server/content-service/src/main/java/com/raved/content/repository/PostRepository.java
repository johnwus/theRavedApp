package com.raved.content.repository;

import com.raved.content.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PostRepository for TheRavedApp
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * Find post by ID and not deleted
     */
    Optional<Post> findByIdAndIsDeletedFalse(Long id);

    /**
     * Find posts by user ID
     */
    Page<Post> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find posts by faculty ID
     */
    Page<Post> findByFacultyIdAndIsDeletedFalseOrderByCreatedAtDesc(Long facultyId, Pageable pageable);

    /**
     * Find posts by visibility
     */
    Page<Post> findByVisibilityAndIsDeletedFalseOrderByCreatedAtDesc(Post.Visibility visibility, Pageable pageable);

    /**
     * Find featured posts
     */
    List<Post> findByIsFeaturedTrueAndIsDeletedFalseAndFeaturedUntilAfterOrderByCreatedAtDesc(LocalDateTime now);

    /**
     * Find trending posts based on engagement
     */
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.moderationStatus = 'APPROVED' " +
           "ORDER BY (p.likesCount * 3 + p.commentsCount * 2 + p.sharesCount * 5 + p.viewsCount * 0.1) DESC")
    List<Post> findTrendingPosts(@Param("limit") int limit);

    /**
     * Search posts by content
     */
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.moderationStatus = 'APPROVED' " +
           "AND LOWER(p.content) LIKE LOWER(CONCAT('%', :query, '%')) " +
           "ORDER BY p.createdAt DESC")
    Page<Post> searchPosts(@Param("query") String query, Pageable pageable);

    /**
     * Find posts by moderation status
     */
    List<Post> findByModerationStatusAndIsDeletedFalse(Post.ModerationStatus status);

    /**
     * Find flagged posts
     */
    List<Post> findByIsFlaggedTrueAndIsDeletedFalse();

    /**
     * Count posts by user
     */
    long countByUserIdAndIsDeletedFalse(Long userId);

    /**
     * Find recent posts for feed algorithm
     */
    @Query("SELECT p FROM Post p WHERE p.isDeleted = false AND p.moderationStatus = 'APPROVED' " +
           "AND p.visibility IN ('PUBLIC', 'FACULTY_ONLY') AND p.createdAt >= :since " +
           "ORDER BY p.createdAt DESC")
    List<Post> findRecentPostsForFeed(@Param("since") LocalDateTime since, Pageable pageable);
}
