package com.raved.social.controller;

import com.raved.social.dto.request.CreateCommentRequest;
import com.raved.social.dto.request.UpdateCommentRequest;
import com.raved.social.dto.response.CommentResponse;
import com.raved.social.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody CreateCommentRequest request) {
        return ResponseEntity.ok(commentService.createComment(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> update(@PathVariable String id, @Valid @RequestBody UpdateCommentRequest request) {
        return ResponseEntity.ok(commentService.updateComment(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentResponse>> listByPost(@PathVariable String postId) {
        return ResponseEntity.ok(commentService.getCommentsByPost(postId));
    }
}
