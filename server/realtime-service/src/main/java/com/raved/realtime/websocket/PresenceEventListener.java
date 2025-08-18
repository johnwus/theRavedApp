package com.raved.realtime.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
public class PresenceEventListener {

    private static final Logger logger = LoggerFactory.getLogger(PresenceEventListener.class);

    @Autowired
    private WebSocketSessionManager sessionManager;

    @Autowired
    private MessageBroker messageBroker;

    @EventListener
    public void handleSessionConnected(SessionConnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        String userId = getFirstNativeHeader(sha, "x-user-id");
        if (sessionId != null && userId != null) {
            sessionManager.registerSession(sessionId, userId);
            logger.debug("User {} connected with session {}", userId, sessionId);
            messageBroker.broadcastUserOnline(Long.valueOf(userId));
        }
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = sha.getSessionId();
        if (sessionId != null) {
            String userId = sessionManager.getUserForSession(sessionId);
            sessionManager.unregisterSession(sessionId);
            if (userId != null) {
                logger.debug("User {} disconnected (session {})", userId, sessionId);
                messageBroker.broadcastUserOffline(Long.valueOf(userId));
            }
        }
    }

    private String getFirstNativeHeader(StompHeaderAccessor accessor, String name) {
        if (accessor.getNativeHeader(name) != null && !accessor.getNativeHeader(name).isEmpty()) {
            return accessor.getNativeHeader(name).get(0);
        }
        return null;
    }
}

