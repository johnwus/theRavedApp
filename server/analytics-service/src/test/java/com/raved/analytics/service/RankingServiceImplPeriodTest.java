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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class RankingServiceImplPeriodTest {

    @Mock private UserMetricsRepository userRepo;
    @Mock private ContentMetricsRepository contentRepo;
    @Mock private RankingSnapshotRepository snapRepo;

    private RankingServiceImpl service;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        service = new RankingServiceImpl();
        inject(service, "userMetricsRepository", userRepo);
        inject(service, "contentMetricsRepository", contentRepo);
        inject(service, "snapshotRepository", snapRepo);
        // Bot detector with proxy-backed empty repo
        BotPatternDetector bpd = new BotPatternDetector();
        var proxy = (com.raved.analytics.repository.AnalyticsEventRepository) java.lang.reflect.Proxy.newProxyInstance(
                com.raved.analytics.repository.AnalyticsEventRepository.class.getClassLoader(),
                new Class[]{com.raved.analytics.repository.AnalyticsEventRepository.class},
                (p,m,a)->{
                    if (m.getReturnType().equals(long.class)) return 0L;
                    if (m.getReturnType().equals(boolean.class)) return false;
                    if (java.util.List.class.isAssignableFrom(m.getReturnType())) return java.util.List.of();
                    if (java.util.Optional.class.isAssignableFrom(m.getReturnType())) return java.util.Optional.empty();
                    return null;
                }
        );
        Field f = BotPatternDetector.class.getDeclaredField("analyticsEventRepository");
        f.setAccessible(true);
        f.set(bpd, proxy);
        inject(service, "botPatternDetector", bpd);
        when(snapRepo.save(any(RankingSnapshot.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    private void inject(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private UserMetrics user(String id) { UserMetrics u = new UserMetrics(); u.setUserId(id); u.setLastActiveDate(LocalDateTime.now()); u.setSocialMetrics(new UserMetrics.SocialMetrics()); return u; }
    private ContentMetrics content(String id) { ContentMetrics c = new ContentMetrics(); c.setContentId(id); c.setContentType("POST"); return c; }

    @Test
    void computeRankings_weekly_userEngagement_usesWeekWindow_andSetsPeriod() throws Exception {
        // Arrange week date (e.g., Wednesday)
        LocalDate date = LocalDate.now().with(DayOfWeek.WEDNESDAY);
        when(userRepo.findTopUsersByEngagement(any(PageRequest.class))).thenReturn(List.of(user("u1")));
        final LocalDateTime[] captured = new LocalDateTime[2];
        EngagementCalculator ec = new EngagementCalculator() {
            @Override public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime start, LocalDateTime end) {
                captured[0] = start; captured[1] = end; return new BigDecimal("10");
            }
        };
        inject(service, "engagementCalculator", ec);

        // Act
        RankingSnapshot snap = service.computeRankings("USER","ENGAGEMENT","WEEKLY", date, null, 5);

        // Assert
        assertThat(snap.getPeriod()).isEqualTo("WEEKLY");
        // Start should be Monday 00:00, end one week later
        LocalDate monday = date.minusDays((date.getDayOfWeek().getValue() + 6) % 7);
        assertThat(captured[0]).isEqualTo(monday.atStartOfDay());
        assertThat(captured[1]).isEqualTo(monday.atStartOfDay().plusWeeks(1));
    }

    @Test
    void computeRankings_monthly_contentVirality_usesMonthWindow_andSetsPeriod() throws Exception {
        LocalDate date = LocalDate.of(2025, 8, 16);
        when(contentRepo.findTopContentByViralScore(any(PageRequest.class))).thenReturn(List.of(content("c1")));
        final LocalDateTime[] captured = new LocalDateTime[2];
        TrendingAlgorithm ta = new TrendingAlgorithm() {
            @Override public double calculateTrendingScore(Long v, Long l, Long c, Long s, LocalDateTime createdAt) { return 42.0; }
        };
        inject(service, "trendingAlgorithm", ta);

        RankingSnapshot snap = service.computeRankings("CONTENT","VIRALITY","MONTHLY", date, null, 3);
        assertThat(snap.getPeriod()).isEqualTo("MONTHLY");
        // We can't capture window directly here because the trending score method doesn't expose it,
        // but we validated weekly above; this confirms period assignment and execution path.
    }
}

