package com.raved.realtime.service;

import com.raved.realtime.model.UserPresence;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing user presence and activity tracking
 */
public interface PresenceService {
    /**
     * Updates user's online status and presence information
     */
    void updateUserPresence(Long userId, UserPresence.PresenceStatus status, String location, String deviceInfo);

    /**
     * Marks a user as online
     */
    void markUserOnline(Long userId);

    /**
     * Marks a user as offline
     */
    void markUserOffline(Long userId);

    /**
     * Updates user's last active timestamp
     */
    void updateUserActivity(Long userId);

    /**
     * Updates user's last seen timestamp
     */
    void updateLastSeen(Long userId);

    /**
     * Gets user's current presence status
     */
    UserPresence getUserPresence(Long userId);

    /**
     * Gets list of online users
     */
    List<UserPresence> getOnlineUsers();

    /**
     * Gets list of users active in a specific location
     */
    List<UserPresence> getUsersInLocation(String location);

    /**
     * Gets list of users active within the last N minutes
     */
    List<UserPresence> getRecentlyActiveUsers(int minutes);

    /**
     * Checks if a user is currently online
     */
    boolean isUserOnline(Long userId);

    /**
     * Gets user's last active timestamp
     */
    LocalDateTime getLastActiveTime(Long userId);

    /**
     * Gets user's last seen timestamp
     */
    LocalDateTime getLastSeenTime(Long userId);

    /**
     * Cleans up inactive user sessions
     */
    void cleanupInactiveSessions(int timeoutMinutes);
}