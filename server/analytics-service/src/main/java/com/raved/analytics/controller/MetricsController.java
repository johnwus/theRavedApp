package com.raved.analytics.controller;

import com.raved.analytics.dto.response.ContentMetricsResponse;
import com.raved.analytics.dto.response.UserMetricsResponse;
import com.raved.analytics.service.MetricsService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/analytics/metrics", "/api/v1/analytics/metrics"})
public class MetricsController {

    private final MetricsService metricsService;

    public MetricsController(MetricsService metricsService) {
        this.metricsService = metricsService;
    }

    // User metrics
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserMetricsResponse> getUserMetrics(@PathVariable @NotBlank String userId) {
        return ResponseEntity.ok(metricsService.getUserMetrics(userId));
    }

    @PostMapping("/users/{userId}/recalculate")
    public ResponseEntity<Void> recalcUser(@PathVariable @NotBlank String userId) {
        metricsService.recalculateUserMetrics(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/trends")
    public ResponseEntity<Map<String, Object>> getUserTrends(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(metricsService.getUserEngagementTrends(userId, startDate, endDate));
    }

    // Content metrics
    @GetMapping("/content/{contentId}")
    public ResponseEntity<ContentMetricsResponse> getContentMetrics(@PathVariable String contentId) {
        return ResponseEntity.ok(metricsService.getContentMetrics(contentId));
    }

    @PostMapping("/content/{contentId}/recalculate")
    public ResponseEntity<Void> recalcContent(@PathVariable String contentId) {
        metricsService.recalculateContentMetrics(contentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/content/{contentId}/performance")
    public ResponseEntity<Map<String, Object>> getContentPerformance(
            @PathVariable String contentId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(metricsService.getContentPerformanceMetrics(contentId, startDate, endDate));
    }

    // Top rankings
    @GetMapping("/top/users")
    public ResponseEntity<Page<UserMetricsResponse>> topUsers(Pageable pageable) {
        return ResponseEntity.ok(metricsService.getTopUsersByEngagement(pageable));
    }

    @GetMapping("/top/content")
    public ResponseEntity<Page<ContentMetricsResponse>> topContent(Pageable pageable) {
        return ResponseEntity.ok(metricsService.getTopContentByEngagement(pageable));
    }

    // Platform metrics
    @GetMapping("/platform")
    public ResponseEntity<Map<String, Object>> platformMetrics() {
        return ResponseEntity.ok(metricsService.getPlatformMetrics());
    }

    @PostMapping("/platform/refresh")
    public ResponseEntity<Void> refreshPlatform() {
        metricsService.refreshPlatformMetrics();
        return ResponseEntity.ok().build();
    }
}
