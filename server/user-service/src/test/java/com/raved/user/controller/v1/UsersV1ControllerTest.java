package com.raved.user.controller.v1;

import com.raved.user.dto.request.UpdateProfileRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.service.ProfileService;
import com.raved.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsersV1ControllerTest {

    @Mock
    private ProfileService profileService;
    @Mock
    private UserService userService;
    @InjectMocks
    private UsersV1Controller controller;

    private Principal principal;
    private com.raved.user.model.User user;

    @BeforeEach
    void setUp() {
        principal = () -> "testuser";
        user = new com.raved.user.model.User();
        user.setId(7L);
        user.setUsername("testuser");
    }

    @Test
    void getCurrentProfile_returnsProfile() {
        when(userService.findUserByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));
        UserResponse respBody = new UserResponse();
        when(profileService.getProfile(7L)).thenReturn(respBody);
        ResponseEntity<UserResponse> resp = controller.getCurrentProfile(principal);
        assertThat(resp.getBody()).isSameAs(respBody);
    }

    @Test
    void updateCurrentProfile_updatesAndReturnsProfile() {
        when(userService.findUserByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));
        UserResponse respBody = new UserResponse();
        when(profileService.updateProfile(any(Long.class), any(UpdateProfileRequest.class))).thenReturn(respBody);
        ResponseEntity<UserResponse> resp = controller.updateCurrentProfile(new UpdateProfileRequest(), principal);
        assertThat(resp.getBody()).isSameAs(respBody);
    }

    @Test
    void uploadAvatar_uploadsAndReturnsProfile() {
        when(userService.findUserByUsernameOrEmail("testuser")).thenReturn(Optional.of(user));
        UserResponse respBody = new UserResponse();
        when(profileService.uploadProfilePicture(any(Long.class), any())).thenReturn(respBody);
        MockMultipartFile mf = new MockMultipartFile("avatar", "a.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[]{1, 2});
        ResponseEntity<UserResponse> resp = controller.uploadAvatar(mf, principal);
        assertThat(resp.getBody()).isSameAs(respBody);
    }

    @Test
    void getCurrentProfile_throwsWhenUserNotFound() {
        Principal badPrincipal = () -> "ghost";
        when(userService.findUserByUsernameOrEmail("ghost")).thenReturn(Optional.empty());
        try {
            controller.getCurrentProfile(badPrincipal);
            throw new AssertionError("Expected RuntimeException not thrown");
        } catch (RuntimeException expected) {
            assertThat(expected).isNotNull();
        }
    }
}
