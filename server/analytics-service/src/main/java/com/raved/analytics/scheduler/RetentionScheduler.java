package com.raved.analytics.scheduler;

import com.raved.analytics.repository.AnalyticsEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
public class RetentionScheduler {

    private static final Logger logger = LoggerFactory.getLogger(RetentionScheduler.class);

    private final AnalyticsEventRepository eventRepository;

    @Value("${analytics.processing.retention-days:365}")
    private int retentionDays;

    public RetentionScheduler(AnalyticsEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Weekly cleanup on Sundays at 03:00
    @Scheduled(cron = "0 0 3 * * SUN")
    public void cleanupOldEvents() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(retentionDays);
        long deleted = eventRepository.deleteByEventTimestampBefore(cutoff);
        logger.info("Retention job removed {} events older than {} days", deleted, retentionDays);
    }
}

