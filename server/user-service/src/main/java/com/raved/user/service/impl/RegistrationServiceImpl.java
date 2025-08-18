package com.raved.user.service.impl;

import com.raved.user.dto.request.RegisterStepRequest;
import com.raved.user.dto.response.RegisterStepResponse;
import com.raved.user.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private static final String PREFIX = "reg:sess:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public RegisterStepResponse handleStep(RegisterStepRequest request) {
        Integer step = request.getStep();
        String token = request.getSessionToken();
        Map<String, Object> data = request.getData() != null ? request.getData() : new HashMap<>();

        if (step == 1) {
            // Start session
            String sessionId = UUID.randomUUID().toString();
            Map<String, Object> session = new HashMap<>();
            session.put("step", 1);
            session.putAll(data);
            redisTemplate.opsForValue().set(PREFIX + sessionId, session, Duration.ofMinutes(30));

            RegisterStepResponse resp = new RegisterStepResponse();
            resp.setSuccess(true);
            resp.setNextStep(2);
            resp.setSessionToken(sessionId);
            return resp;
        }

        if (token == null || token.isBlank()) {
            RegisterStepResponse resp = new RegisterStepResponse();
            resp.setSuccess(false);
            resp.setNextStep(step);
            return resp;
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> session = (Map<String, Object>) redisTemplate.opsForValue().get(PREFIX + token);
        if (session == null) {
            RegisterStepResponse resp = new RegisterStepResponse();
            resp.setSuccess(false);
            resp.setNextStep(1);
            return resp;
        }

        session.putAll(data);
        session.put("step", step);
        redisTemplate.opsForValue().set(PREFIX + token, session, Duration.ofMinutes(30));

        RegisterStepResponse resp = new RegisterStepResponse();
        resp.setSuccess(true);
        resp.setNextStep(step + 1);
        resp.setSessionToken(token);
        return resp;
    }
}

