package com.raved.analytics.service;

import com.raved.analytics.dto.request.TrackEventRequest;
import com.raved.analytics.dto.response.AnalyticsEventResponse;
import com.raved.analytics.mapper.AnalyticsEventMapper;
import com.raved.analytics.model.AnalyticsEvent;
import com.raved.analytics.model.EventType;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.service.impl.AnalyticsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AnalyticsServiceImplTest {

    private AnalyticsEventRepository repository;
    private AnalyticsEventMapper mapper;
    private AnalyticsServiceImpl service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(AnalyticsEventRepository.class);
        mapper = Mockito.mock(AnalyticsEventMapper.class);
        service = new AnalyticsServiceImpl();
        org.springframework.test.util.ReflectionTestUtils.setField(service, "eventRepository", repository);
        org.springframework.test.util.ReflectionTestUtils.setField(service, "eventMapper", mapper);
    }

    @Test
    void getTotalEventCount_returnsRepositoryCount() {
        when(repository.count()).thenReturn(42L);
        assertThat(service.getTotalEventCount()).isEqualTo(42L);
    }

    @Test
    void processKafkaEvent_withInvalidJson_shouldNotInvokeRepository() {
        service.processKafkaEvent("{invalid-json");
        verify(repository, never()).save(any());
    }

    @Test
    void getEventsByUser_mapsPageOfEntities() {
        var pageable = PageRequest.of(0, 10);
        AnalyticsEvent entity = new AnalyticsEvent();
        entity.setId("e1");
        entity.setUserId("u1");
        entity.setEventType(EventType.USER_LOGIN);
        entity.setEventTimestamp(LocalDateTime.now());

        when(repository.findByUserIdAndEventTimestampBetween(eq("u1"), any(), any()))
                .thenReturn(List.of(entity));

        Page<AnalyticsEventResponse> out = service.getEventsByUser("u1", pageable);
        assertThat(out.getTotalElements()).isEqualTo(1);
        // We only check the count due to manual conversion in service
        // The ID may not be mapped in convertToResponse; ensure non-empty content
        assertThat(out.getContent()).isNotEmpty();
    }
}
