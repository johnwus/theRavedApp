package com.raved.social.controller;

import com.raved.social.dto.request.CreateActivityRequest;
import com.raved.social.dto.response.ActivityResponse;
import com.raved.social.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activity")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> create(@Valid @RequestBody CreateActivityRequest request) {
        return ResponseEntity.ok(activityService.createActivity(request));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<ActivityResponse>> getUserActivity(@PathVariable String userId, Pageable pageable) {
        return ResponseEntity.ok(activityService.getActivityForUser(userId, pageable));
    }
}
