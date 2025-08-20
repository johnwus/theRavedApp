package com.raved.user.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.raved.user.dto.request.RegistrationRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.model.User;
import com.raved.user.model.UserStatus;
import com.raved.user.repository.UserRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for User Service
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@Transactional
public class UserServiceIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        userRepository.deleteAll();
    }

    @Test
    void testUserRegistrationFlow() throws Exception {
        // Test user registration
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setFirstName("John");
        registrationRequest.setLastName("Doe");
        registrationRequest.setUsername("johndoe");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.username", is("johndoe")));
    }

    @Test
    void testGetUserById() throws Exception {
        // Create a test user
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");
        user.setStatus(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        // Test getting user by ID
        mockMvc.perform(get("/api/v1/users/{id}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")))
                .andExpect(jsonPath("$.username", is("johndoe")));
    }

    @Test
    void testUpdateUserStatus() throws Exception {
        // Create a test user
        User user = new User();
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUsername("johndoe");
        user.setStatus(UserStatus.ACTIVE);
        User savedUser = userRepository.save(user);

        // Test updating user status
        mockMvc.perform(patch("/api/v1/users/{id}/status", savedUser.getId())
                .param("status", "SUSPENDED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("SUSPENDED")));
    }

    @Test
    void testGetUsersByStatus() throws Exception {
        // Create test users with different statuses
        User activeUser = new User();
        activeUser.setEmail("active@example.com");
        activeUser.setFirstName("Active");
        activeUser.setLastName("User");
        activeUser.setUsername("activeuser");
        activeUser.setStatus(UserStatus.ACTIVE);
        userRepository.save(activeUser);

        User suspendedUser = new User();
        suspendedUser.setEmail("suspended@example.com");
        suspendedUser.setFirstName("Suspended");
        suspendedUser.setLastName("User");
        suspendedUser.setUsername("suspendeduser");
        suspendedUser.setStatus(UserStatus.SUSPENDED);
        userRepository.save(suspendedUser);

        // Test getting users by status
        mockMvc.perform(get("/api/v1/users")
                .param("status", "ACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].status", is("ACTIVE")));
    }

    @Test
    void testUserNotFound() throws Exception {
        // Test getting non-existent user
        mockMvc.perform(get("/api/v1/users/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDuplicateEmailRegistration() throws Exception {
        // Create a user first
        User existingUser = new User();
        existingUser.setEmail("test@example.com");
        existingUser.setFirstName("Existing");
        existingUser.setLastName("User");
        existingUser.setUsername("existinguser");
        existingUser.setStatus(UserStatus.ACTIVE);
        userRepository.save(existingUser);

        // Try to register with same email
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setPassword("password123");
        registrationRequest.setFirstName("New");
        registrationRequest.setLastName("User");
        registrationRequest.setUsername("newuser");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void testInvalidRegistrationData() throws Exception {
        // Test registration with invalid data
        RegistrationRequest registrationRequest = new RegistrationRequest();
        registrationRequest.setEmail("invalid-email");
        registrationRequest.setPassword("123"); // Too short
        registrationRequest.setFirstName("");
        registrationRequest.setLastName("");
        registrationRequest.setUsername("");

        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest());
    }
}
