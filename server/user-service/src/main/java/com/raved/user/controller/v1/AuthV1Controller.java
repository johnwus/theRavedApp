package com.raved.user.controller.v1;

import com.raved.user.dto.request.LoginRequest;
import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.service.AuthService;
import com.raved.user.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import com.raved.user.service.VerificationService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthV1Controller {

    @Autowired
    private AuthService authService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    // Placeholders for verification flows to be implemented in next phase
    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody Map<String, String> payload) {
        String email = payload.getOrDefault("email", "");
        String code = payload.getOrDefault("code", "");
        boolean ok = verificationService.verifyEmailCode(email, code, "VERIFY_EMAIL");
        return ResponseEntity.ok(Map.of("success", ok));
    }

    @PostMapping("/verify-phone")
    public ResponseEntity<Map<String, Object>> verifyPhone(@RequestBody Map<String, String> payload) {
        String phone = payload.getOrDefault("phone", "");
        String code = payload.getOrDefault("code", "");
        boolean ok = verificationService.verifyPhoneCode(phone, code, "VERIFY_PHONE");
        return ResponseEntity.ok(Map.of("success", ok));
    }

    @PostMapping("/send-email-code")
    public ResponseEntity<Map<String, Object>> sendEmailCode(@RequestBody Map<String, String> payload) {
        String email = payload.getOrDefault("email", "");
        String purpose = payload.getOrDefault("purpose", "VERIFY_EMAIL");
        verificationService.sendEmailCode(email, purpose);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/send-phone-code")
    public ResponseEntity<Map<String, Object>> sendPhoneCode(@RequestBody Map<String, String> payload) {
        String phone = payload.getOrDefault("phone", "");
        String purpose = payload.getOrDefault("purpose", "VERIFY_PHONE");
        verificationService.sendPhoneCode(phone, purpose);
        return ResponseEntity.ok(Map.of("success", true));
    }
}

