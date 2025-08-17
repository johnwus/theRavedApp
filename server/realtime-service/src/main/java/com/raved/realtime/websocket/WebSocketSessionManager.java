package com.raved.realtime.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketSessionManager.class);

    private final Map<String, String> localSessionToUser = new ConcurrentHashMap<>();

    @Autowired(required = false)
    private RedisSessionStore redisSessionStore;

    public void registerSession(String sessionId, String userId) {
        localSessionToUser.put(sessionId, userId);
        if (redisSessionStore != null) redisSessionStore.registerSession(sessionId, userId);
        logger.debug("Registered session {} for user {}", sessionId, userId);
    }

    public void unregisterSession(String sessionId) {
        String userId = localSessionToUser.remove(sessionId);
        if (redisSessionStore != null) redisSessionStore.unregisterSession(sessionId);
        logger.debug("Unregistered session {} for user {}", sessionId, userId);
    }

    public String getUserForSession(String sessionId) {
        String user = localSessionToUser.get(sessionId);
        if (user == null && redisSessionStore != null) {
            user = redisSessionStore.getUserForSession(sessionId);
        }
        return user;
    }
}
