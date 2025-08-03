package com.raved.realtime.service;

import com.raved.realtime.dto.request.CreateChatRoomRequest;
import com.raved.realtime.dto.request.JoinChatRoomRequest;
import com.raved.realtime.dto.response.ChatRoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ChatService for TheRavedApp
 */
public interface ChatService {

    /**
     * Create chat room
     */
    ChatRoomResponse createChatRoom(CreateChatRoomRequest request);

    /**
     * Join chat room
     */
    ChatRoomResponse joinChatRoom(JoinChatRoomRequest request);

    /**
     * Leave chat room
     */
    void leaveChatRoom(String roomId, Long userId);

    /**
     * Get chat room
     */
    ChatRoomResponse getChatRoom(String roomId);

    /**
     * Get user chat rooms
     */
    Page<ChatRoomResponse> getUserChatRooms(Long userId, Pageable pageable);

    /**
     * Get public chat rooms
     */
    Page<ChatRoomResponse> getPublicChatRooms(Pageable pageable);

    /**
     * Get chat room participants
     */
    List<Long> getChatRoomParticipants(String roomId);

    /**
     * Check if user is in chat room
     */
    boolean isUserInChatRoom(String roomId, Long userId);

    /**
     * Update chat room
     */
    ChatRoomResponse updateChatRoom(String roomId, Long userId, CreateChatRoomRequest request);

    /**
     * Delete chat room
     */
    void deleteChatRoom(String roomId, Long userId);

    /**
     * Get chat room count
     */
    long getChatRoomCount();

    /**
     * Get active chat room count
     */
    long getActiveChatRoomCount();

    /**
     * Update chat room activity
     */
    void updateChatRoomActivity(String roomId);

    /**
     * Cleanup inactive chat rooms
     */
    void cleanupInactiveChatRooms(LocalDateTime cutoffDate);
}
