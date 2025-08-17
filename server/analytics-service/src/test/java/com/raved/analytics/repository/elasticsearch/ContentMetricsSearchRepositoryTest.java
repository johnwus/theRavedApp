package com.raved.analytics.repository.elasticsearch;

import com.raved.analytics.model.elasticsearch.ContentMetricsDocument;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ContentMetricsSearchRepositoryTest {

    @Test
    void repositoryInterfaceCompilesAndAllowsMethodCalls() {
        // This is a lightweight sanity test to ensure the interface and signature compile and is proxyable
        ContentMetricsSearchRepository repo = Mockito.mock(ContentMetricsSearchRepository.class);
        Mockito.when(repo.findByContentId("c1")).thenReturn(List.of(new ContentMetricsDocument()));
        List<ContentMetricsDocument> result = repo.findByContentId("c1");
        assertThat(result).hasSize(1);
    }
}

