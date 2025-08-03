package com.raved.realtime.service;

import com.raved.realtime.dto.request.SendMessageRequest;
import com.raved.realtime.dto.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * MessageService for TheRavedApp
 */
public interface MessageService {

    /**
     * Send message
     */
    MessageResponse sendMessage(SendMessageRequest request);

    /**
     * Get message
     */
    MessageResponse getMessage(Long messageId);

    /**
     * Get chat room messages
     */
    Page<MessageResponse> getChatRoomMessages(String roomId, Long userId, Pageable pageable);

    /**
     * Get messages by user
     */
    Page<MessageResponse> getMessagesByUser(Long userId, Pageable pageable);

    /**
     * Edit message
     */
    MessageResponse editMessage(Long messageId, Long userId, String newContent);

    /**
     * Delete message
     */
    void deleteMessage(Long messageId, Long userId);

    /**
     * Mark message as read
     */
    void markMessageAsRead(Long messageId, Long userId);

    /**
     * Mark messages as read
     */
    void markMessagesAsRead(String roomId, Long userId);

    /**
     * Add reaction
     */
    void addReaction(Long messageId, Long userId, String emoji);

    /**
     * Remove reaction
     */
    void removeReaction(Long messageId, Long userId, String emoji);

    /**
     * Get unread message count
     */
    long getUnreadMessageCount(String roomId, Long userId);

    /**
     * Get total message count
     */
    long getTotalMessageCount();

    /**
     * Get message count for room
     */
    long getMessageCountForRoom(String roomId);

    /**
     * Cleanup old messages
     */
    void cleanupOldMessages(LocalDateTime cutoffDate);
}
