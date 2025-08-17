package com.raved.analytics.service;

import com.raved.analytics.dto.response.AnalyticsReportResponse;
import com.raved.analytics.repository.AnalyticsEventRepository;
import com.raved.analytics.repository.ContentMetricsRepository;
import com.raved.analytics.repository.UserMetricsRepository;
import com.raved.analytics.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReportServiceImplTest {

    @Mock
    private AnalyticsEventRepository eventRepository;
    @Mock
    private UserMetricsRepository userMetricsRepository;
    @Mock
    private ContentMetricsRepository contentMetricsRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateUserEngagementReport_returnsReport() {
        Mockito.when(userMetricsRepository.countActiveUsersBetween(Mockito.any(), Mockito.any()))
                .thenReturn(10L);
        Mockito.when(userMetricsRepository.countNewUsersBetween(Mockito.any(), Mockito.any()))
                .thenReturn(5L);
        Mockito.when(eventRepository.getDailyActiveUsers(Mockito.any(), Mockito.any()))
                .thenReturn(java.util.Collections.emptyList());

        AnalyticsReportResponse response = reportService.generateUserEngagementReport(LocalDateTime.now().minusDays(7), LocalDateTime.now());
        assertNotNull(response);
    }
}
