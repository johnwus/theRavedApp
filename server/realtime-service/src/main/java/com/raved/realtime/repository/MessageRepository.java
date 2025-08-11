package com.raved.realtime.repository;

import com.raved.realtime.model.Message;
import com.raved.realtime.model.MessageStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for Message entities
 */
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    Page<Message> findByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);
    
    Page<Message> findBySenderIdOrderByCreatedAtDesc(Long senderId, Pageable pageable);
    
    List<Message> findByChatRoomIdAndStatusAndSenderIdNot(
            Long chatRoomId, MessageStatus status, Long senderId);
    
    long countByChatRoomIdAndStatusAndSenderIdNot(
            Long chatRoomId, MessageStatus status, Long senderId);
    
    long countByChatRoomId(Long chatRoomId);
    
    @Modifying
    @Query("DELETE FROM Message m WHERE m.createdAt < :cutoffDate")
    long deleteByCreatedAtBefore(@Param("cutoffDate") LocalDateTime cutoffDate);
}