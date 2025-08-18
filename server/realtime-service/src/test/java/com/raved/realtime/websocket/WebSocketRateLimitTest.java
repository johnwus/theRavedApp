package com.raved.realtime.websocket;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WebSocketRateLimitTest {

    @Test
    void rateLimiter_allowsUpToLimitWithinWindow() {
        RateLimiterService rl = new RateLimiterService();
        String key = "test:rl";
        int allowed = 0;
        for (int i = 0; i < 35; i++) {
            if (rl.allow(key, 30, 1)) allowed++;
        }
        assertThat(allowed).isEqualTo(30);
    }
}

