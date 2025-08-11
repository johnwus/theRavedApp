package com.raved.analytics.repository;

import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
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
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {
    
    Page<AnalyticsEvent> findByUserIdOrderByTimestampDesc(Long userId, Pageable pageable);
    
    Page<AnalyticsEvent> findByEventTypeOrderByTimestampDesc(EventType eventType, Pageable pageable);
    
    Page<AnalyticsEvent> findByTimestampBetweenOrderByTimestampDesc(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    @Query("SELECT e.eventType, COUNT(e) FROM AnalyticsEvent e WHERE e.timestamp BETWEEN :startDate AND :endDate GROUP BY e.eventType")
    List<Object[]> countEventsByType(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT DATE(e.timestamp), COUNT(e) FROM AnalyticsEvent e WHERE e.timestamp BETWEEN :startDate AND :endDate GROUP BY DATE(e.timestamp)")
    List<Object[]> countEventsByDay(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    long countByUserId(Long userId);
    
    long countByUserIdAndEventType(Long userId, EventType eventType);
    
    long countByTargetIdAndEventType(Long targetId, EventType eventType);
    
    @Query("SELECT COUNT(DISTINCT e.userId) FROM AnalyticsEvent e WHERE e.targetId = :targetId AND e.eventType = :eventType AND e.timestamp BETWEEN :startDate AND :endDate")
    long countUniqueUsers(@Param("targetId") Long targetId, @Param("eventType") EventType eventType, 
                         @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(e) FROM AnalyticsEvent e WHERE e.targetId = :targetId AND e.eventType = :eventType AND e.timestamp >= :since")
    long countByTargetIdAndEventTypeAndTimestampAfter(@Param("targetId") Long targetId, @Param("eventType") EventType eventType, 
                                                     @Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(e) FROM AnalyticsEvent e WHERE e.targetId = :targetId AND e.eventType = :eventType AND e.timestamp BETWEEN :startDate AND :endDate")
    long countByTargetIdAndEventTypeAndTimestampBetween(@Param("targetId") Long targetId, @Param("eventType") EventType eventType,
                                                       @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e.timestamp FROM AnalyticsEvent e WHERE e.userId = :userId ORDER BY e.timestamp DESC LIMIT 1")
    Optional<LocalDateTime> findLastEventTimeForUser(@Param("userId") Long userId);
    
    @Query("SELECT COUNT(e) FROM AnalyticsEvent e WHERE e.timestamp >= :since")
    long countByTimestampAfter(@Param("since") LocalDateTime since);
    
    long deleteByTimestampBefore(LocalDateTime cutoffDate);
    
    @Query("SELECT DATE(e.timestamp), COUNT(e) FROM AnalyticsEvent e WHERE e.userId = :userId AND e.eventType = :eventType AND e.timestamp BETWEEN :startDate AND :endDate GROUP BY DATE(e.timestamp)")
    List<Object[]> getDailyEventCounts(@Param("userId") Long userId, @Param("eventType") EventType eventType,
                                      @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT HOUR(e.timestamp), COUNT(e) FROM AnalyticsEvent e WHERE e.targetId = :targetId AND e.eventType = :eventType AND e.timestamp BETWEEN :startDate AND :endDate GROUP BY HOUR(e.timestamp)")
    List<Object[]> getHourlyEventCounts(@Param("targetId") Long targetId, @Param("eventType") EventType eventType,
                                       @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT DATE(e.timestamp), COUNT(DISTINCT e.userId) FROM AnalyticsEvent e WHERE e.timestamp BETWEEN :startDate AND :endDate GROUP BY DATE(e.timestamp)")
    List<Object[]> getDailyActiveUsers(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(e) FROM AnalyticsEvent e WHERE e.eventType = :eventType AND e.timestamp BETWEEN :startDate AND :endDate")
    long countByEventTypeAndTimestampBetween(@Param("eventType") EventType eventType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT DATE(e.timestamp), COUNT(e) FROM AnalyticsEvent e WHERE e.eventType = :eventType AND e.timestamp BETWEEN :startDate AND :endDate GROUP BY DATE(e.timestamp)")
    List<Object[]> getDailyEventCounts(@Param("eventType") EventType eventType, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT DATE(e.timestamp), COUNT(e) FROM AnalyticsEvent e WHERE e.timestamp BETWEEN :startDate AND :endDate GROUP BY DATE(e.timestamp)")
    List<Object[]> getDailyEventCounts(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(e) FROM AnalyticsEvent e WHERE e.timestamp BETWEEN :startDate AND :endDate")
    long countByTimestampBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e.eventData, COUNT(*) as mentionCount, COUNT(DISTINCT e.userId) as uniqueUsers FROM AnalyticsEvent e WHERE e.eventTimestamp >= :since AND e.eventData LIKE '%#%' GROUP BY e.eventData ORDER BY mentionCount DESC")
    List<Object[]> getTrendingTopics(@Param("since") LocalDateTime since, int limit);
    
    @Query("SELECT HOUR(e.timestamp), COUNT(e) FROM AnalyticsEvent e WHERE e.timestamp >= :since GROUP BY HOUR(e.timestamp) ORDER BY COUNT(e) DESC")
    List<Object[]> getPeakEngagementHours(@Param("since") LocalDateTime since);
}
