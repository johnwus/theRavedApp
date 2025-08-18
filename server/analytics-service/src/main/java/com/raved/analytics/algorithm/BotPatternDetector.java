package com.raved.analytics.algorithm;

import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.repository.AnalyticsEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BotPatternDetector {

    @Autowired
    private AnalyticsEventRepository analyticsEventRepository;

    /**
     * Returns a penalty factor in [0.0, 0.5] to reduce scores for suspected bot-like behavior.
     * Heuristics:
     * - Very high actions in the last 10 minutes
     * - Highly repetitive event types (>90% same type)
     */
    public double penaltyForUser(String userId, LocalDateTime now) {
        LocalDateTime tenMinAgo = now.minusMinutes(10);
        List<AnalyticsEvent> events = analyticsEventRepository.findByUserIdAndEventTimestampBetween(userId, tenMinAgo, now);
        if (events.isEmpty()) return 0.0;

        int total = events.size();
        // Count dominant event type frequency
        long maxSameType = events.stream()
                .map(e -> e.getEventType() == null ? "UNKNOWN" : e.getEventType().name())
                .collect(java.util.stream.Collectors.groupingBy(s -> s, java.util.stream.Collectors.counting()))
                .values().stream().mapToLong(Long::longValue).max().orElse(0L);

        double ratePenalty = 0.0;
        if (total >= 300) ratePenalty = 0.35;
        else if (total >= 150) ratePenalty = 0.20;
        else if (total >= 80) ratePenalty = 0.10;

        double repetition = total == 0 ? 0.0 : (maxSameType * 1.0 / total);
        double repetitionPenalty = repetition >= 0.9 ? 0.15 : repetition >= 0.8 ? 0.08 : 0.0;

        return Math.min(0.5, ratePenalty + repetitionPenalty);
    }

    public double penaltyForContent(String contentId, LocalDateTime now) {
        LocalDateTime tenMinAgo = now.minusMinutes(10);
        List<AnalyticsEvent> events = analyticsEventRepository.findByEntityIdAndEventTimestampBetween(contentId, tenMinAgo, now);
        if (events.isEmpty()) return 0.0;
        int total = events.size();
        double ratePenalty = total >= 1000 ? 0.4 : total >= 500 ? 0.25 : total >= 200 ? 0.1 : 0.0;
        return Math.min(0.5, ratePenalty);
    }
}

