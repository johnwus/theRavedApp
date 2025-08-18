package com.raved.analytics.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.raved.analytics.algorithm.BotPatternDetector;
import com.raved.analytics.algorithm.EngagementCalculator;
import com.raved.analytics.algorithm.TrendingAlgorithm;
import com.raved.analytics.model.ContentMetrics;
import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.model.UserMetrics;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.RankingSnapshotRepository;
import com.raved.analytics.repository.UserMetricsRepository;
import com.raved.analytics.service.RankingService;

@Service
public class RankingServiceImpl implements RankingService {

    private static final Logger logger = LoggerFactory.getLogger(RankingServiceImpl.class);

    @Autowired
    private UserMetricsRepository userMetricsRepository;
    @Autowired
    private ContentMetricsRepository contentMetricsRepository;
    @Autowired
    private RankingSnapshotRepository snapshotRepository;

    @Autowired
    private EngagementCalculator engagementCalculator;
    @Autowired
    private TrendingAlgorithm trendingAlgorithm;
    @Autowired
    private BotPatternDetector botPatternDetector;

    @Override
    public RankingSnapshot computeDailyUserEngagement(LocalDate date, int limit) {
        logger.info("Computing daily user engagement ranking for {}", date);
        List<UserMetrics> candidates = userMetricsRepository.findTopUsersByEngagement(PageRequest.of(0, Math.max(200, limit)));
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        RankingSnapshot snapshot = toUserSnapshot("ENGAGEMENT", "DAILY", date, candidates, limit,
                (u) -> userEngagementScore(u, start, end));
        return snapshotRepository.save(snapshot);
    }

    @Override
    public RankingSnapshot computeDailyUserInfluence(LocalDate date, int limit) {
        logger.info("Computing daily user influence ranking for {}", date);
        List<UserMetrics> candidates = userMetricsRepository.findTopUsersByInfluence(PageRequest.of(0, Math.max(200, limit)));
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        RankingSnapshot snapshot = toUserSnapshot("INFLUENCE", "DAILY", date, candidates, limit,
                (u) -> userInfluenceScore(u, start, end));
        return snapshotRepository.save(snapshot);
    }

    @Override
    public RankingSnapshot computeDailyContentVirality(LocalDate date, int limit) {
        logger.info("Computing daily content virality ranking for {}", date);
        List<ContentMetrics> candidates = contentMetricsRepository.findTopContentByViralScore(PageRequest.of(0, Math.max(200, limit)));
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        RankingSnapshot snapshot = toContentSnapshot("VIRALITY", "DAILY", date, candidates, limit,
                (c) -> contentViralityScore(c, start, end));
        return snapshotRepository.save(snapshot);
    }

    // WEEKLY
    public RankingSnapshot computeWeeklyUserEngagement(LocalDate date, int limit) {
        Window w = windowFor("WEEKLY", date);
        logger.info("Computing weekly user engagement ranking for week of {}", w.start.toLocalDate());
        List<UserMetrics> candidates = userMetricsRepository.findTopUsersByEngagement(PageRequest.of(0, Math.max(200, limit)));
        RankingSnapshot snapshot = toUserSnapshot("ENGAGEMENT", "WEEKLY", date, candidates, limit,
                (u) -> userEngagementScore(u, w.start, w.end));
        return snapshotRepository.save(snapshot);
    }

    public RankingSnapshot computeWeeklyUserInfluence(LocalDate date, int limit) {
        Window w = windowFor("WEEKLY", date);
        logger.info("Computing weekly user influence ranking for week of {}", w.start.toLocalDate());
        List<UserMetrics> candidates = userMetricsRepository.findTopUsersByInfluence(PageRequest.of(0, Math.max(200, limit)));
        RankingSnapshot snapshot = toUserSnapshot("INFLUENCE", "WEEKLY", date, candidates, limit,
                (u) -> userInfluenceScore(u, w.start, w.end));
        return snapshotRepository.save(snapshot);
    }

