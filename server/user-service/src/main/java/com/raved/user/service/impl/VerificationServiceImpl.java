package com.raved.user.service.impl;

import com.raved.user.model.VerificationCode;
import com.raved.user.repository.VerificationCodeRepository;
import com.raved.user.service.VerificationService;
import com.raved.user.util.PasswordEncoderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.raved.user.client.NotificationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Map;

@Service
@Transactional
public class VerificationServiceImpl implements VerificationService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationServiceImpl.class);

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private PasswordEncoderUtil passwordEncoderUtil;

    @Autowired(required = false)
    private NotificationClient notificationClient;

    private static final SecureRandom RNG = new SecureRandom();

    @Override
    public void sendEmailCode(String email, String purpose) {
        String code = generateCode();
        saveCode(null, "EMAIL", email, purpose, code);
        if (notificationClient != null) {
            try {
                notificationClient.sendEmailVerification(Map.of(
                        "email", email,
                        "code", code,
                        "userName", "User"
                ));
            } catch (Exception e) {
                logger.warn("Failed to send email via notification-service: {}", e.getMessage());
            }
        }
        logger.info("Verification email code generated for {} purpose={}", email, purpose);
    }

    @Override
    public void sendPhoneCode(String phone, String purpose) {
        String code = generateCode();
        saveCode(null, "PHONE", phone, purpose, code);
        if (notificationClient != null) {
            try {
                notificationClient.sendSmsVerification(Map.of(
                        "phone", phone,
                        "code", code,
                        "userName", "User"
                ));
            } catch (Exception e) {
                logger.warn("Failed to send SMS via notification-service: {}", e.getMessage());
            }
        }
        logger.info("Verification SMS code generated for {} purpose={}", phone, purpose);
    }

    @Override
    public boolean verifyEmailCode(String email, String code, String purpose) {
        return verifyCode(email, code, purpose);
    }

    @Override
    public boolean verifyPhoneCode(String phone, String code, String purpose) {
        return verifyCode(phone, code, purpose);
    }

    private boolean verifyCode(String destination, String rawCode, String purpose) {
        return verificationCodeRepository
                .findTopByDestinationAndPurposeAndConsumedAtIsNullAndExpiresAtAfterOrderByCreatedAtDesc(
                        destination, purpose, Instant.now())
                .map(vc -> {
                    if (vc.getAttempts() >= vc.getMaxAttempts()) {
                        return false;
                    }
                    boolean ok = passwordEncoderUtil.matches(rawCode, vc.getCodeHash());
                    vc.setAttempts(vc.getAttempts() + 1);
                    if (ok) {
                        vc.setConsumedAt(Instant.now());
                    }
                    verificationCodeRepository.save(vc);
                    return ok;
                })
                .orElse(false);
    }

    private void saveCode(Long userId, String channel, String destination, String purpose, String rawCode) {
        VerificationCode vc = new VerificationCode();
        vc.setUserId(userId);
        vc.setChannel(channel);
        vc.setDestination(destination);
        vc.setPurpose(purpose);
        vc.setCodeHash(passwordEncoderUtil.encode(rawCode));
        vc.setMaxAttempts(5);
        vc.setAttempts(0);
        vc.setExpiresAt(Instant.now().plusSeconds(10 * 60)); // 10 minutes
        verificationCodeRepository.save(vc);
    }

    private String generateCode() {
        int code = 100000 + RNG.nextInt(900000);
        return String.valueOf(code);
    }
}
