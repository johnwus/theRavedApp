package com.raved.realtime.repository;

import com.raved.realtime.model.MessageReaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for MessageReaction entities
 */
@Repository
public interface MessageReactionRepository extends JpaRepository<MessageReaction, Long> {
    
    List<MessageReaction> findByMessageId(Long messageId);
    
    List<MessageReaction> findByMessageIdAndEmoji(Long messageId, String emoji);
    
    List<MessageReaction> findByUserId(Long userId);
    
    Optional<MessageReaction> findByMessageIdAndUserIdAndEmoji(Long messageId, Long userId, String emoji);
    
    long countByMessageId(Long messageId);
    
    long countByMessageIdAndEmoji(Long messageId, String emoji);
    
    void deleteByMessageIdAndUserId(Long messageId, Long userId);
    
    void deleteByMessageId(Long messageId);
}