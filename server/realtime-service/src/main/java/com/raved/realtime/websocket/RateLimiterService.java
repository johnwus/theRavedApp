package com.raved.realtime.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimiterService {

    @Autowired(required = false)
    private StringRedisTemplate redis;

    private final Map<String, Counter> localCounters = new ConcurrentHashMap<>();

    public boolean allow(String key, int limit, int windowSeconds) {
        if (redis != null) {
            Long count = redis.opsForValue().increment(key);
            if (count != null && count == 1L) {
                redis.expire(key, Duration.ofSeconds(windowSeconds));
            }
            return count != null && count <= limit;
        }
        long now = System.currentTimeMillis();
        Counter c = localCounters.compute(key, (k, existing) -> {
            if (existing == null || now > existing.resetAt) {
                return new Counter(1, now + windowSeconds * 1000L);
            } else {
                existing.count++;
                return existing;
            }
        });
        return c.count <= limit;
    }

    private static class Counter {
        int count;
        long resetAt;
        Counter(int count, long resetAt) { this.count = count; this.resetAt = resetAt; }
    }
}

