package com.raved.analytics.controller;

import com.raved.analytics.dto.response.RankingPageResponse;
import com.raved.analytics.model.RankingSnapshot;
import com.raved.analytics.repository.RankingSnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/analytics/rankings", "/api/v1/analytics/rankings"})
@CrossOrigin(origins = "*")
public class RankingsController {

    @Autowired
    private RankingSnapshotRepository snapshotRepository;

    @GetMapping("/users/engagement")
    public ResponseEntity<RankingPageResponse> getUserEngagement(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "DAILY") String period,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(buildPage("USER", "ENGAGEMENT", date, period, category, page, size));
    }

    @GetMapping("/users/influence")
    public ResponseEntity<RankingPageResponse> getUserInfluence(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "DAILY") String period,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(buildPage("USER", "INFLUENCE", date, period, category, page, size));
    }

    @GetMapping("/content/virality")
    public ResponseEntity<RankingPageResponse> getContentVirality(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "DAILY") String period,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(buildPage("CONTENT", "VIRALITY", date, period, category, page, size));
    }

    private static final int MAX_PAGE_SIZE = 200;

    private RankingPageResponse buildPage(String type, String metric, LocalDate date, String period, String category, int page, int size) {
        String p = (period == null ? "DAILY" : period.toUpperCase());
        if (!p.equals("DAILY") && !p.equals("WEEKLY") && !p.equals("MONTHLY")) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid period");
        }
        if (page < 0 || size <= 0 || size > MAX_PAGE_SIZE) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid pagination parameters");
        }
        List<RankingSnapshot> snaps = snapshotRepository.findBySnapshotTypeAndMetricAndPeriodAndDate(type, metric, p, date);
        if (category != null && !category.isBlank()) {
            snaps = snaps.stream().filter(s -> category.equalsIgnoreCase(s.getCategory())).toList();
        }
        // Typically one snapshot per day/period/metric/type; merge all items if multiple
        List<RankingSnapshot.Item> all = snaps.stream().flatMap(s -> s.getItems().stream()).toList();
        int total = all.size();
        int from = Math.min(page * size, total);
        int to = Math.min(from + size, total);
        List<RankingSnapshot.Item> pageItems = all.subList(from, to);

        RankingPageResponse resp = new RankingPageResponse();
        resp.setSnapshotType(type);
        resp.setMetric(metric);
        resp.setPeriod(p);
        resp.setDate(date);
        resp.setCategory(category);
        resp.setPage(page);
        resp.setSize(size);
        resp.setTotalItems(total);
        resp.setItems(pageItems);
        return resp;
    }
}
