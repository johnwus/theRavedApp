package com.raved.user.controller.v1;

import com.raved.user.dto.request.UpdateProfileRequest;
import com.raved.user.dto.response.UserResponse;
import com.raved.user.service.ProfileService;
import com.raved.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
public class UsersV1Controller {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private UserService userService;

    // Current user profile endpoints
    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getCurrentProfile(Principal principal) {
        Long userId = userService.findUserByUsernameOrEmail(principal.getName())
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return ResponseEntity.ok(profileService.getProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserResponse> updateCurrentProfile(@Valid @RequestBody UpdateProfileRequest request,
            Principal principal) {
        Long userId = userService.findUserByUsernameOrEmail(principal.getName())
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return ResponseEntity.ok(profileService.updateProfile(userId, request));
    }

    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponse> uploadAvatar(@RequestPart("avatar") MultipartFile avatar,
            Principal principal) {
        Long userId = userService.findUserByUsernameOrEmail(principal.getName())
                .map(u -> u.getId())
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
        return ResponseEntity.ok(profileService.uploadProfilePicture(userId, avatar));
    }

    // Validation endpoints with spec-compliant paths
    @GetMapping("/validate/username/{username}")
    public ResponseEntity<Map<String, Object>> validateUsername(@PathVariable String username) {
        boolean available = !userService.existsByUsername(username);
        return ResponseEntity.ok(Map.of(
                "available", available
        ));
    }

    @GetMapping("/validate/email/{email}")
    public ResponseEntity<Map<String, Object>> validateEmail(@PathVariable String email) {
        boolean available = !userService.existsByEmail(email);
        return ResponseEntity.ok(Map.of(
                "available", available
        ));
    }
}
