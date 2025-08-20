package com.raved.user.service;

import com.raved.user.dto.request.RegisterStepRequest;
import com.raved.user.dto.response.RegisterStepResponse;
import com.raved.user.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class RegistrationServiceImplTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private RegistrationServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        service = new RegistrationServiceImpl();
        // inject mock
        try {
            var f = RegistrationServiceImpl.class.getDeclaredField("redisTemplate");
            f.setAccessible(true);
            f.set(service, redisTemplate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // stub Redis ops to avoid NPE
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(anyString(), any(), any(Duration.class));
    }

    @Test
    void startStep_returnsSessionTokenAndNextStep() {
        RegisterStepRequest req = new RegisterStepRequest();
        req.setStep(1);
        req.setData(new HashMap<>());
        RegisterStepResponse resp = service.handleStep(req);
        assertThat(resp.isSuccess()).isTrue();
        assertThat(resp.getNextStep()).isEqualTo(2);
        assertThat(resp.getSessionToken()).isNotBlank();
    }
}

