package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketAuthorizationNegativeTest {

    @Test
    void preSend_blocksWhenMissingJwtOnSend() {
        WebSocketChannelInterceptor interceptor = new WebSocketChannelInterceptor();
        try {
            var sm = WebSocketChannelInterceptor.class.getDeclaredField("sessionManager");
            sm.setAccessible(true);
            sm.set(interceptor, new WebSocketSessionManager());

            var auth = WebSocketChannelInterceptor.class.getDeclaredField("jwtAuthenticator");
            auth.setAccessible(true);
            auth.set(interceptor, new com.raved.realtime.security.JwtAuthenticator());

            var cs = WebSocketChannelInterceptor.class.getDeclaredField("chatService");
            cs.setAccessible(true);
            cs.set(interceptor, new DummyChatService());

            var rl = WebSocketChannelInterceptor.class.getDeclaredField("rateLimiter");
            rl.setAccessible(true);
            rl.set(interceptor, new RateLimiterService());
        } catch (Exception e) { throw new RuntimeException(e); }

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SEND);
        accessor.setDestination("/app/chat/rooms/room-1/send");
        accessor.setSessionId("s1");
        var msg = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        var result = interceptor.preSend(msg, new org.springframework.messaging.MessageChannel() {
            @Override
            public boolean send(org.springframework.messaging.Message<?> m) {
                return true;
            }

            @Override
            public boolean send(org.springframework.messaging.Message<?> m, long timeout) {
                return true;
            }
        });
        assertThat(result).isNull();
    }

    @Test
    void preSend_blocksWhenNotInRoomOnSubscribe() {
        WebSocketChannelInterceptor interceptor = new WebSocketChannelInterceptor();
        try {
            var sm = WebSocketChannelInterceptor.class.getDeclaredField("sessionManager");
            sm.setAccessible(true);
            sm.set(interceptor, new WebSocketSessionManager());

            var auth = WebSocketChannelInterceptor.class.getDeclaredField("jwtAuthenticator");
            auth.setAccessible(true);
            auth.set(interceptor, new com.raved.realtime.security.JwtAuthenticator(){
                @Override public java.util.Optional<JwtUser> authenticate(String token) { JwtUser u = new JwtUser(); u.setUserId("99"); return java.util.Optional.of(u);} });

            var cs = WebSocketChannelInterceptor.class.getDeclaredField("chatService");
            cs.setAccessible(true);
            cs.set(interceptor, new DummyChatService());

            var rl = WebSocketChannelInterceptor.class.getDeclaredField("rateLimiter");
            rl.setAccessible(true);
            rl.set(interceptor, new RateLimiterService());
        } catch (Exception e) { throw new RuntimeException(e); }

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination("/topic/room/room-1");
        accessor.setSessionId("s1");
        accessor.setNativeHeader("authorization", "Bearer sometoken");
        var msg = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        var result = interceptor.preSend(msg, new org.springframework.messaging.MessageChannel() {
            @Override
            public boolean send(org.springframework.messaging.Message<?> m) {
                return true;
            }

            @Override
            public boolean send(org.springframework.messaging.Message<?> m, long timeout) {
                return true;
            }
        });
        assertThat(result).isNull();
    }

    static class DummyChatService implements com.raved.realtime.service.ChatService {
        public com.raved.realtime.dto.response.ChatRoomResponse createChatRoom(com.raved.realtime.dto.request.CreateChatRoomRequest request){ return null; }
        public com.raved.realtime.dto.response.ChatRoomResponse joinChatRoom(com.raved.realtime.dto.request.JoinChatRoomRequest request){ return null; }
        public void leaveChatRoom(String roomId, Long userId){}
        public com.raved.realtime.dto.response.ChatRoomResponse getChatRoom(String roomId){ return null; }
        public org.springframework.data.domain.Page<com.raved.realtime.dto.response.ChatRoomResponse> getUserChatRooms(Long userId, org.springframework.data.domain.Pageable pageable){ return null; }
        public org.springframework.data.domain.Page<com.raved.realtime.dto.response.ChatRoomResponse> getPublicChatRooms(org.springframework.data.domain.Pageable pageable){ return null; }
        public java.util.List<Long> getChatRoomParticipants(String roomId){ return java.util.List.of(); }
        public boolean isUserInChatRoom(String roomId, Long userId){ return false; }
        public com.raved.realtime.dto.response.ChatRoomResponse updateChatRoom(String roomId, Long userId, com.raved.realtime.dto.request.CreateChatRoomRequest request){ return null; }
        public void deleteChatRoom(String roomId, Long userId){}
        public long getChatRoomCount(){ return 0; }
        public long getActiveChatRoomCount(){ return 0; }
        public void updateChatRoomActivity(String roomId){}
        public void cleanupInactiveChatRooms(java.time.LocalDateTime cutoffDate){}
    }
}

