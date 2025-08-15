package com.raved.user.service.impl;

import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.mapper.UserMapper;
import com.raved.user.model.User;
import com.raved.user.model.UserStatus;
import com.raved.user.repository.UserRepository;
import com.raved.user.security.JwtTokenProvider;
import com.raved.user.util.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest request;
    private User user;

    private static class FakeJwtTokenProvider extends JwtTokenProvider {

        @Override
        public String generateAccessToken(User user) {
            return "access";
        }

        @Override
        public String generateRefreshToken(User user) {
            return "refresh";
        }

        @Override
        public long getAccessTokenValidityInSeconds() {
            return 3600L;
        }
    }

    private static class CapturingUserEventPublisher extends com.raved.user.event.UserEventPublisher {

        Long userId;
        String username;
        String email;
        int createdCalls;

        @Override
        public void publishUserCreated(Long userId, String username, String email) {
            this.userId = userId;
            this.username = username;
            this.email = email;
            this.createdCalls++;
        }
    }

    private static class FakePasswordEncoderUtil extends PasswordEncoderUtil {

        @Override
        public String encode(String rawPassword) {
            return "hashed";
        }

        @Override
        public boolean matches(String rawPassword, String encodedPassword) {
            return true;
        }
    }

    @BeforeEach
    void setup() throws Exception {
        request = new RegisterRequest();
        // Using reflection to set fields if no setters exist; otherwise assume setters
        try {
            var usernameField = RegisterRequest.class.getDeclaredField("username");
            usernameField.setAccessible(true);
            usernameField.set(request, "testuser");
            var emailField = RegisterRequest.class.getDeclaredField("email");
            emailField.setAccessible(true);
            emailField.set(request, "test@raved.app");
            var passwordField = RegisterRequest.class.getDeclaredField("password");
            passwordField.setAccessible(true);
            passwordField.set(request, "Password#123");
        } catch (Exception ignored) {
        }

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@raved.app");
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        // Inject fakes to avoid Mockito mocking issues with JDK 24
        var fakeJwt = new FakeJwtTokenProvider();
        var publisher = new CapturingUserEventPublisher();
        setField(authService, "jwtTokenProvider", fakeJwt);
        setField(authService, "userEventPublisher", publisher);
        setField(authService, "passwordEncoderUtil", new FakePasswordEncoderUtil());
    }

    @Test
    void register_publishesUserCreated_andReturnsTokens() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@raved.app")).thenReturn(false);
        when(userMapper.toUser(any(RegisterRequest.class))).thenReturn(new User());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toUserResponse(user)).thenReturn(null);

        AuthResponse resp = authService.register(request);

        assertThat(resp.getAccessToken()).isEqualTo("access");
        assertThat(resp.getRefreshToken()).isEqualTo("refresh");
        // verify event captured
        CapturingUserEventPublisher cap = (CapturingUserEventPublisher) getField(authService, "userEventPublisher");
        assertThat(cap.createdCalls).isEqualTo(1);
        assertThat(cap.userId).isEqualTo(1L);
        assertThat(cap.username).isEqualTo("testuser");
        assertThat(cap.email).isEqualTo("test@raved.app");
    }

    // Simple reflection helpers
    private static void setField(Object target, String name, Object value) throws Exception {
        var f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }

    private static Object getField(Object target, String name) {
        try {
            var f = target.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) {
            return null;
        }
    }
}
