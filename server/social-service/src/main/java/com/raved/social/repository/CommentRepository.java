package com.raved.social.repository;

import com.raved.social.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * CommentRepository for TheRavedApp
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Find comment by ID and not deleted
     */
    Optional<Comment> findByIdAndIsDeletedFalse(Long id);

    /**
     * Find comments by post ID
     */
    Page<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtAsc(Long postId, Pageable pageable);

    /**
     * Find comments by user ID
     */
    Page<Comment> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Find replies to a comment
     */
    List<Comment> findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(Long parentCommentId);

    /**
     * Count comments for a post
     */
    long countByPostIdAndIsDeletedFalse(Long postId);

    /**
     * Find recent comments for user's posts
     */
    @Query("SELECT c FROM Comment c WHERE c.postId IN " +
           "(SELECT p.id FROM Post p WHERE p.userId = :userId) " +
           "AND c.isDeleted = false ORDER BY c.createdAt DESC")
    List<Comment> findRecentCommentsForUserPosts(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Check if user has liked a comment (placeholder - would need CommentLike entity)
     */
    @Query("SELECT CASE WHEN COUNT(cl) > 0 THEN true ELSE false END " +
           "FROM CommentLike cl WHERE cl.commentId = :commentId AND cl.userId = :userId")
    boolean hasUserLikedComment(@Param("commentId") Long commentId, @Param("userId") Long userId);

    /**
     * Find flagged comments
     */
    List<Comment> findByIsFlaggedTrueAndIsDeletedFalse();

    /**
     * Find top-level comments (no parent)
     */
    Page<Comment> findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtAsc(Long postId, Pageable pageable);

    /**
     * Delete comments by post ID
     */
    void deleteByPostId(Long postId);

    /**
     * Find comments by user IDs
     */
    List<Comment> findByUserIdInAndIsDeletedFalse(List<Long> userIds);
}
