package com.raved.analytics.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AnalyticsEventSearchRepository extends ElasticsearchRepository<AnalyticsEventDocument, String> {

    Page<AnalyticsEventDocument> findByUserId(String userId, Pageable pageable);

    Page<AnalyticsEventDocument> findByEventType(String eventType, Pageable pageable);

    Page<AnalyticsEventDocument> findByHashtagsContaining(String hashtag, Pageable pageable);

    Page<AnalyticsEventDocument> findByContentTagsContaining(String tag, Pageable pageable);
}
