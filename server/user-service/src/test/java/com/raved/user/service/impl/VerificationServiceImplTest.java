package com.raved.user.service.impl;

import com.raved.user.model.VerificationCode;
import com.raved.user.repository.VerificationCodeRepository;
import com.raved.user.util.PasswordEncoderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VerificationServiceImplTest {

    @Mock
    private VerificationCodeRepository verificationCodeRepository;
    @InjectMocks
    private VerificationServiceImpl verificationService;

    static class FakePasswordEncoderUtil extends PasswordEncoderUtil {

        @Override
        public String encode(String rawPassword) {
            return "hash:" + rawPassword;
        }

        @Override
        public boolean matches(String rawPassword, String encodedPassword) {
            return encodedPassword.equals("hash:" + rawPassword);
        }
    }

    @BeforeEach
    void injectPasswordEncoder() throws Exception {
        setField(verificationService, "passwordEncoderUtil", new FakePasswordEncoderUtil());
    }

    @Test
    void verifyCode_succeedsAndConsumes() {
        VerificationCode vc = new VerificationCode();
        vc.setCodeHash("hash:123456");
        vc.setAttempts(0);
        vc.setMaxAttempts(5);
        vc.setExpiresAt(Instant.now().plusSeconds(300));
        when(verificationCodeRepository.findTopByDestinationAndPurposeAndConsumedAtIsNullAndExpiresAtAfterOrderByCreatedAtDesc(eq("user@example.com"), eq("VERIFY_EMAIL"), any(Instant.class)))
                .thenReturn(Optional.of(vc));

        boolean ok = verificationService.verifyEmailCode("user@example.com", "123456", "VERIFY_EMAIL");

        assertThat(ok).isTrue();
        verify(verificationCodeRepository, times(1)).save(any(VerificationCode.class));
    }

    @Test
    void verifyCode_wrongCode_incrementsAttemptsAndNotConsumed() {
        VerificationCode vc = new VerificationCode();
        vc.setCodeHash("hash:654321");
        vc.setAttempts(1);
        vc.setMaxAttempts(5);
        vc.setExpiresAt(Instant.now().plusSeconds(300));
        when(verificationCodeRepository.findTopByDestinationAndPurposeAndConsumedAtIsNullAndExpiresAtAfterOrderByCreatedAtDesc(eq("user@example.com"), eq("VERIFY_EMAIL"), any(Instant.class)))
                .thenReturn(Optional.of(vc));

        boolean ok = verificationService.verifyEmailCode("user@example.com", "123456", "VERIFY_EMAIL");

        assertThat(ok).isFalse();
        verify(verificationCodeRepository, times(1)).save(argThat(saved -> saved.getAttempts() == 2 && saved.getConsumedAt() == null));
    }

    @Test
    void verifyCode_maxAttempts_rejectsWithoutSaving() {
        VerificationCode vc = new VerificationCode();
        vc.setCodeHash("hash:any");
        vc.setAttempts(5);
        vc.setMaxAttempts(5);
        vc.setExpiresAt(Instant.now().plusSeconds(300));
        when(verificationCodeRepository.findTopByDestinationAndPurposeAndConsumedAtIsNullAndExpiresAtAfterOrderByCreatedAtDesc(eq("user@example.com"), eq("VERIFY_EMAIL"), any(Instant.class)))
                .thenReturn(Optional.of(vc));

        boolean ok = verificationService.verifyEmailCode("user@example.com", "whatever", "VERIFY_EMAIL");

        assertThat(ok).isFalse();
        verify(verificationCodeRepository, never()).save(any());
    }

    @Test
    void verifyCode_expired_returnsFalse() {
        // Repository would return empty for expired; simulate that
        when(verificationCodeRepository.findTopByDestinationAndPurposeAndConsumedAtIsNullAndExpiresAtAfterOrderByCreatedAtDesc(eq("user@example.com"), eq("VERIFY_EMAIL"), any(Instant.class)))
                .thenReturn(Optional.empty());

        boolean ok = verificationService.verifyEmailCode("user@example.com", "123456", "VERIFY_EMAIL");
        assertThat(ok).isFalse();
        verify(verificationCodeRepository, never()).save(any());
    }

    private static void setField(Object target, String name, Object value) throws Exception {
        var f = target.getClass().getDeclaredField(name);
        f.setAccessible(true);
        f.set(target, value);
    }
}
