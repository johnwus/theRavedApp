package com.raved.user.controller.v1;

import com.raved.user.dto.request.LoginRequest;
import com.raved.user.dto.request.RegisterStepRequest;
import com.raved.user.dto.response.AuthResponse;
import com.raved.user.dto.response.RegisterStepResponse;
import com.raved.user.service.AuthService;
import com.raved.user.service.RegistrationService;
import com.raved.user.service.VerificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthV1ControllerTest {

    @Mock
    private AuthService authService;
    @Mock
    private VerificationService verificationService;
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private AuthV1Controller controller;

    private AuthResponse response;

    @BeforeEach
    void setUp() {
        response = AuthResponse.builder().accessToken("a").refreshToken("b").tokenType("Bearer").expiresIn(3600L).build();
    }

    @Test
    void refreshToken_acceptsQueryParamOrBody() {
        when(authService.refreshToken("q")).thenReturn(response);
        ResponseEntity<AuthResponse> resp1 = controller.refreshToken("q", null);
        assertThat(resp1).isNotNull();
        assertThat(resp1.getBody()).isNotNull();
        assertThat(resp1.getBody().getAccessToken()).isEqualTo("a");

        Map<String, String> body = new HashMap<>();
        body.put("refreshToken", "b");
        when(authService.refreshToken("b")).thenReturn(response);
        ResponseEntity<AuthResponse> resp2 = controller.refreshToken(null, body);
        assertThat(resp2).isNotNull();
        assertThat(resp2.getBody()).isNotNull();
        assertThat(resp2.getBody().getAccessToken()).isEqualTo("a");
    }

    @Test
    void login_callsService() {
        when(authService.login(any(LoginRequest.class))).thenReturn(response);
        ResponseEntity<AuthResponse> resp = controller.login(new LoginRequest());
        assertThat(resp).isNotNull();
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getAccessToken()).isEqualTo("a");
    }

    @Test
    void register_callsRegistrationService() {
        RegisterStepResponse rsr = new RegisterStepResponse();
        rsr.setSuccess(true);
        rsr.setNextStep(2);
        when(registrationService.handleStep(any(RegisterStepRequest.class))).thenReturn(rsr);
        ResponseEntity<RegisterStepResponse> resp = controller.register(new RegisterStepRequest());
        assertThat(resp).isNotNull();
        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().isSuccess()).isTrue();
        assertThat(resp.getBody().getNextStep()).isEqualTo(2);
    }
}

