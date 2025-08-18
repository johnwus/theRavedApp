package com.raved.content.controller;

import com.raved.content.dto.request.CreatePostRequest;
import com.raved.content.dto.request.UpdatePostRequest;
import com.raved.content.dto.response.PostResponse;
import com.raved.content.service.PostService;
import com.raved.content.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for post operations MongoDB
 */
@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostMapper postMapper;

    /**
     * Create a new post
     */
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest request) {
        var post = postMapper.toPost(request);
        var savedPost = postService.createPost(post);
        var response = postMapper.toPostResponse(savedPost);
        return ResponseEntity.ok(response);
    }

    /**
     * Get post by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable String id) {
        var post = postService.getPostById(id);
        if (post.isPresent()) {
            var response = postMapper.toPostResponse(post.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Update post
     */
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(@PathVariable String id, @RequestBody UpdatePostRequest request) {
        var existingPost = postService.getPostById(id);
        if (existingPost.isPresent()) {
            var updatedPost = postMapper.updatePostFromRequest(existingPost.get(), request);
            var savedPost = postService.updatePost(id, updatedPost);
            var response = postMapper.toPostResponse(savedPost);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Delete post
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable String id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get posts by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<PostResponse>> getPostsByUser(@PathVariable String userId, Pageable pageable) {
        var posts = postService.getPostsByUserId(userId, pageable);
        var responses = posts.map(postMapper::toPostResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get posts by faculty ID
     */
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<Page<PostResponse>> getPostsByFaculty(@PathVariable String facultyId, Pageable pageable) {
        var posts = postService.getPostsByFacultyId(facultyId, pageable);
        var responses = posts.map(postMapper::toPostResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get public posts
     */
    @GetMapping("/public")
    public ResponseEntity<Page<PostResponse>> getPublicPosts(Pageable pageable) {
        var posts = postService.getPublicPosts(pageable);
        var responses = posts.map(postMapper::toPostResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get trending posts
     */
    @GetMapping("/trending")
    public ResponseEntity<List<PostResponse>> getTrendingPosts(@RequestParam(defaultValue = "10") int limit) {
        var posts = postService.getTrendingPosts(limit);
        var responses = postMapper.toPostResponseList(posts);
        return ResponseEntity.ok(responses);
    }

    /**
     * Get featured posts
     */
    @GetMapping("/featured")
    public ResponseEntity<List<PostResponse>> getFeaturedPosts(@RequestParam(defaultValue = "10") int limit) {
        var posts = postService.getFeaturedPosts(limit);
        var responses = postMapper.toPostResponseList(posts);
        return ResponseEntity.ok(responses);
    }

    /**
     * Search posts
     */
    @GetMapping("/search")
    public ResponseEntity<Page<PostResponse>> searchPosts(@RequestParam String query, Pageable pageable) {
        var posts = postService.searchPosts(query, pageable);
        var responses = posts.map(postMapper::toPostResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Increment view count
     */
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable String id) {
        postService.incrementViewCount(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Flag post for moderation
     */
    @PostMapping("/{id}/flag")
    public ResponseEntity<Void> flagPost(@PathVariable String id, @RequestParam String reason) {
        postService.flagPost(id, reason);
        return ResponseEntity.ok().build();
    }

    /**
     * Feature post
     */
    @PostMapping("/{id}/feature")
    public ResponseEntity<Void> featurePost(@PathVariable String id, @RequestParam int durationHours) {
        postService.featurePost(id, durationHours);
        return ResponseEntity.ok().build();
    }

    /**
     * Pin post
     */
    @PostMapping("/{id}/pin")
    public ResponseEntity<Void> pinPost(@PathVariable String id) {
        postService.pinPost(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Unpin post
     */
    @PostMapping("/{id}/unpin")
    public ResponseEntity<Void> unpinPost(@PathVariable String id) {
        postService.unpinPost(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Moderate post
     */
    @PostMapping("/{id}/moderate")
    public ResponseEntity<Void> moderatePost(@PathVariable String id,
            @RequestParam String status,
            @RequestParam String reason,
            @RequestParam String moderatorId) {
        postService.moderatePost(id, status, reason, moderatorId);
        return ResponseEntity.ok().build();
    }
}
