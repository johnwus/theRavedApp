package com.raved.social.repository;

import com.raved.social.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Like entities
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    
    // Generic methods using target_id and target_type
    Optional<Like> findByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, Like.TargetType targetType);
    
    boolean existsByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, Like.TargetType targetType);
    
    Page<Like> findByTargetIdAndTargetTypeOrderByCreatedAtDesc(Long targetId, Like.TargetType targetType, Pageable pageable);
    
    Page<Like> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    long countByTargetIdAndTargetType(Long targetId, Like.TargetType targetType);
    
    long countByUserId(Long userId);
    
    @Query(value = "SELECT l.* FROM likes l JOIN posts p ON l.target_id = p.id WHERE l.target_type = 'POST' AND p.user_id = :userId ORDER BY l.created_at DESC LIMIT :limit", nativeQuery = true)
    List<Like> findRecentLikesForUserPosts(@Param("userId") Long userId, @Param("limit") int limit);
    
    @Query("SELECT COUNT(l) > 0 FROM Like l WHERE l.targetId = :targetId AND l.targetType = :targetType AND l.userId IN :userIds")
    boolean isLikedByAnyUser(@Param("targetId") Long targetId, @Param("targetType") Like.TargetType targetType, @Param("userIds") List<Long> userIds);
    
    @Query("SELECT l.userId FROM Like l WHERE l.targetId = :targetId AND l.targetType = :targetType")
    List<Long> findUserIdsByTargetIdAndTargetType(@Param("targetId") Long targetId, @Param("targetType") Like.TargetType targetType);
    
    void deleteByUserIdAndTargetIdAndTargetType(Long userId, Long targetId, Like.TargetType targetType);
    
    void deleteByTargetIdAndTargetType(Long targetId, Like.TargetType targetType);
    
    // Legacy methods for backward compatibility (deprecated)
    @Deprecated
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);
    
    @Deprecated
    Optional<Like> findByUserIdAndCommentId(Long userId, Long commentId);
    
    @Deprecated
    boolean existsByUserIdAndPostId(Long userId, Long postId);
    
    @Deprecated
    boolean existsByUserIdAndCommentId(Long userId, Long commentId);
    
    @Deprecated
    Page<Like> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    
    @Deprecated
    Page<Like> findByCommentIdOrderByCreatedAtDesc(Long commentId, Pageable pageable);
    
    @Deprecated
    long countByPostId(Long postId);
    
    @Deprecated
    long countByCommentId(Long commentId);
    
    @Deprecated
    void deleteByUserIdAndPostId(Long userId, Long postId);
    
    @Deprecated
    void deleteByUserIdAndCommentId(Long userId, Long commentId);
    
    @Deprecated
    void deleteByPostId(Long postId);
    
    @Deprecated
    void deleteByCommentId(Long commentId);
}