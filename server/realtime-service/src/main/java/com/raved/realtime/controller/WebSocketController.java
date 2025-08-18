package com.raved.realtime.controller;

import com.raved.realtime.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ws")
@CrossOrigin(origins = "*")
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    @PostMapping("/rooms/{roomId}/send")
    public ResponseEntity<Void> sendToRoom(@PathVariable String roomId, @RequestParam String event, @RequestBody String payload) {
        webSocketService.sendToRoom(roomId, event, payload);
        return ResponseEntity.accepted().build();
    }
}
