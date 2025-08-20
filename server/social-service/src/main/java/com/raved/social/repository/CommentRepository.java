package com.raved.social.repository;

import com.raved.social.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Comment MongoDB documents
 */
@Repository
public interface CommentRepository extends MongoRepository<Comment, String>, CommentRepositoryCustom {

    Page<Comment> findByPostIdAndIsDeletedFalseOrderByCreatedAtDesc(String postId, Pageable pageable);

    Page<Comment> findByUserIdAndIsDeletedFalseOrderByCreatedAtDesc(String userId, Pageable pageable);

    Page<Comment> findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtDesc(String parentCommentId, Pageable pageable);

    Page<Comment> findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(String postId, Pageable pageable);

    List<Comment> findByPostIdAndParentCommentIdIsNullAndIsDeletedFalseOrderByCreatedAtDesc(String postId);

    List<Comment> findByParentCommentIdAndIsDeletedFalseOrderByCreatedAtAsc(String parentCommentId);

    long countByPostIdAndIsDeletedFalse(String postId);

    long countByParentCommentIdAndIsDeletedFalse(String parentCommentId);

    long countByUserIdAndIsDeletedFalse(String userId);

    long countByPostIdAndCreatedAtAfterAndIsDeletedFalse(String postId, LocalDateTime since);

    @Query("{'postId': ?0, 'content': {$regex: ?1, $options: 'i'}, 'isDeleted': false}")
    List<Comment> findByPostIdAndContentContaining(String postId, String keyword);

    @Query("{'userId': ?0, 'isDeleted': false}")
    List<Comment> findByUserIdAndIsDeletedFalse(String userId);

    @Query("{'postId': ?0, 'isDeleted': false}")
    List<Comment> findByPostIdAndIsDeletedFalse(String postId);
    
    @Query("{'parentCommentId': ?0, 'isDeleted': false}")
    List<Comment> findByParentCommentIdAndIsDeletedFalse(String parentCommentId);
    
    @Query("{'isFlagged': true, 'isDeleted': false}")
    List<Comment> findFlaggedComments();
    
    @Query("{'moderationStatus': ?0, 'isDeleted': false}")
    List<Comment> findByModerationStatus(String status);
    
    @Query("{'sentiment': ?0, 'isDeleted': false}")
    List<Comment> findBySentiment(String sentiment);
    
    @Query("{'language': ?0, 'isDeleted': false}")
    List<Comment> findByLanguage(String language);
    
    @Query("{'reportCount': {$gte: ?0}, 'isDeleted': false}")
    List<Comment> findByReportCountGreaterThanEqual(int reportCount);
    
    @Query("{'createdAt': {$gte: ?0}, 'isDeleted': false}")
    List<Comment> findByCreatedAtAfter(LocalDateTime since);
    
    @Query("{'updatedAt': {$gte: ?0}, 'isDeleted': false}")
    List<Comment> findByUpdatedAtAfter(LocalDateTime since);

    // Methods for backward compatibility with service layer

    Page<Comment> findByPostIdAndParentCommentIdIsNullOrderByCreatedAtDesc(String postId, Pageable pageable);

    Page<Comment> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    List<Comment> findByParentCommentIdOrderByCreatedAtAsc(String parentCommentId);

    void incrementLikesCount(String commentId);

    void decrementLikesCount(String commentId);

    void flagComment(String commentId, String reason);
}