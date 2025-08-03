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
 * LikeRepository for TheRavedApp
 */
@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    /**
     * Check if user has liked a post
     */
    boolean existsByUserIdAndPostId(Long userId, Long postId);

    /**
     * Find like by user and post
     */
    Optional<Like> findByUserIdAndPostId(Long userId, Long postId);

    /**
     * Find likes for a post
     */
    Page<Like> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);

    /**
     * Find likes by user
     */
    Page<Like> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);

    /**
     * Count likes for a post
     */
    long countByPostId(Long postId);

    /**
     * Find recent likes for user's posts
     */
    @Query("SELECT l FROM Like l WHERE l.postId IN " +
           "(SELECT p.id FROM Post p WHERE p.userId = :userId) " +
           "ORDER BY l.createdAt DESC")
    List<Like> findRecentLikesForUserPosts(@Param("userId") Long userId, @Param("limit") int limit);

    /**
     * Delete likes by post ID
     */
    void deleteByPostId(Long postId);

    /**
     * Find likes by user IDs
     */
    List<Like> findByUserIdIn(List<Long> userIds);
}
