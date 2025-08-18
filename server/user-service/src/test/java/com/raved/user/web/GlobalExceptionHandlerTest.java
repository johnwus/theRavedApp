package com.raved.user.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    @Test
    void handleGeneric_returnsSpecShape() {
        GlobalExceptionHandler geh = new GlobalExceptionHandler();
        ResponseEntity<Map<String, Object>> resp = geh.handleGeneric(new RuntimeException("x"), null);
        Map<String, Object> body = resp.getBody();
        assertThat(body).isNotNull();
        assertThat(body.get("success")).isEqualTo(false);
        Map<String, Object> error = (Map<String, Object>) body.get("error");
        assertThat(error.get("code")).isEqualTo("INTERNAL_SERVER_ERROR");
    }
}

