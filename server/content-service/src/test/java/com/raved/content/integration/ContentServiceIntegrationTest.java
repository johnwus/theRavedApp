package com.raved.content.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.content.dto.request.CreatePostRequest;
import com.raved.content.model.Post;
import com.raved.content.model.PostStatus;
import com.raved.content.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for Content Service
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
public class ContentServiceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        postRepository.deleteAll();
    }

    @Test
    void testCreatePost() throws Exception {
        CreatePostRequest createRequest = new CreatePostRequest();
        createRequest.setTitle("Test Post");
        createRequest.setContent("This is a test post content");
        createRequest.setAuthorId("user123");
        createRequest.setCategory("TECHNOLOGY");
        createRequest.setTags(Arrays.asList("test", "integration"));

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Test Post")))
                .andExpect(jsonPath("$.content", is("This is a test post content")))
                .andExpect(jsonPath("$.authorId", is("user123")))
                .andExpect(jsonPath("$.category", is("TECHNOLOGY")))
                .andExpect(jsonPath("$.tags", hasSize(2)))
                .andExpect(jsonPath("$.tags", containsInAnyOrder("test", "integration")));
    }

    @Test
    void testGetPostById() throws Exception {
        // Create a test post
        Post post = new Post();
        post.setTitle("Test Post");
        post.setContent("Test content");
        post.setAuthorId("user123");
        post.setCategory("TECHNOLOGY");
        post.setStatus(PostStatus.PUBLISHED);
        post.setCreatedAt(LocalDateTime.now());
        post.setTags(Arrays.asList("test"));
        Post savedPost = postRepository.save(post);

        // Test getting post by ID
        mockMvc.perform(get("/api/posts/{id}", savedPost.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Test Post")))
                .andExpect(jsonPath("$.content", is("Test content")))
                .andExpect(jsonPath("$.authorId", is("user123")))
                .andExpect(jsonPath("$.category", is("TECHNOLOGY")));
    }

    @Test
    void testUpdatePost() throws Exception {
        // Create a test post
        Post post = new Post();
        post.setTitle("Original Title");
        post.setContent("Original content");
        post.setAuthorId("user123");
        post.setCategory("TECHNOLOGY");
        post.setStatus(PostStatus.DRAFT);
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(post);

        // Update the post
        CreatePostRequest updateRequest = new CreatePostRequest();
        updateRequest.setTitle("Updated Title");
        updateRequest.setContent("Updated content");
        updateRequest.setAuthorId("user123");
        updateRequest.setCategory("SCIENCE");
        updateRequest.setTags(Arrays.asList("updated", "test"));

        mockMvc.perform(put("/api/posts/{id}", savedPost.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.content", is("Updated content")))
                .andExpect(jsonPath("$.category", is("SCIENCE")));
    }

    @Test
    void testDeletePost() throws Exception {
        // Create a test post
        Post post = new Post();
        post.setTitle("Post to Delete");
        post.setContent("This post will be deleted");
        post.setAuthorId("user123");
        post.setCategory("TECHNOLOGY");
        post.setStatus(PostStatus.PUBLISHED);
        post.setCreatedAt(LocalDateTime.now());
        Post savedPost = postRepository.save(post);

        // Delete the post
        mockMvc.perform(delete("/api/posts/{id}", savedPost.getId()))
                .andExpect(status().isNoContent());

        // Verify post is deleted
        mockMvc.perform(get("/api/posts/{id}", savedPost.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetPostsByAuthor() throws Exception {
        // Create test posts by different authors
        Post post1 = new Post();
        post1.setTitle("Post by User 1");
        post1.setContent("Content by user 1");
        post1.setAuthorId("user1");
        post1.setCategory("TECHNOLOGY");
        post1.setStatus(PostStatus.PUBLISHED);
        post1.setCreatedAt(LocalDateTime.now());
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Post by User 2");
        post2.setContent("Content by user 2");
        post2.setAuthorId("user2");
        post2.setCategory("SCIENCE");
        post2.setStatus(PostStatus.PUBLISHED);
        post2.setCreatedAt(LocalDateTime.now());
        postRepository.save(post2);

        Post post3 = new Post();
        post3.setTitle("Another Post by User 1");
        post3.setContent("Another content by user 1");
        post3.setAuthorId("user1");
        post3.setCategory("TECHNOLOGY");
        post3.setStatus(PostStatus.PUBLISHED);
        post3.setCreatedAt(LocalDateTime.now());
        postRepository.save(post3);

        // Test getting posts by author
        mockMvc.perform(get("/api/posts")
                .param("authorId", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[*].authorId", everyItem(is("user1"))));
    }

    @Test
    void testGetPostsByCategory() throws Exception {
        // Create test posts in different categories
        Post techPost = new Post();
        techPost.setTitle("Tech Post");
        techPost.setContent("Technology content");
        techPost.setAuthorId("user1");
        techPost.setCategory("TECHNOLOGY");
        techPost.setStatus(PostStatus.PUBLISHED);
        techPost.setCreatedAt(LocalDateTime.now());
        postRepository.save(techPost);

        Post sciencePost = new Post();
        sciencePost.setTitle("Science Post");
        sciencePost.setContent("Science content");
        sciencePost.setAuthorId("user2");
        sciencePost.setCategory("SCIENCE");
        sciencePost.setStatus(PostStatus.PUBLISHED);
        sciencePost.setCreatedAt(LocalDateTime.now());
        postRepository.save(sciencePost);

        // Test getting posts by category
        mockMvc.perform(get("/api/posts")
                .param("category", "TECHNOLOGY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].category", is("TECHNOLOGY")));
    }

    @Test
    void testPostNotFound() throws Exception {
        // Test getting non-existent post
        mockMvc.perform(get("/api/posts/{id}", "nonexistent-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testInvalidPostCreation() throws Exception {
        // Test creating post with invalid data
        CreatePostRequest invalidRequest = new CreatePostRequest();
        invalidRequest.setTitle(""); // Empty title
        invalidRequest.setContent(""); // Empty content
        invalidRequest.setAuthorId(""); // Empty author ID

        mockMvc.perform(post("/api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}
