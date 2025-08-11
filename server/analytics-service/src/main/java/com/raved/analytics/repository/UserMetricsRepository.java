package com.raved.analytics.repository;

import com.raved.analytics.model.UserMetrics;
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
public interface UserMetricsRepository extends JpaRepository<UserMetrics, Long> {
    
    Optional<UserMetrics> findByUserId(Long userId);
    
    @Query("SELECT u FROM UserMetrics u ORDER BY u.engagementRate DESC")
    Page<UserMetrics> findTopByEngagementRate(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM UserMetrics u WHERE u.lastActiveAt >= :since")
    long countActiveUsers(@Param("since") LocalDateTime since);
    
    @Query("SELECT AVG(u.engagementRate) FROM UserMetrics u")
    double getAverageEngagementRate();
    
    @Query("SELECT COUNT(u) FROM UserMetrics u WHERE u.lastActiveAt BETWEEN :startDate AND :endDate")
    long countActiveUsersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(u) FROM UserMetrics u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countNewUsersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT AVG(u.engagementRate) FROM UserMetrics u WHERE u.lastActiveAt BETWEEN :startDate AND :endDate")
    double getAverageEngagementRateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT u.userId, u.engagementRate, u.totalPosts, u.totalLikes FROM UserMetrics u ORDER BY u.engagementRate DESC")
    List<Object[]> getTopUsersByEngagement(Pageable pageable);
}
