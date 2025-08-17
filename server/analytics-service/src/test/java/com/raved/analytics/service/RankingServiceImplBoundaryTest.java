package com.raved.analytics.service;

import com.raved.analytics.algorithm.BotPatternDetector;
import com.raved.analytics.algorithm.EngagementCalculator;
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

public class RankingServiceImplBoundaryTest {

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
        // minimal bot detector
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
    void monthly_window_endOfMonth_transition_ok() throws Exception {
        LocalDate date = LocalDate.of(2025, 1, 31);
        when(userRepo.findTopUsersByEngagement(any(PageRequest.class))).thenReturn(List.of(user("u1")));
        final LocalDateTime[] captured = new LocalDateTime[2];
        EngagementCalculator ec = new EngagementCalculator() {
            @Override public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime start, LocalDateTime end) {
                captured[0] = start; captured[1] = end; return new BigDecimal("5");
            }
        };
        inject(service, "engagementCalculator", ec);

        RankingSnapshot snap = service.computeRankings("USER","ENGAGEMENT","MONTHLY", date, null, 10);
        assertThat(snap.getPeriod()).isEqualTo("MONTHLY");
        assertThat(captured[0]).isEqualTo(LocalDate.of(2025,1,1).atStartOfDay());
        assertThat(captured[1]).isEqualTo(LocalDate.of(2025,2,1).atStartOfDay());
    }

    @Test
    void monthly_window_leapYear_feb29_ok() throws Exception {
        LocalDate date = LocalDate.of(2024, 2, 29);
        when(userRepo.findTopUsersByEngagement(any(PageRequest.class))).thenReturn(List.of(user("u2")));
        final LocalDateTime[] captured = new LocalDateTime[2];
        EngagementCalculator ec = new EngagementCalculator() {
            @Override public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime start, LocalDateTime end) {
                captured[0] = start; captured[1] = end; return new BigDecimal("7");
            }
        };
        inject(service, "engagementCalculator", ec);

        service.computeRankings("USER","ENGAGEMENT","MONTHLY", date, null, 5);
        assertThat(captured[0]).isEqualTo(LocalDate.of(2024,2,1).atStartOfDay());
        assertThat(captured[1]).isEqualTo(LocalDate.of(2024,3,1).atStartOfDay());
    }

    @Test
    void weekly_window_mondayStart_across_week_transition_ok() throws Exception {
        // Use a Sunday and ensure Monday start of that week
        LocalDate sunday = LocalDate.of(2025, 8, 17); // 2025-08-17 is a Sunday
        when(userRepo.findTopUsersByEngagement(any(PageRequest.class))).thenReturn(List.of(user("u3")));
        final LocalDateTime[] captured = new LocalDateTime[2];
        EngagementCalculator ec = new EngagementCalculator() {
            @Override public BigDecimal calculateUserEngagementRate(String userId, LocalDateTime start, LocalDateTime end) {
                captured[0] = start; captured[1] = end; return new BigDecimal("9");
            }
        };
        inject(service, "engagementCalculator", ec);

        service.computeRankings("USER","ENGAGEMENT","WEEKLY", sunday, null, 5);
        LocalDate monday = sunday.minusDays((sunday.getDayOfWeek().getValue() + 6) % 7);
        assertThat(monday.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(captured[0]).isEqualTo(monday.atStartOfDay());
        assertThat(captured[1]).isEqualTo(monday.atStartOfDay().plusWeeks(1));
    }
}

