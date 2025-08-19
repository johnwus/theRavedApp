package com.raved.realtime.websocket;

import com.raved.realtime.security.JwtAuthenticator;
import com.raved.realtime.service.ChatService;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketChannelInterceptor implements ChannelInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketChannelInterceptor.class);

    @Autowired
    private WebSocketSessionManager sessionManager;
    @Autowired
    private JwtAuthenticator jwtAuthenticator;
    @Autowired
    @org.springframework.context.annotation.Lazy
    private ChatService chatService;
    @Autowired
    private RateLimiterService rateLimiter;
    @Autowired(required = false)
    private MeterRegistry meterRegistry;

    @Value("${websocket.ratelimit.window-seconds:${websocket.ratelimit.window.seconds:5}}")
    private int rlWindowSeconds = 5;
    @Value("${websocket.ratelimit.send-limit:${websocket.ratelimit.send.limit:30}}")
    private int rlSendLimit = 30;
    @Value("${websocket.ratelimit.subscribe-limit:${websocket.ratelimit.subscribe.limit:30}}")
    private int rlSubscribeLimit = 30;
    @Value("${websocket.ratelimit.enabled:true}")
    private boolean rlEnabled = true;
    @Value("${websocket.auth.required:true}")
    private boolean authRequired = true;
    @Value("${websocket.membership.check:true}")
    private boolean membershipCheck = true;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor != null) {
            StompCommand command = accessor.getCommand();
            String sessionId = accessor.getSessionId();
            String bearer = accessor.getFirstNativeHeader("authorization");
            String token = (bearer != null && bearer.toLowerCase().startsWith("bearer ")) ? bearer.substring(7) : bearer;

            // Always try to register session when a token or x-user-id is provided, regardless of command
            String headerUser = accessor.getFirstNativeHeader("x-user-id");
            if (sessionId != null) {
                if (token != null) {
                    var optUser = jwtAuthenticator.authenticate(token);
                    if (optUser.isPresent()) {
                        sessionManager.registerSession(sessionId, optUser.get().getUserId());
                    }
                } else if (headerUser != null) {
                    sessionManager.registerSession(sessionId, headerUser);
                }
            }

            if (command == StompCommand.SEND || command == StompCommand.SUBSCRIBE) {
                // Enforce JWT for message actions
                if (token == null) {
                    recordBlocked("missing_jwt", command, null);
                    logger.debug("Blocking {} due to missing JWT", command);
                    return null;
                }
                var optUser = jwtAuthenticator.authenticate(token);
                if (optUser.isEmpty()) {
                    recordBlocked("invalid_jwt", command, null);
                    logger.debug("Blocking {} due to invalid JWT", command);
                    return null;
                }
                String userId = optUser.get().getUserId();
                if (sessionId != null) {
                    sessionManager.registerSession(sessionId, userId);
                }

                String destination = accessor.getDestination();
                String roomId = extractRoomId(destination, command);
                if (roomId != null) {
                    try {
                        Long uid = Long.valueOf(userId);
                        boolean inRoom = true;
                        if (membershipCheck) {
                            inRoom = chatService.isUserInChatRoom(roomId, uid);
                        }
                        if (!inRoom) {
                            recordBlocked("not_in_room", command, roomId);
                            logger.debug("Blocking {} tbo {} for user {} not in room", command, destination, userId);
                            return null;
                        }
                        if (rlEnabled) {
                            int limit = (command == StompCommand.SEND) ? rlSendLimit : rlSubscribeLimit;
                            String rlKey = "ws:rl:" + roomId + ":" + userId + ":" + (command != null ? command.name().toLowerCase() : "unknown");
                            if (!rateLimiter.allow(rlKey, limit, rlWindowSeconds)) {
                                recordBlocked("rate_limited", command, roomId);
                                logger.debug("Rate limit exceeded for user {} on {} {}", userId, command, destination);
                                return null;
                            }
                        }
                        recordAllowed(command, roomId);
                    } catch (NumberFormatException nfe) {
                        recordBlocked("bad_user_id", command, roomId);
                        logger.debug("Blocking {} due to non-numeric userId {}", command, userId);
                        return null;
                    }
                } else {
                    // No room destination; still count allowed
                    recordAllowed(command, null);
                }
            }
        }
        return message;
    }

    private void recordBlocked(String reason, StompCommand cmd, String roomId) {
        if (meterRegistry != null) {
            meterRegistry.counter("websocket.blocked", Tags.of("reason", reason, "command", String.valueOf(cmd), "room", String.valueOf(roomId))).increment();
        }
    }

    private void recordAllowed(StompCommand cmd, String roomId) {
        if (meterRegistry != null) {
            meterRegistry.counter("websocket.allowed", Tags.of("command", String.valueOf(cmd), "room", String.valueOf(roomId))).increment();
        }
    }

    private String extractRoomId(String destination, StompCommand command) {
        if (destination == null) {
            return null;
        }
        if (command == StompCommand.SEND && destination.startsWith("/app/chat/rooms/") && destination.endsWith("/send")) {
            return destination.substring("/app/chat/rooms/".length(), destination.length() - "/send".length());
        }
        if (command == StompCommand.SUBSCRIBE && destination.startsWith("/topic/room/")) {
            return destination.substring("/topic/room/".length());
        }
        return null;
    }

    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
    }

    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, @Nullable Exception ex) {
    }
}
