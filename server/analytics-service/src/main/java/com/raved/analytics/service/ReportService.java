package com.raved.analytics.service;

import com.raved.analytics.dto.response.AnalyticsReportResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ReportService for TheRavedApp
 */
public interface ReportService {

    /**
     * Generate user engagement report
     */
    AnalyticsReportResponse generateUserEngagementReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Generate content performance report
     */
    AnalyticsReportResponse generateContentPerformanceReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Generate platform overview report
     */
    AnalyticsReportResponse generatePlatformOverviewReport(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * Generate custom report
     */
    AnalyticsReportResponse generateCustomReport(String reportName, Map<String, Object> parameters);

    /**
     * Get scheduled reports
     */
    List<AnalyticsReportResponse> getScheduledReports();

    /**
     * Schedule report
     */
    void scheduleReport(String reportType, String schedule, Map<String, Object> parameters);

    /**
     * Export report
     */
    void exportReport(AnalyticsReportResponse report, String format);
}