    public RankingSnapshot computeWeeklyContentVirality(LocalDate date, int limit) {
        Window w = windowFor("WEEKLY", date);
        logger.info("Computing weekly content virality ranking for week of {}", w.start.toLocalDate());
        List<ContentMetrics> candidates = contentMetricsRepository.findTopContentByViralScore(PageRequest.of(0, Math.max(200, limit)));
        RankingSnapshot snapshot = toContentSnapshot("VIRALITY", "WEEKLY", date, candidates, limit,
                (c) -> contentViralityScore(c, w.start, w.end));
        return snapshotRepository.save(snapshot);
    }

    // MONTHLY
    public RankingSnapshot computeMonthlyUserEngagement(LocalDate date, int limit) {
        Window w = windowFor("MONTHLY", date);
        logger.info("Computing monthly user engagement ranking for {}", w.start.toLocalDate().withDayOfMonth(1));
        List<UserMetrics> candidates = userMetricsRepository.findTopUsersByEngagement(PageRequest.of(0, Math.max(200, limit)));
        RankingSnapshot snapshot = toUserSnapshot("ENGAGEMENT", "MONTHLY", date, candidates, limit,
                (u) -> userEngagementScore(u, w.start, w.end));
        return snapshotRepository.save(snapshot);
    }

    public RankingSnapshot computeMonthlyUserInfluence(LocalDate date, int limit) {
        Window w = windowFor("MONTHLY", date);
        logger.info("Computing monthly user influence ranking for {}", w.start.toLocalDate().withDayOfMonth(1));
        List<UserMetrics> candidates = userMetricsRepository.findTopUsersByInfluence(PageRequest.of(0, Math.max(200, limit)));
        RankingSnapshot snapshot = toUserSnapshot("INFLUENCE", "MONTHLY", date, candidates, limit,
                (u) -> userInfluenceScore(u, w.start, w.end));
        return snapshotRepository.save(snapshot);
    }

    public RankingSnapshot computeMonthlyContentVirality(LocalDate date, int limit) {
        Window w = windowFor("MONTHLY", date);
        logger.info("Computing monthly content virality ranking for {}", w.start.toLocalDate().withDayOfMonth(1));
        List<ContentMetrics> candidates = contentMetricsRepository.findTopContentByViralScore(PageRequest.of(0, Math.max(200, limit)));
        RankingSnapshot snapshot = toContentSnapshot("VIRALITY", "MONTHLY", date, candidates, limit,
                (c) -> contentViralityScore(c, w.start, w.end));
        return snapshotRepository.save(snapshot);
    }

    @Override
    public RankingSnapshot computeRankings(String snapshotType, String metric, String period, LocalDate date, String category, int limit) {
        String p = period == null ? "DAILY" : period.toUpperCase();
        if ("USER".equalsIgnoreCase(snapshotType) && "ENGAGEMENT".equalsIgnoreCase(metric)) {
            return computeForUsers(metric, p, date, category, limit,
                    () -> userMetricsRepository.findTopUsersByEngagement(PageRequest.of(0, Math.max(200, limit))));
        }
        if ("USER".equalsIgnoreCase(snapshotType) && "INFLUENCE".equalsIgnoreCase(metric)) {
            return computeForUsers(metric, p, date, category, limit,
                    () -> userMetricsRepository.findTopUsersByInfluence(PageRequest.of(0, Math.max(200, limit))));
        }
        if ("CONTENT".equalsIgnoreCase(snapshotType) && "VIRALITY".equalsIgnoreCase(metric)) {
            return computeForContent(metric, p, date, category, limit,
                    () -> contentMetricsRepository.findTopContentByViralScore(PageRequest.of(0, Math.max(200, limit))));
        }
        throw new UnsupportedOperationException("computeRankings: unsupported combination: " + snapshotType + ", " + metric + ", " + period);
    }

