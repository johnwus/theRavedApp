package com.raved.analytics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.analytics.dto.request.ReportRequest;
import com.raved.analytics.dto.request.ReportRequest.ReportFormat;
import com.raved.analytics.dto.request.ReportRequest.ReportType;
import com.raved.analytics.dto.response.AnalyticsReportResponse;
import com.raved.analytics.service.ReportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReportsController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc(addFilters = false)
class ReportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReportService reportService;

    @Test
    void generateReport_shouldReturn200() throws Exception {
        ReportRequest req = new ReportRequest();
        req.setReportType(ReportType.CUSTOM_METRICS);
        req.setReportFormat(ReportFormat.JSON);
        req.setStartDate(java.time.LocalDateTime.now().minusDays(7));
        req.setEndDate(java.time.LocalDateTime.now());
        Mockito.when(reportService.generateCustomReport(Mockito.anyString(), Mockito.anyMap()))
                .thenReturn(new AnalyticsReportResponse());

        mockMvc.perform(post("/api/analytics/reports/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    void getScheduledReports_shouldReturn200() throws Exception {
        Mockito.when(reportService.getScheduledReports()).thenReturn(List.of(new AnalyticsReportResponse()));
        mockMvc.perform(get("/api/analytics/reports/scheduled").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }
}
