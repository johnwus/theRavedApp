package com.raved.social.controller;

import com.raved.social.dto.request.FollowRequest;
import com.raved.social.dto.response.FollowResponse;
import com.raved.social.service.FollowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/follows")
@CrossOrigin(origins = "*")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping
    public ResponseEntity<FollowResponse> follow(@Valid @RequestBody FollowRequest request) {
        return ResponseEntity.ok(followService.followUser(request));
    }

    @DeleteMapping
    public ResponseEntity<Void> unfollow(@Valid @RequestBody FollowRequest request) {
        Long followerId = com.raved.social.util.MongoIdConverter.toLongId(request.getFollowerId());
        Long followingId = com.raved.social.util.MongoIdConverter.toLongId(request.getFollowingId());
        followService.unfollowUser(followerId, followingId);
        return ResponseEntity.noContent().build();
    }
}
