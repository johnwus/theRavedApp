package com.raved.analytics.repository;

import com.raved.analytics.model.ContentMetrics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContentMetricsRepository extends JpaRepository<ContentMetrics, Long> {
    
    Optional<ContentMetrics> findByContentId(Long contentId);
    
    @Query("SELECT c FROM ContentMetrics c ORDER BY c.engagementScore DESC")
    Page<ContentMetrics> findTopByEngagementScore(Pageable pageable);
    
    @Query("SELECT SUM(c.viewCount) FROM ContentMetrics c")
    long getTotalViews();
    
    @Query("SELECT SUM(c.likeCount) FROM ContentMetrics c")
    long getTotalLikes();
    
    @Query("SELECT SUM(c.commentCount) FROM ContentMetrics c")
    long getTotalComments();
    
    @Query("SELECT SUM(c.shareCount) FROM ContentMetrics c")
    long getTotalShares();
    
    @Query("SELECT c.contentId, c.viewCount, c.likeCount, c.commentCount, c.shareCount, c.createdAt FROM ContentMetrics c WHERE c.createdAt >= :since ORDER BY c.engagementScore DESC")
    List<Object[]> getContentWithMetrics(@Param("since") LocalDateTime since, int limit);
    
    @Query("SELECT c.contentId, c.viewCount, c.likeCount, c.commentCount, c.shareCount, c.createdAt FROM ContentMetrics c WHERE c.createdAt BETWEEN :startDate AND :endDate ORDER BY c.engagementScore DESC")
    List<Object[]> getContentWithMetrics(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, int limit);
    
    @Query("SELECT c.contentId, c.viewCount, c.likeCount, c.commentCount, c.shareCount, c.createdAt FROM ContentMetrics c WHERE c.category = :category AND c.createdAt >= :since ORDER BY c.engagementScore DESC")
    List<Object[]> getContentWithMetricsByCategory(@Param("category") String category, @Param("since") LocalDateTime since, int limit);
    
    @Query("SELECT COUNT(c) FROM ContentMetrics c WHERE c.engagementScore > 10 AND c.createdAt >= :since")
    long countTrendingContent(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(c) FROM ContentMetrics c WHERE c.createdAt >= :since")
    long countContentCreatedAfter(@Param("since") LocalDateTime since);
    
    @Query("SELECT c.category, COUNT(c) FROM ContentMetrics c WHERE c.createdAt >= :since GROUP BY c.category ORDER BY COUNT(c) DESC")
    List<Object[]> getTopTrendingCategories(@Param("since") LocalDateTime since, int limit);
    
    @Query("SELECT c.contentId, c.viewCount, c.likeCount, c.commentCount, c.shareCount, c.createdAt FROM ContentMetrics c WHERE c.engagementScore > 5 AND c.createdAt >= :since ORDER BY c.engagementScore DESC")
    List<Object[]> getHighEngagementContent(@Param("since") LocalDateTime since, int limit);
    
    @Query("SELECT c.contentId, c.createdAt, c.updatedAt FROM ContentMetrics c WHERE c.engagementScore > 10 AND c.createdAt >= :since")
    List<Object[]> getTrendingContentWithCreationTime(@Param("since") LocalDateTime since);
    
    @Query("UPDATE ContentMetrics c SET c.engagementScore = :score WHERE c.contentId = :contentId")
    void updateTrendingScore(@Param("contentId") Long contentId, @Param("score") double score);
    
    @Query("SELECT COUNT(c) FROM ContentMetrics c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long countContentCreatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(c.viewCount) FROM ContentMetrics c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long getTotalViewsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(c.likeCount) FROM ContentMetrics c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long getTotalLikesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(c.commentCount) FROM ContentMetrics c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long getTotalCommentsBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(c.shareCount) FROM ContentMetrics c WHERE c.createdAt BETWEEN :startDate AND :endDate")
    long getTotalSharesBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT c.contentId, c.engagementScore, c.viewCount, c.likeCount, c.commentCount FROM ContentMetrics c ORDER BY c.engagementScore DESC")
    List<Object[]> getTopContentByEngagement(int limit);
}