    // --- Scoring functions with recency weighting, multi-signal, and bot penalties ---
    private BigDecimal userEngagementScore(UserMetrics u, LocalDateTime start, LocalDateTime end) {
        // Base: event-driven engagement in window (uses EngagementCalculator)
        BigDecimal eventRate = engagementCalculator.calculateUserEngagementRate(u.getUserId(), start, end);
        // Additional signals from metrics (likes/comments/shares/views if available)
        BigDecimal socialEng = BigDecimal.ZERO;
        if (u.getSocialMetrics() != null) {
            int likes = nvl(u.getSocialMetrics().getLikesReceived()).intValue();
            int comments = nvl(u.getSocialMetrics().getCommentsReceived()).intValue();
            int shares = nvl(u.getSocialMetrics().getSharesReceived()).intValue();
            int posts = nvl(u.getSocialMetrics().getPostsCount()).intValue();
            socialEng = BigDecimal.valueOf(likes * 2 + comments * 3 + shares * 4 + posts);
        }
        // Recency weighting: time since last active
        BigDecimal recency = BigDecimal.ONE;
        if (u.getLastActiveDate() != null) {
            long hours = Math.max(0, ChronoUnit.HOURS.between(u.getLastActiveDate(), end));
            recency = BigDecimal.valueOf(Math.max(0.25, Math.exp(-hours / 48.0))); // 2-day half-life approx
        }
        double botPenalty = botPatternDetector.penaltyForUser(u.getUserId(), end);
        BigDecimal score = eventRate.add(socialEng).multiply(recency).multiply(BigDecimal.valueOf(1.0 - botPenalty));
        return score.setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal userInfluenceScore(UserMetrics u, LocalDateTime start, LocalDateTime end) {
        // Influence includes followers and engagement velocity
        int followers = u.getSocialMetrics() != null ? nvl(u.getSocialMetrics().getFollowersCount()).intValue() : 0;
        BigDecimal recentEng = engagementCalculator.calculateUserEngagementRate(u.getUserId(), start, end);
        BigDecimal base = BigDecimal.valueOf(Math.log(followers + 1) * 10).add(recentEng);
        long hoursSinceActive = u.getLastActiveDate() != null ? Math.max(0, ChronoUnit.HOURS.between(u.getLastActiveDate(), end)) : 9999;
        BigDecimal decay = BigDecimal.valueOf(Math.max(0.2, Math.exp(-hoursSinceActive / 72.0))); // 3-day half-life approx
        double botPenalty = botPatternDetector.penaltyForUser(u.getUserId(), end);
        return base.multiply(decay).multiply(BigDecimal.valueOf(1.0 - botPenalty)).setScale(4, RoundingMode.HALF_UP);
    }

    private BigDecimal contentViralityScore(ContentMetrics c, LocalDateTime start, LocalDateTime end) {
        long views = nvl(c.getViewsCount()).longValue();
        long likes = nvl(c.getLikesCount()).longValue();
        long comments = nvl(c.getCommentsCount()).longValue();
        long shares = nvl(c.getSharesCount()).longValue();
        // Use TrendingAlgorithm for time-decay engagement
        double trending = trendingAlgorithm.calculateTrendingScore(views, likes, comments, shares, start);
        double botPenalty = botPatternDetector.penaltyForContent(c.getContentId(), end);
        return BigDecimal.valueOf(trending).multiply(BigDecimal.valueOf(1.0 - botPenalty)).setScale(4, RoundingMode.HALF_UP);
    }

    // --- Snapshot builders ---
    private interface ScoreFn<T> {

        BigDecimal score(T t);
    }

    private RankingSnapshot toUserSnapshot(String metric, String period, LocalDate date, List<UserMetrics> users, int limit, ScoreFn<UserMetrics> scoreFn) {
        RankingSnapshot snap = new RankingSnapshot();
        snap.setSnapshotType("USER");
        snap.setMetric(metric);
        snap.setPeriod(period);
        snap.setDate(date);
        snap.setComputedAt(LocalDateTime.now());
        List<UserMetrics> sorted = users.stream()
                .sorted(Comparator.comparing(scoreFn::score).reversed())
                .limit(limit)
                .collect(Collectors.toList());
        List<RankingSnapshot.Item> items = new ArrayList<>();
        int rank = 1;
        for (UserMetrics u : sorted) {
            RankingSnapshot.Item item = new RankingSnapshot.Item();
            item.setEntityId(u.getUserId());
            item.setRank(rank++);
            item.setScore(scoreFn.score(u));
            items.add(item);
        }
        snap.setItems(items);
        return snap;
    }

    private RankingSnapshot toContentSnapshot(String metric, String period, LocalDate date, List<ContentMetrics> content, int limit, ScoreFn<ContentMetrics> scoreFn) {
        RankingSnapshot snap = new RankingSnapshot();
        snap.setSnapshotType("CONTENT");
        snap.setMetric(metric);
        snap.setPeriod(period);
        snap.setDate(date);
        snap.setComputedAt(LocalDateTime.now());
        List<ContentMetrics> sorted = content.stream()
                .sorted(Comparator.comparing(scoreFn::score).reversed())
                .limit(limit)
                .collect(Collectors.toList());
        List<RankingSnapshot.Item> items = new ArrayList<>();
        int rank = 1;
        for (ContentMetrics c : sorted) {
            RankingSnapshot.Item item = new RankingSnapshot.Item();
            item.setEntityId(c.getContentId());
            item.setContentType(c.getContentType());
            item.setRank(rank++);
            item.setScore(scoreFn.score(c));
            items.add(item);
        }
        snap.setItems(items);
        return snap;
    }

    // --- Period window helpers and generic compute ---
    private static class Window {

        final LocalDateTime start;
        final LocalDateTime end;

        Window(LocalDateTime s, LocalDateTime e) {
            this.start = s;
            this.end = e;
        }
    }

    private Window windowFor(String period, LocalDate date) {
        switch (period) {
            case "DAILY": {
                LocalDateTime s = date.atStartOfDay();
                return new Window(s, s.plusDays(1));
            }
            case "WEEKLY": {
                LocalDate weekStart = date.minusDays((date.getDayOfWeek().getValue() + 6) % 7); // Monday start
                LocalDateTime s = weekStart.atStartOfDay();
                return new Window(s, s.plusWeeks(1));
            }
            case "MONTHLY": {
                LocalDate monthStart = date.withDayOfMonth(1);
                LocalDateTime s = monthStart.atStartOfDay();
                return new Window(s, s.plusMonths(1));
            }
            default:
                throw new UnsupportedOperationException("Unsupported period: " + period);
        }
    }

    private RankingSnapshot computeForUsers(String metric, String period, LocalDate date, String category, int limit, java.util.function.Supplier<List<UserMetrics>> loader) {
        Window w = windowFor(period, date);
        List<UserMetrics> candidates = loader.get();
        RankingSnapshot snap;
        if ("ENGAGEMENT".equalsIgnoreCase(metric)) {
            snap = toUserSnapshot("ENGAGEMENT", period, date, candidates, limit, u -> userEngagementScore(u, w.start, w.end));
        } else if ("INFLUENCE".equalsIgnoreCase(metric)) {
            snap = toUserSnapshot("INFLUENCE", period, date, candidates, limit, u -> userInfluenceScore(u, w.start, w.end));
        } else {
            throw new UnsupportedOperationException("Unsupported user metric: " + metric);
        }
        snap.setCategory(category);
        return snapshotRepository.save(snap);
    }

    private RankingSnapshot computeForContent(String metric, String period, LocalDate date, String category, int limit, java.util.function.Supplier<List<ContentMetrics>> loader) {
        Window w = windowFor(period, date);
        List<ContentMetrics> candidates = loader.get();
        if (!"VIRALITY".equalsIgnoreCase(metric)) {
            throw new UnsupportedOperationException("Unsupported content metric: " + metric);
        }
        RankingSnapshot snap = toContentSnapshot("VIRALITY", period, date, candidates, limit, c -> contentViralityScore(c, w.start, w.end));
        snap.setCategory(category);
        return snapshotRepository.save(snap);
    }

    private Integer nvl(Integer val) {
        return val == null ? 0 : val;
    }

    private Long nvl(Long val) {
        return val == null ? 0L : val;
    }

    private BigDecimal nvl(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
}
