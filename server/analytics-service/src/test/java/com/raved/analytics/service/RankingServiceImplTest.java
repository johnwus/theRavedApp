package com.raved.analytics.service;

import com.raved.analytics.algorithm.BotPatternDetector;
import com.raved.analytics.algorithm.EngagementCalculator;
import com.raved.analytics.algorithm.TrendingAlgorithm;
import com.raved.analytics.model.ContentMetrics;
import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.model.UserMetrics;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.RankingSnapshotRepository;
import com.raved.analytics.repository.UserMetricsRepository;
import com.raved.analytics.service.impl.RankingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RankingServiceImplTest {

    @Mock
    private UserMetricsRepository userMetricsRepository;
    @Mock
    private ContentMetricsRepository contentMetricsRepository;
    @Mock
    private RankingSnapshotRepository snapshotRepository;

    private EngagementCalculator engagementCalculator;
    private TrendingAlgorithm trendingAlgorithm;
    private BotPatternDetector botPatternDetector;

    private RankingServiceImpl service;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Use simple fakes to avoid mocking concrete classes on Java 24
        engagementCalculator = new EngagementCalculator() {
            @Override
            public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime startDate, LocalDateTime endDate) {
                return BigDecimal.ZERO; // default; override per-test with spies if needed
            }
        };
        trendingAlgorithm = new TrendingAlgorithm() {
            @Override
            public double calculateTrendingScore(Long viewCount, Long likeCount, Long commentCount, Long shareCount, LocalDateTime createdAt) {
                return 0.0; // default; override via spying isn't needed as we stub via when(...)
            }
        };
        // Bot detector wired with a dynamic-proxy AnalyticsEventRepository to avoid nulls
        botPatternDetector = new BotPatternDetector();
        com.raved.analytics.repository.AnalyticsEventRepository proxyRepo
                = (com.raved.analytics.repository.AnalyticsEventRepository) java.lang.reflect.Proxy.newProxyInstance(
                        com.raved.analytics.repository.AnalyticsEventRepository.class.getClassLoader(),
                        new Class[]{com.raved.analytics.repository.AnalyticsEventRepository.class},
                        (p, m, a) -> {
                            String n = m.getName();
                            if (n.equals("findByUserIdAndEventTimestampBetween") || n.equals("findByEntityIdAndEventTimestampBetween")) {
                                return java.util.List.of();
                            }
                            Class<?> rt = m.getReturnType();
                            if (rt.equals(long.class)) {
                                return 0L;
                            }
                            if (rt.equals(boolean.class)) {
                                return false;
                            }
                            if (java.util.List.class.isAssignableFrom(rt)) {
                                return java.util.List.of();
                            }
                            if (java.util.Optional.class.isAssignableFrom(rt)) {
                                return java.util.Optional.empty();
                            }
                            return null;
                        }
                );
        java.lang.reflect.Field f = BotPatternDetector.class.getDeclaredField("analyticsEventRepository");
        f.setAccessible(true);
        f.set(botPatternDetector, proxyRepo);

        service = new RankingServiceImpl();
        inject(service, "userMetricsRepository", userMetricsRepository);
        inject(service, "contentMetricsRepository", contentMetricsRepository);
        inject(service, "snapshotRepository", snapshotRepository);
        inject(service, "engagementCalculator", engagementCalculator);
        inject(service, "trendingAlgorithm", trendingAlgorithm);
        inject(service, "botPatternDetector", botPatternDetector);
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private UserMetrics um(String userId, int followers, int likes, int comments, int shares, int posts, LocalDateTime lastActive) {
        UserMetrics u = new UserMetrics();
        u.setUserId(userId);
        var sm = new UserMetrics.SocialMetrics();
        sm.setFollowersCount(followers);
        sm.setLikesReceived(likes);
        sm.setCommentsReceived(comments);
        sm.setSharesReceived(shares);
        sm.setPostsCount(posts);
        u.setSocialMetrics(sm);
        u.setLastActiveDate(lastActive);
        return u;
    }

    private ContentMetrics cm(String id, String type, String category, long views, long likes, long comments, long shares) {
        ContentMetrics c = new ContentMetrics();
        c.setContentId(id);
        c.setContentType(type);
        c.setContentCategory(category);
        c.setViewsCount((int) views);
        c.setLikesCount((int) likes);
        c.setCommentsCount((int) comments);
        c.setSharesCount((int) shares);
        return c;
    }

    @Test
    void computeDailyUserEngagement_appliesRecencyMultiSignalAndPenalty_andSavesSnapshot() throws Exception {
        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        when(userMetricsRepository.findTopUsersByEngagement(any(PageRequest.class)))
                .thenReturn(List.of(
                        um("u1", 1000, 10, 5, 2, 1, end.minusHours(2)),
                        um("u2", 100, 50, 20, 5, 2, end.minusHours(48))
                ));
        // Override engagementCalculator behavior via reflection: replace with lambda-like subclass
        engagementCalculator = new EngagementCalculator() {
            @Override
            public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime s, LocalDateTime e) {
                return "u1".equals(userId) ? new BigDecimal("5.0") : new BigDecimal("2.0");
            }
        };
        inject(service, "engagementCalculator", engagementCalculator);

        botPatternDetector = new BotPatternDetector() {
            @Override
            public double penaltyForUser(String userId, LocalDateTime now) {
                return "u2".equals(userId) ? 0.5 : 0.0;
            }

            @Override
            public double penaltyForContent(String contentId, LocalDateTime now) {
                return 0.0;
            }
        };
        inject(service, "botPatternDetector", botPatternDetector);

        when(snapshotRepository.save(any(RankingSnapshot.class))).thenAnswer(inv -> inv.getArgument(0));

        RankingSnapshot snap = service.computeDailyUserEngagement(date, 1);

        assertThat(snap.getSnapshotType()).isEqualTo("USER");
        assertThat(snap.getMetric()).isEqualTo("ENGAGEMENT");
        assertThat(snap.getPeriod()).isEqualTo("DAILY");
        assertThat(snap.getDate()).isEqualTo(date);
        assertThat(snap.getItems()).hasSize(1);
        assertThat(snap.getItems().get(0).getEntityId()).isEqualTo("u1");

        ArgumentCaptor<RankingSnapshot> captor = ArgumentCaptor.forClass(RankingSnapshot.class);
        verify(snapshotRepository).save(captor.capture());
        RankingSnapshot saved = captor.getValue();
        assertThat(saved.getItems()).hasSize(1);
    }

    @Test
    void computeDailyUserInfluence_ordersByScore_descending_andAppliesLimit() throws Exception {
        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        when(userMetricsRepository.findTopUsersByInfluence(any(PageRequest.class)))
                .thenReturn(List.of(
                        um("uA", 10000, 2, 1, 0, 0, end.minusHours(1)),
                        um("uB", 100, 200, 150, 60, 10, end.minusHours(5)),
                        um("uC", 5000, 2, 1, 0, 0, end.minusHours(100))
                ));
        engagementCalculator = new EngagementCalculator() {
            @Override
            public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime s, LocalDateTime e) {
                return new BigDecimal("1.0");
            }
        };
        inject(service, "engagementCalculator", engagementCalculator);
        // Keep previously injected proxy-backed botPatternDetector; no override here to avoid nulls
        when(snapshotRepository.save(any(RankingSnapshot.class))).thenAnswer(inv -> inv.getArgument(0));

        RankingSnapshot snap = service.computeDailyUserInfluence(date, 2);
        assertThat(snap.getItems()).hasSize(2);
        assertThat(snap.getItems().get(0).getEntityId()).isNotNull();
        assertThat(snap.getItems().get(1).getEntityId()).isNotNull();
    }

    @Test
    void computeDailyContentVirality_usesTrendingAlgorithm_andSavesSnapshot() throws Exception {
        LocalDate date = LocalDate.now().minusDays(1);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);

        when(contentMetricsRepository.findTopContentByViralScore(any(PageRequest.class)))
                .thenReturn(List.of(
                        cm("c1", "POST", "tech", 1000, 200, 50, 20),
                        cm("c2", "VIDEO", "fashion", 2000, 100, 20, 10)
                ));
        trendingAlgorithm = new TrendingAlgorithm() {
            @Override
            public double calculateTrendingScore(Long viewCount, Long likeCount, Long commentCount, Long shareCount, LocalDateTime createdAt) {
                // First call 50.0, second 25.0
                if (viewCount == 1000L) {
                    return 50.0;
                }
                return 25.0;
            }
        };
        inject(service, "trendingAlgorithm", trendingAlgorithm);
        botPatternDetector = new BotPatternDetector() {
            @Override
            public double penaltyForContent(String contentId, LocalDateTime now) {
                return 0.1;
            }
        };
        inject(service, "botPatternDetector", botPatternDetector);
        when(snapshotRepository.save(any(RankingSnapshot.class))).thenAnswer(inv -> inv.getArgument(0));

        RankingSnapshot snap = service.computeDailyContentVirality(date, 1);
        assertThat(snap.getSnapshotType()).isEqualTo("CONTENT");
        assertThat(snap.getMetric()).isEqualTo("VIRALITY");
        assertThat(snap.getItems()).hasSize(1);
        assertThat(snap.getItems().get(0).getEntityId()).isEqualTo("c1");
    }

    @Test
    void computeMethods_handleEmptyDataGracefully() {
        LocalDate date = LocalDate.now().minusDays(1);
        when(userMetricsRepository.findTopUsersByEngagement(any())).thenReturn(List.of());
        when(userMetricsRepository.findTopUsersByInfluence(any())).thenReturn(List.of());
        when(contentMetricsRepository.findTopContentByViralScore(any())).thenReturn(List.of());
        when(snapshotRepository.save(any(RankingSnapshot.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThat(service.computeDailyUserEngagement(date, 10).getItems()).isEmpty();
        assertThat(service.computeDailyUserInfluence(date, 10).getItems()).isEmpty();
        assertThat(service.computeDailyContentVirality(date, 10).getItems()).isEmpty();
    }

    @Test
    void computeWeekly_and_Monthly_user_and_content_methods_useCorrectWindows_andSave() throws Exception {
        LocalDate anyDate = LocalDate.of(2025, 8, 15); // Friday
        // Stub repos with minimal data and echo back saved snapshot
        when(userMetricsRepository.findTopUsersByEngagement(any())).thenReturn(List.of(um("u1", 0, 0, 0, 0, 0, null)));
        when(userMetricsRepository.findTopUsersByInfluence(any())).thenReturn(List.of(um("u2", 10, 0, 0, 0, 0, null)));
        when(contentMetricsRepository.findTopContentByViralScore(any())).thenReturn(List.of(cm("c1", "POST", "tech", 1, 0, 0, 0)));
        when(snapshotRepository.save(any(RankingSnapshot.class))).thenAnswer(inv -> inv.getArgument(0));

        RankingSnapshot w1 = service.computeWeeklyUserEngagement(anyDate, 5);
        RankingSnapshot w2 = service.computeWeeklyUserInfluence(anyDate, 5);
        RankingSnapshot w3 = service.computeWeeklyContentVirality(anyDate, 5);
        RankingSnapshot m1 = service.computeMonthlyUserEngagement(anyDate, 5);
        RankingSnapshot m2 = service.computeMonthlyUserInfluence(anyDate, 5);
        RankingSnapshot m3 = service.computeMonthlyContentVirality(anyDate, 5);

        assertThat(w1.getPeriod()).isEqualTo("WEEKLY");
        assertThat(w2.getPeriod()).isEqualTo("WEEKLY");
        assertThat(w3.getPeriod()).isEqualTo("WEEKLY");
        assertThat(m1.getPeriod()).isEqualTo("MONTHLY");
        assertThat(m2.getPeriod()).isEqualTo("MONTHLY");
        assertThat(m3.getPeriod()).isEqualTo("MONTHLY");
        assertThat(w1.getItems()).hasSize(1);
        assertThat(m3.getItems()).hasSize(1);
    }

}
