package com.raved.social.controller;

import com.raved.social.dto.request.LikeRequest;
import com.raved.social.dto.response.LikeResponse;
import com.raved.social.service.LikeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/likes")
@CrossOrigin(origins = "*")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping
    public ResponseEntity<LikeResponse> likeTarget(@Valid @RequestBody LikeRequest request) {
        return ResponseEntity.ok(likeService.likeTarget(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> unlikeTarget(@Valid @RequestBody LikeRequest request) {
        Long userId = com.raved.social.util.MongoIdConverter.toLongId(request.getUserId());
        Long targetId = com.raved.social.util.MongoIdConverter.toLongId(request.getTargetId());
        likeService.unlikeTarget(userId, targetId, request.getTargetType());
        return ResponseEntity.noContent().build();
    }
}
