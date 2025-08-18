package com.raved.realtime.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class RedisSessionStore {

    private static final String KEY_PREFIX = "ws:sessions:";
    private static final Duration TTL = Duration.ofHours(12);

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    public void registerSession(String sessionId, String userId) {
        if (stringRedisTemplate == null) return; // fallback noop if Redis not configured
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set(KEY_PREFIX + sessionId, userId, TTL);
    }

    public void unregisterSession(String sessionId) {
        if (stringRedisTemplate == null) return;
        stringRedisTemplate.delete(KEY_PREFIX + sessionId);
    }

    public String getUserForSession(String sessionId) {
        if (stringRedisTemplate == null) return null;
        return stringRedisTemplate.opsForValue().get(KEY_PREFIX + sessionId);
    }
}

