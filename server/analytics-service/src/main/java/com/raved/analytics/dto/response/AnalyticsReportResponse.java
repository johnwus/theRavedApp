package com.raved.analytics.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for analytics reports
 */
public class AnalyticsReportResponse {

    private String reportType;
    private String reportName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime generatedAt;
    private Map<String, Object> data;
    private String status;
    private String format;

    // Constructors
    public AnalyticsReportResponse() {}

    public AnalyticsReportResponse(String reportType, Map<String, Object> data) {
        this.reportType = reportType;
        this.data = data;
        this.generatedAt = LocalDateTime.now();
        this.status = "COMPLETED";
    }

    // Getters and Setters
    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
