package com.raved.user.controller.v1;

import com.raved.user.dto.request.LoginRequest;
import com.raved.user.dto.request.RegisterRequest;
import com.raved.user.dto.request.RegisterStepRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.dto.response.RegisterStepResponse;
import com.raved.user.service.AuthService;
import com.raved.user.service.RegistrationService;
import com.raved.user.service.VerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*")
public class AuthV1Controller {

    @Autowired
    private AuthService authService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    private RegistrationService registrationService;

    @Operation(summary = "Login", description = "Authenticate a user and return access/refresh tokens")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authenticated",
            content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Register (multi-step)", description = "Handle a registration step; returns nextStep and sessionToken")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Step handled",
            content = @Content(schema = @Schema(implementation = RegisterStepResponse.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RegisterStepResponse> register(@Valid @RequestBody RegisterStepRequest request) {
        return ResponseEntity.ok(registrationService.handleStep(request));
    }

    @Operation(summary = "Refresh token", description = "Accepts refreshToken via query param or JSON body")
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam(required = false) String refreshToken,
                                                     @RequestBody(required = false) Map<String, String> body) {
        String token = refreshToken;
        if ((token == null || token.isBlank()) && body != null) {
            token = body.get("refreshToken");
        }
        return ResponseEntity.ok(authService.refreshToken(token));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        authService.logout(token);
        return ResponseEntity.ok().build();
    }

    // Verification flows
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
