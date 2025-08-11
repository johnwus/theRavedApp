package com.raved.realtime.service.impl;

import com.raved.realtime.model.UserPresence;
import com.raved.realtime.repository.UserPresenceRepository;
import com.raved.realtime.service.PresenceService;
import com.raved.realtime.websocket.MessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PresenceServiceImpl implements PresenceService {

    private static final Logger logger = LoggerFactory.getLogger(PresenceServiceImpl.class);

    @Autowired
    private UserPresenceRepository presenceRepository;

    @Autowired
    private MessageBroker messageBroker;

    @Override
    public void updateUserPresence(Long userId, UserPresence.PresenceStatus status, String location, String deviceInfo) {
        logger.debug("Updating presence for user: {} status: {}", userId, status);

        UserPresence presence = presenceRepository.findByUserId(userId)
                .orElse(new UserPresence(userId));

        presence.setUserId(userId);
        presence.setStatus(status);
        presence.setLastLocation(location);
        presence.setDeviceInfo(deviceInfo);
        presence.setLastActiveAt(LocalDateTime.now());
        presence.setIsOnline(true);

        presenceRepository.save(presence);

        // Broadcast presence update
        Map<String, Object> presenceUpdate = new HashMap<>();
        presenceUpdate.put("userId", userId);
        presenceUpdate.put("status", status);
        presenceUpdate.put("location", location);
        messageBroker.broadcastPresenceUpdate(presenceUpdate);
    }

    @Override
    public void markUserOnline(Long userId) {
        logger.debug("Marking user online: {}", userId);

        UserPresence presence = presenceRepository.findByUserId(userId)
                .orElse(new UserPresence(userId));

        presence.setUserId(userId);
        presence.setIsOnline(true);
        presence.setStatus(UserPresence.PresenceStatus.ONLINE);
        presence.setLastActiveAt(LocalDateTime.now());
        presence.setLastSeenAt(LocalDateTime.now());

        presenceRepository.save(presence);

        // Broadcast online status
        messageBroker.broadcastUserOnline(userId);
    }

    @Override
    public void markUserOffline(Long userId) {
        logger.debug("Marking user offline: {}", userId);

        UserPresence presence = presenceRepository.findByUserId(userId)
                .orElse(new UserPresence(userId));

        presence.setUserId(userId);
        presence.setIsOnline(false);
        presence.setStatus(UserPresence.PresenceStatus.OFFLINE);
        presence.setLastSeenAt(LocalDateTime.now());

        presenceRepository.save(presence);

        // Broadcast offline status
        messageBroker.broadcastUserOffline(userId);
    }

    @Override
    public void updateUserActivity(Long userId) {
        logger.debug("Updating activity for user: {}", userId);

        presenceRepository.findByUserId(userId).ifPresent(presence -> {
            presence.setLastActiveAt(LocalDateTime.now());
            presenceRepository.save(presence);
        });
    }

    @Override
    public void updateLastSeen(Long userId) {
        logger.debug("Updating last seen for user: {}", userId);

        presenceRepository.findByUserId(userId).ifPresent(presence -> {
            presence.setLastSeenAt(LocalDateTime.now());
            presenceRepository.save(presence);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public UserPresence getUserPresence(Long userId) {
        return presenceRepository.findByUserId(userId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPresence> getOnlineUsers() {
        return presenceRepository.findByIsOnlineTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPresence> getUsersInLocation(String location) {
        return presenceRepository.findByLastLocationStartingWith(location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserPresence> getRecentlyActiveUsers(int minutes) {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(minutes);
        return presenceRepository.findByLastActiveAtAfter(threshold);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserOnline(Long userId) {
        return presenceRepository.findByUserId(userId)
                .map(UserPresence::getIsOnline)
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public LocalDateTime getLastActiveTime(Long userId) {
        return presenceRepository.findByUserId(userId)
                .map(UserPresence::getLastActiveAt)
                .orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public LocalDateTime getLastSeenTime(Long userId) {
        return presenceRepository.findByUserId(userId)
                .map(UserPresence::getLastSeenAt)
                .orElse(null);
    }

    @Override
    @Scheduled(fixedRate = 300000) // Run every 5 minutes
    public void cleanupInactiveSessions(int timeoutMinutes) {
        logger.info("Cleaning up inactive sessions older than {} minutes", timeoutMinutes);

        LocalDateTime threshold = LocalDateTime.now().minusMinutes(timeoutMinutes);
        List<UserPresence> inactiveUsers = presenceRepository.findInactiveUsers(threshold);

        for (UserPresence user : inactiveUsers) {
            markUserOffline(user.getUserId());
        }

        logger.info("Cleaned up {} inactive sessions", inactiveUsers.size());
    }
}