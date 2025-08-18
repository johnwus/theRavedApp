package com.raved.analytics.repository;

import com.raved.analytics.model.PageView;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PageViewRepository extends MongoRepository<PageView, String> {
    List<PageView> findByUserIdAndViewedAtBetween(String userId, LocalDateTime start, LocalDateTime end);
}

