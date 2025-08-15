package com.raved.user.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "notification-service", path = "/api/v1/notifications")
public interface NotificationClient {

    @PostMapping("/email/verification")
    void sendEmailVerification(@RequestBody Map<String, Object> payload);

    @PostMapping("/sms/verification")
    void sendSmsVerification(@RequestBody Map<String, Object> payload);
}

