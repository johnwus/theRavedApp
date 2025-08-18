package com.raved.analytics.service;

import com.raved.analytics.model.RankingSnapshot;

import java.time.LocalDate;

public interface RankingService {

    RankingSnapshot computeDailyUserEngagement(LocalDate date, int limit);

    RankingSnapshot computeDailyUserInfluence(LocalDate date, int limit);

    RankingSnapshot computeDailyContentVirality(LocalDate date, int limit);

    // Extended API
    RankingSnapshot computeRankings(String snapshotType, String metric, String period, LocalDate date, String category, int limit);
}
