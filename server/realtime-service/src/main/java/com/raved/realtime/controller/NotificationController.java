package com.raved.realtime.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    @PostMapping("/broadcast")
    public ResponseEntity<Void> broadcast(@RequestParam String roomId, @RequestParam String event, @RequestBody String payload) {
        // TODO: inject message broker and dispatch broadcast
        return ResponseEntity.accepted().build();
    }
}
