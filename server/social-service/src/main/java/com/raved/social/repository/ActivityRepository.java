package com.raved.social.repository;

import com.raved.social.model.Activity;
import com.raved.social.model.ActivityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Activity entities
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    Page<Activity> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    
    Page<Activity> findByUserIdAndActivityTypeOrderByCreatedAtDesc(Long userId, ActivityType activityType, Pageable pageable);
    
    Page<Activity> findByTargetIdAndTargetTypeOrderByCreatedAtDesc(Long targetId, String targetType, Pageable pageable);
    
    @Query(value = "SELECT a FROM Activity a WHERE a.userId = :userId ORDER BY a.createdAt DESC LIMIT :limit")
    List<Activity> findTopByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId, @Param("limit") int limit);
    
    long countByUserIdAndActivityType(Long userId, ActivityType activityType);
    
    long countByUserId(Long userId);
    
    List<Activity> findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(Long userId, LocalDateTime startDate, LocalDateTime endDate);
    
    long deleteByUserId(Long userId);
    
    long deleteByCreatedAtBefore(LocalDateTime cutoffDate);
}