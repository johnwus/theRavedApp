package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class WebSocketSessionManagerTest {

    @Test
    void registerAndUnregisterSession() {
        WebSocketSessionManager manager = new WebSocketSessionManager();
        manager.registerSession("s1", "u1");
        assertThat(manager.getUserForSession("s1")).isEqualTo("u1");
        manager.unregisterSession("s1");
        assertThat(manager.getUserForSession("s1")).isNull();
    }
}

