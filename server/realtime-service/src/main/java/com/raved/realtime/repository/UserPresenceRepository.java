package com.raved.realtime.repository;

import com.raved.realtime.model.UserPresence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserPresenceRepository extends JpaRepository<UserPresence, Long> {
    
    Optional<UserPresence> findByUserId(Long userId);
    
    List<UserPresence> findByIsOnlineTrue();
    List<UserPresence> findByLastActiveAtAfter(LocalDateTime timestamp);
    List<UserPresence> findByLastLocationStartingWith(String locationPrefix);
    
    @Query("SELECT up FROM UserPresence up WHERE up.isOnline = true AND up.lastActiveAt < :threshold")
    List<UserPresence> findInactiveUsers(LocalDateTime threshold);
}