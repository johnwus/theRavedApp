package com.raved.social.repository;

import com.raved.social.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Comment entities
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    
    Page<Comment> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    Page<Comment> findByParentCommentIdOrderByCreatedAtDesc(Long parentCommentId, Pageable pageable);
    
    Page<Comment> findByPostIdAndParentCommentIdIsNullOrderByCreatedAtDesc(Long postId, Pageable pageable);
    
    List<Comment> findByPostIdAndParentCommentIdIsNullOrderByCreatedAtDesc(Long postId);
    
    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(Long parentCommentId);
    
    long countByPostId(Long postId);
    
    long countByParentCommentId(Long parentCommentId);
    
    long countByUserId(Long userId);
    
    long countByPostIdAndCreatedAtAfter(Long postId, LocalDateTime since);
    
    @Query("SELECT c FROM Comment c WHERE c.postId = :postId AND c.content LIKE %:keyword%")
    List<Comment> findByPostIdAndContentContaining(@Param("postId") Long postId, @Param("keyword") String keyword);
    
    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true, c.content = '[Deleted]', c.updatedAt = CURRENT_TIMESTAMP WHERE c.userId = :userId")
    int softDeleteByUserId(@Param("userId") Long userId);
    
    @Modifying
    @Query("UPDATE Comment c SET c.isDeleted = true, c.content = '[Deleted]', c.updatedAt = CURRENT_TIMESTAMP WHERE c.id = :commentId")
    int softDeleteById(@Param("commentId") Long commentId);
    
    @Modifying
    @Query("UPDATE Comment c SET c.likesCount = c.likesCount + 1 WHERE c.id = :commentId")
    int incrementLikesCount(@Param("commentId") Long commentId);
    
    @Modifying
    @Query("UPDATE Comment c SET c.likesCount = GREATEST(0, c.likesCount - 1) WHERE c.id = :commentId")
    int decrementLikesCount(@Param("commentId") Long commentId);
    
    @Modifying
    @Query("UPDATE Comment c SET c.isFlagged = true, c.updatedAt = CURRENT_TIMESTAMP WHERE c.id = :commentId")
    int flagComment(@Param("commentId") Long commentId, @Param("reason") String reason);
}