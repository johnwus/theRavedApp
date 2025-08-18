package com.raved.realtime.repository;

import com.raved.realtime.model.ChatRoom;
import com.raved.realtime.model.ChatRoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository for ChatRoom entities
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    
    Optional<ChatRoom> findByRoomId(String roomId);
    
    @Query("SELECT c FROM ChatRoom c WHERE c.roomType = :type AND c.isActive = true ORDER BY c.lastMessageAt DESC")
    Page<ChatRoom> findByTypeAndIsActiveTrueOrderByLastMessageAtDesc(
            @Param("type") ChatRoomType type, Pageable pageable);
    
    @Query("SELECT c FROM ChatRoom c WHERE c.type = :type AND c.isActive = true ORDER BY c.currentParticipants DESC")
    Page<ChatRoom> findByTypeAndIsActiveTrueOrderByCurrentParticipantsDesc(
            @Param("type") ChatRoomType type, Pageable pageable);
    
    @Query("SELECT c FROM ChatRoom c JOIN ChatRoomMember m ON c.id = m.roomId " +
           "WHERE m.userId = :userId AND m.isActive = true ORDER BY c.lastMessageAt DESC")
    Page<ChatRoom> findByMembersContainingOrderByLastMessageAtDesc(
            @Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT c FROM ChatRoom c JOIN ChatRoomMember m ON c.id = m.roomId " +
           "WHERE m.userId = :userId AND m.isActive = true ORDER BY c.lastActivityAt DESC")
    Page<ChatRoom> findByParticipantsContainingOrderByLastActivityAtDesc(
            @Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT m.userId FROM ChatRoomMember m WHERE m.roomId = :roomId AND m.isActive = true")
    List<Long> findActiveMemberIdsByRoomId(@Param("roomId") Long roomId);
    
    @Query("SELECT m.userId FROM ChatRoomMember m WHERE m.roomId = :roomId AND m.isActive = true")
    List<Long> findParticipantsByRoomId(@Param("roomId") String roomId);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM ChatRoomMember m " +
           "WHERE m.roomId = :roomId AND m.userId = :userId AND m.isActive = true")
    boolean isUserActiveMember(@Param("roomId") Long roomId, @Param("userId") Long userId);
    
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM ChatRoomMember m " +
           "WHERE m.roomId = :roomId AND m.userId = :userId AND m.isActive = true")
    boolean isUserParticipant(@Param("roomId") String roomId, @Param("userId") Long userId);
    
    long countByIsActiveTrue();
    
    long countByIsActiveTrueAndLastMessageAtAfter(LocalDateTime timestamp);
    
    @Query("SELECT COUNT(c) FROM ChatRoom c WHERE c.isActive = true AND c.lastActivityAt > :timestamp")
    long countByIsActiveTrueAndLastActivityAtAfter(@Param("timestamp") LocalDateTime timestamp);
    
    List<ChatRoom> findByLastMessageAtBeforeAndIsActiveTrue(LocalDateTime timestamp);
    
    @Query("SELECT c FROM ChatRoom c WHERE c.lastActivityAt < :timestamp AND c.isActive = true")
    List<ChatRoom> findByLastActivityAtBeforeAndIsActiveTrue(@Param("timestamp") LocalDateTime timestamp);
}