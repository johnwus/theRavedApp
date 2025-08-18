package com.raved.realtime.websocket;

import com.raved.realtime.security.JwtAuthenticator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketChannelInterceptorJwtTest {

    @Test
    void preSend_registersSessionFromBearerToken() {
        WebSocketSessionManager manager = new WebSocketSessionManager();
        JwtAuthenticator auth = Mockito.mock(JwtAuthenticator.class);
        JwtAuthenticator.JwtUser user = new JwtAuthenticator.JwtUser();
        user.setUserId("u-jwt");
        Mockito.when(auth.authenticate("validtoken")).thenReturn(Optional.of(user));

        WebSocketChannelInterceptor interceptor = new WebSocketChannelInterceptor();
        try {
            var f1 = WebSocketChannelInterceptor.class.getDeclaredField("sessionManager");
            f1.setAccessible(true);
            f1.set(interceptor, manager);
            var f2 = WebSocketChannelInterceptor.class.getDeclaredField("jwtAuthenticator");
            f2.setAccessible(true);
            f2.set(interceptor, auth);
        } catch (Exception e) { throw new RuntimeException(e); }

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setSessionId("s1");
        accessor.setNativeHeader("authorization", "Bearer validtoken");
        Message<byte[]> msg = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        MessageChannel channel = Mockito.mock(MessageChannel.class);
        interceptor.preSend(msg, channel);

        assertThat(manager.getUserForSession("s1")).isEqualTo("u-jwt");
    }
}

