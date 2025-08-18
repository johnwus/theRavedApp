package com.raved.analytics.service;

import com.raved.analytics.algorithm.EngagementCalculator;
import com.raved.analytics.dto.response.ContentMetricsResponse;
import com.raved.analytics.dto.response.UserMetricsResponse;
import com.raved.analytics.mapper.ContentMetricsMapper;
import com.raved.analytics.mapper.UserMetricsMapper;
import com.raved.analytics.model.ContentMetrics;
import com.raved.analytics.model.UserMetrics;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.UserMetricsRepository;
import com.raved.analytics.service.impl.MetricsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class MetricsServiceImplTest {

    @Mock
    private UserMetricsRepository userRepo;
    @Mock
    private ContentMetricsRepository contentRepo;
    @Mock
    private AnalyticsEventRepository eventRepo;
    @Mock
    private UserMetricsMapper userMapper;
    @Mock
    private ContentMetricsMapper contentMapper;
    private EngagementCalculator engagementCalculator;
    private RedisTemplate<String, Object> redis;

    private MetricsServiceImpl service;

    // Simple in-memory RedisTemplate using a dynamic proxy for ValueOperations
    private static class InMemoryRedisTemplate extends RedisTemplate<String, Object> {

        private final java.util.concurrent.ConcurrentHashMap<String, Object> store = new java.util.concurrent.ConcurrentHashMap<>();
        private final org.springframework.data.redis.core.ValueOperations<String, Object> ops;

        @SuppressWarnings("unchecked")
        InMemoryRedisTemplate() {
            this.ops = (org.springframework.data.redis.core.ValueOperations<String, Object>) java.lang.reflect.Proxy.newProxyInstance(
                    org.springframework.data.redis.core.ValueOperations.class.getClassLoader(),
                    new Class[]{org.springframework.data.redis.core.ValueOperations.class},
                    (proxy, method, args) -> {
                        String name = method.getName();
                        switch (name) {
                            case "get":
                                return store.get(args[0]);
                            case "set":
                                // supports set(key, value) and set(key, value, ...)
                                store.put((String) args[0], args[1]);
                                return null;
                            case "getAndDelete":
                                return store.remove(args[0]);
                            case "getAndSet":
                                return store.put((String) args[0], args[1]);
                            case "multiGet":
                                java.util.Collection<String> keys = (java.util.Collection<String>) args[0];
                                return keys.stream().map(store::get).toList();
                            case "multiSet":
                                store.putAll((java.util.Map<String, Object>) args[0]);
                                return null;
                            case "multiSetIfAbsent":
                                ((java.util.Map<String, Object>) args[0]).forEach(store::putIfAbsent);
                                return Boolean.TRUE;
                            case "setIfAbsent":
                                return store.putIfAbsent((String) args[0], args[1]) == null;
                            case "getOperations":
                                return this;
                            default:
                                // default return values for unused methods
                                Class<?> rt = method.getReturnType();
                                if (rt.equals(Boolean.class) || rt.equals(boolean.class)) {
                                    return Boolean.FALSE;
                                }
                                if (rt.equals(Long.class) || rt.equals(long.class)) {
                                    return 0L;
                                }
                                if (rt.equals(Double.class) || rt.equals(double.class)) {
                                    return 0.0;
                                }
                                if (rt.equals(Integer.class) || rt.equals(int.class)) {
                                    return 0;
                                }
                                return null;
                        }
                    }
            );
        }

        @Override
        public org.springframework.data.redis.core.ValueOperations<String, Object> opsForValue() {
            return ops;
        }

        @Override
        public Boolean delete(String key) {
            return store.remove(key) != null;
        }
    }

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new MetricsServiceImpl();
        // Provide a simple stub EngagementCalculator to avoid inline mocking issues on newer JDKs
        engagementCalculator = new EngagementCalculator() {
            @Override
            public BigDecimal calculateContentEngagementScore(String contentId) {
                return BigDecimal.valueOf(0.5);
            }

            @Override
            public BigDecimal calculateUserEngagementRate(String userId) {
                return BigDecimal.ONE;
            }
        };
        redis = new InMemoryRedisTemplate();
        inject("userMetricsRepository", userRepo);
        inject("contentMetricsRepository", contentRepo);
        inject("eventRepository", eventRepo);
        inject("userMetricsMapper", userMapper);
        inject("contentMetricsMapper", contentMapper);
        inject("engagementCalculator", engagementCalculator);
        inject("redisTemplate", redis);
    }

    private void inject(String field, Object val) throws Exception {
        var f = MetricsServiceImpl.class.getDeclaredField(field);
        f.setAccessible(true);
        f.set(service, val);
    }

    @Test
    void getUserMetrics_cacheHit_returnsCached() {
        var expected = new UserMetricsResponse();
        redis.opsForValue().set("metrics:user:u1", expected);
        assertThat(service.getUserMetrics("u1")).isSameAs(expected);
    }

    @Test
    void getUserMetrics_cacheMiss_fallsBackToRepo_or_default() {
        // No cache entry
        when(userRepo.findByUserId("u1")).thenReturn(Optional.empty());
        when(userMapper.toUserMetricsResponse(any())).thenAnswer(inv -> new UserMetricsResponse());
        assertThat(service.getUserMetrics("u1")).isNotNull();
    }

    @Test
    void getContentMetrics_cacheHit_returnsCached() {
        var expected = new ContentMetricsResponse();
        redis.opsForValue().set("metrics:content:c1", expected);
        assertThat(service.getContentMetrics("c1")).isSameAs(expected);
    }

    @Test
    void getContentMetrics_cacheMiss_fallsBackToRepo_or_default() {
        when(contentRepo.findByContentId("c1")).thenReturn(List.of());
        when(contentMapper.toContentMetricsResponse(any())).thenAnswer(inv -> new ContentMetricsResponse());
        assertThat(service.getContentMetrics("c1")).isNotNull();
    }

    @Test
    void topUsersAndContentByEngagement_mapsResponses() {
        Page<UserMetrics> userPage = new PageImpl<>(List.of(new UserMetrics()));
        when(userRepo.findTopByEngagementRate(PageRequest.of(0, 10))).thenReturn(userPage);
        when(userMapper.toUserMetricsResponse(any())).thenReturn(new UserMetricsResponse());
        assertThat(service.getTopUsersByEngagement(PageRequest.of(0, 10)).getContent()).hasSize(1);

        Page<ContentMetrics> contentPage = new PageImpl<>(List.of(new ContentMetrics()));
        when(contentRepo.findTopByEngagementScore(PageRequest.of(0, 5))).thenReturn(contentPage);
        when(contentMapper.toContentMetricsResponse(any())).thenReturn(new ContentMetricsResponse());
        assertThat(service.getTopContentByEngagement(PageRequest.of(0, 5)).getContent()).hasSize(1);
    }

    @Test
    void platformMetrics_aggregates_and_caches() {
        when(userRepo.count()).thenReturn(10L);
        when(userRepo.countActiveUsers(any(LocalDateTime.class))).thenReturn(5L);
        when(userRepo.getAverageEngagementRate()).thenReturn(0.25);
        when(contentRepo.count()).thenReturn(20L);
        when(contentRepo.getTotalViews()).thenReturn(100L);
        when(contentRepo.getTotalLikes()).thenReturn(50L);
        when(contentRepo.getTotalComments()).thenReturn(25L);
        when(contentRepo.getTotalShares()).thenReturn(10L);
        when(eventRepo.count()).thenReturn(500L);
        when(eventRepo.countByTimestampAfter(any(LocalDateTime.class))).thenReturn(12L);

        Map<String, Object> m = service.getPlatformMetrics();
        assertThat(m.get("activeUsers")).isEqualTo(5L);
        assertThat(m.get("totalViews")).isEqualTo(100L);
        assertThat(m.get("totalEvents")).isEqualTo(500L);
        assertThat(m.get("todayEvents")).isEqualTo(12L);
        assertThat(m.get("averageEngagementRate")).isEqualTo(0.25);
    }
}
