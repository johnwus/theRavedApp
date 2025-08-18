package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketChannelInterceptorTest {

    @Test
    void preSend_registersSessionWithUser() {
        WebSocketSessionManager manager = new WebSocketSessionManager();
        WebSocketChannelInterceptor interceptor = new WebSocketChannelInterceptor();
        // inject manager
        try {
            var field = WebSocketChannelInterceptor.class.getDeclaredField("sessionManager");
            field.setAccessible(true);
            field.set(interceptor, manager);
        } catch (Exception e) { throw new RuntimeException(e); }

        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.create();
        accessor.setSessionId("s1");
        accessor.setNativeHeader("x-user-id", "42");

        Message<byte[]> msg = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());
        MessageChannel channel = Mockito.mock(MessageChannel.class);

        interceptor.preSend(msg, channel);

        assertThat(manager.getUserForSession("s1")).isEqualTo("42");
    }
}

