package com.raved.social.repository;

import com.raved.social.model.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Like MongoDB documents
 */
@Repository
public interface LikeRepository extends MongoRepository<Like, String> {
    
    // Core methods using contentId and contentType
    Optional<Like> findByUserIdAndContentIdAndContentType(String userId, String contentId, String contentType);
    
    boolean existsByUserIdAndContentIdAndContentType(String userId, String contentId, String contentType);
    
    Page<Like> findByContentIdAndContentTypeAndIsRemovedFalseOrderByCreatedAtDesc(String contentId, String contentType, Pageable pageable);
    
    Page<Like> findByUserIdAndIsRemovedFalseOrderByCreatedAtDesc(String userId, Pageable pageable);
    
    long countByContentIdAndContentTypeAndIsRemovedFalse(String contentId, String contentType);
    
    long countByUserIdAndIsRemovedFalse(String userId);
    
    // Enhanced MongoDB-specific methods
    @Query("{'contentId': ?0, 'contentType': ?1, 'isRemoved': false}")
    List<Like> findByContentIdAndContentType(String contentId, String contentType);
    
    @Query("{'userId': ?0, 'isRemoved': false}")
    List<Like> findByUserId(String userId);
    
    @Query("{'contentId': ?0, 'contentType': ?1, 'isRemoved': false}")
    List<Like> findActiveLikesByContent(String contentId, String contentType);
    
    @Query("{'userId': ?0, 'likeType': ?1, 'isRemoved': false}")
    List<Like> findByUserIdAndLikeType(String userId, String likeType);

    @Query("{'contentId': ?0, 'contentType': ?1, 'likeType': ?2, 'isRemoved': false}")
    List<Like> findByContentIdAndContentTypeAndLikeType(String contentId, String contentType, String likeType);

    @Query("{'contentId': ?0, 'contentType': ?1, 'isRemoved': false}")
    long countActiveLikesByContent(String contentId, String contentType);

    @Query("{'userId': ?0, 'isRemoved': false}")
    long countActiveLikesByUser(String userId);

    @Query("{'contentId': ?0, 'contentType': ?1, 'isRemoved': false, 'createdAt': {$gte: ?2}}")
    List<Like> findRecentLikesByContent(String contentId, String contentType, java.time.LocalDateTime since);

    @Query("{'userId': ?0, 'isRemoved': false, 'createdAt': {$gte: ?1}}")
    List<Like> findRecentLikesByUser(String userId, java.time.LocalDateTime since);
    
    @Query("{'isRemoved': false, 'createdAt': {$gte: ?0}}")
    List<Like> findRecentLikes(java.time.LocalDateTime since);
    
    @Query("{'isRemoved': false, 'likeType': ?0}")
    List<Like> findByLikeType(String likeType);
    
    @Query("{'isRemoved': false, 'likeSource': ?0}")
    List<Like> findByLikeSource(String likeSource);
    
    @Query("{'isRemoved': false, 'isAnonymous': true}")
    List<Like> findAnonymousLikes();
    
    // Soft delete methods
    @Query("{'userId': ?0, 'contentId': ?1, 'contentType': ?2}")
    Optional<Like> findByUserIdAndContentIdAndContentTypeForUpdate(String userId, String contentId, String contentType);
    
    void deleteByUserIdAndContentIdAndContentType(String userId, String contentId, String contentType);

    void deleteByContentIdAndContentType(String contentId, String contentType);

    // Methods for backward compatibility with service layer using targetId/targetType
    boolean existsByUserIdAndTargetIdAndTargetType(String userId, String targetId, Like.TargetType targetType);

    Optional<Like> findByUserIdAndTargetIdAndTargetType(String userId, String targetId, Like.TargetType targetType);

    Page<Like> findByTargetIdAndTargetTypeOrderByCreatedAtDesc(String targetId, Like.TargetType targetType, Pageable pageable);

    Page<Like> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

    long countByTargetIdAndTargetType(String targetId, Like.TargetType targetType);

    @Query("{'userId': ?0, 'createdAt': {$gte: ?1}, 'isRemoved': false}")
    List<Like> findRecentLikesForUserPosts(String userId, int limit);
}
