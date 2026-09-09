package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.dto.request.ForgotPasswordRequest;
import com.fullstack.online_course_platform.dto.request.ResetPasswordRequest;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.model.PasswordResetToken;
import com.fullstack.online_course_platform.model.User;
import com.fullstack.online_course_platform.repository.PasswordResetTokenRepository;
import com.fullstack.online_course_platform.repository.UserRepository;
import com.fullstack.online_course_platform.service.MailService;
import com.fullstack.online_course_platform.service.PasswordResetService;
import com.fullstack.online_course_platform.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PASSWORD-RESET-SERVICE")
public class PasswordResetServiceImpl implements PasswordResetService {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final MailService mailService;

    @Value("${app.password-reset.expiry-seconds}")
    private long expirySeconds;

    @Override
    @Transactional
    public void requestPasswordReset(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(this::createAndSendResetToken);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        Instant now = Instant.now();
        PasswordResetToken resetToken = passwordResetTokenRepository
                .findActiveTokenForUpdate(hashToken(request.token()), now)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PASSWORD_RESET_TOKEN));
        User user = resetToken.getUser();

        if (passwordEncoder.matches(request.newPassword(), user.getPasswordHash())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_CURRENT);
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        resetToken.setUsedAt(now);
        userRepository.save(user);
        passwordResetTokenRepository.save(resetToken);
        tokenService.revokeActiveTokens(user.getId());
        log.info("Password reset completed: userId={}", user.getId());
    }

    private void createAndSendResetToken(User user) {
        Instant now = Instant.now();
        passwordResetTokenRepository.invalidateActiveTokensByUserId(user.getId(), now);

        String rawToken = generateToken();
        PasswordResetToken resetToken = passwordResetTokenRepository.save(PasswordResetToken.builder()
                .user(user)
                .tokenHash(hashToken(rawToken))
                .expiresAt(now.plusSeconds(expirySeconds))
                .build());

        try {
            mailService.sendPasswordResetEmail(user.getEmail(), rawToken);
            log.info("Password reset email sent: userId={}", user.getId());
        } catch (MailException exception) {
            resetToken.setUsedAt(now);
            passwordResetTokenRepository.save(resetToken);
            log.error("Password reset email delivery failed: userId={}", user.getId(), exception);
        }
    }

    private String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String hashToken(String token) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException exception) {
            throw new IllegalStateException("SHA-256 is unavailable", exception);
        }
    }
}