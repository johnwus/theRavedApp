package com.raved.user.service.impl;

import com.raved.user.dto.request.UpdateProfileRequest;
import com.raved.user.mapper.UserMapper;
import com.raved.user.model.User;
import com.raved.user.repository.UserRepository;
import com.raved.user.event.UserEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ProfileServiceImpl profileService;

    static class CapturingUserEventPublisher extends UserEventPublisher {

        long calls = 0;
        Long lastUserId;
        java.util.Map<String, Object> lastPayload;

        @Override
        public void publishUserUpdated(Long userId, java.util.Map<String, Object> userProfile) {
            calls++;
            lastUserId = userId;
            lastPayload = userProfile;
        }
    }

    @BeforeEach
    void injectPublisher() throws Exception {
        setField(profileService, "userEventPublisher", new CapturingUserEventPublisher());
    }

    @Test
    void updateProfile_publishesUserUpdated() {
        Long userId = 42L;
        User user = new User();
        user.setId(userId);
        user.setUsername("u42");
        user.setEmail("u42@raved.app");
        user.setUpdatedAt(Instant.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        profileService.updateProfile(userId, new UpdateProfileRequest());

        CapturingUserEventPublisher cap = (CapturingUserEventPublisher) getField(profileService, "userEventPublisher");
        assertThat(cap.calls).isEqualTo(1);
        assertThat(cap.lastUserId).isEqualTo(userId);
        assertThat(cap.lastPayload).isNotNull();
    }

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
