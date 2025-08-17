package com.raved.analytics.scheduler;

import com.raved.analytics.service.RankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@EnableScheduling
public class RankingsScheduler {

    private static final Logger logger = LoggerFactory.getLogger(RankingsScheduler.class);

    private final RankingService rankingService;

    public RankingsScheduler(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    // Daily at 02:00
    @Scheduled(cron = "0 0 2 * * *")
    public void computeDailyRankings() {
        logger.info("Computing daily rankings at {}", LocalDateTime.now());
        LocalDate date = LocalDate.now().minusDays(1); // compute for yesterday
        int limit = 100;
        rankingService.computeDailyUserEngagement(date, limit);
        rankingService.computeDailyUserInfluence(date, limit);
        rankingService.computeDailyContentVirality(date, limit);
    }
}
