package com.raved.analytics.controller;

import com.raved.analytics.dto.request.ReportRequest;
import com.raved.analytics.dto.response.AnalyticsReportResponse;
import com.raved.analytics.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/analytics/reports", "/api/v1/analytics/reports"})
public class ReportsController {

    private final ReportService reportService;

    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<AnalyticsReportResponse> generateReport(@Valid @RequestBody ReportRequest request) {
        // Build parameters map safely (Map.of doesn't allow nulls)
        java.util.Map<String, Object> params = new java.util.HashMap<>();
        if (request.getStartDate() != null) {
            params.put("startDate", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            params.put("endDate", request.getEndDate());
        }
        if (request.getFilters() != null) {
            params.put("filters", request.getFilters());
        }
        if (request.getMetrics() != null) {
            params.put("metrics", request.getMetrics());
        }
        if (request.getReportFormat() != null) {
            params.put("format", request.getReportFormat().name());
        }

        // For now, route to custom report generator using ReportType as name
        AnalyticsReportResponse response = reportService.generateCustomReport(
                request.getReportType().name(), params);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/scheduled")
    public ResponseEntity<List<AnalyticsReportResponse>> getScheduledReports() {
        return ResponseEntity.ok(reportService.getScheduledReports());
    }
}
