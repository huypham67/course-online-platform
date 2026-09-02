package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.TokenType;
import com.fullstack.online_course_platform.config.JwtProperties;
import com.fullstack.online_course_platform.dto.response.TokenResponse;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.model.RefreshToken;
import com.fullstack.online_course_platform.model.User;
import com.fullstack.online_course_platform.repository.RefreshTokenRepository;
import com.fullstack.online_course_platform.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public TokenResponse createTokenResponse(User user) {
        return TokenResponse.builder()
                .accessToken(generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    @Override
    @Transactional
    public User consumeRefreshToken(String refreshTokenValue) {
        try {
            //1. Decode refresh token và xác minh rằng nó là một refresh token hợp lệ.
            var jwt = jwtDecoder.decode(refreshTokenValue);
            String tokenType = jwt.getClaimAsString(TOKEN_TYPE_CLAIM);
            if (!TokenType.REFRESH.name().equals(tokenType)) {
                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            UUID userId = UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
            UUID tokenId = UUID.fromString(Objects.requireNonNull(jwt.getId()));

            //2. Tìm trong DB xem refresh token có tồn tại, chưa bị thu hồi, chưa hết hạn và khớp với hash của token được cung cấp.
            RefreshToken refreshToken = refreshTokenRepository.findByJtiAndRevokedAtIsNull(tokenId)
                    .filter(token -> token.getUser().getId().equals(userId))
                    .filter(token -> token.getExpiresAt().isAfter(Instant.now()))
                    .filter(token -> token.getTokenHash().equals(hashToken(refreshTokenValue)))
                    .orElseThrow(() -> {
                        // Security option: Nếu phát hiện token đã bị revoke mà vẫn có người dùng cố gắng sử dụng nó,
                        // nghi ngờ rằng token có thể đã bị đánh cắp, do đó thu hồi tất cả các refresh token hiện tại của người dùng để bảo vệ tài khoản.
                        refreshTokenRepository.revokeActiveTokensByUserId(userId, Instant.now());
                        return new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
                    });

            // 3. Đánh dấu token này đã được sử dụng (revoked) để ngăn chặn việc sử dụng lại.
            refreshToken.setRevokedAt(Instant.now());
            refreshTokenRepository.save(refreshToken);
            return refreshToken.getUser();
        } catch (JwtException | IllegalArgumentException exception) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    @Override
    @Transactional
    public void revokeActiveTokens(UUID userId) {
        refreshTokenRepository.revokeActiveTokensByUserId(userId, Instant.now());
    }

    private String generateAccessToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtProperties.getExpirySeconds()))
                .claim(TOKEN_TYPE_CLAIM, TokenType.ACCESS.name())
                .claim("email", user.getEmail());

        if (user.getRole() != null) {
            claimsBuilder.claim("role", user.getRole().getName());
        }

        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
    }

    private String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtProperties.getRefreshExpirySeconds());
        UUID tokenId = UUID.randomUUID();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .id(tokenId.toString())
                .issuedAt(now)
                .expiresAt(expiresAt)
                .claim(TOKEN_TYPE_CLAIM, TokenType.REFRESH.name())
                .build();

        String refreshTokenValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        refreshTokenRepository.save(RefreshToken.builder()
            .user(user)
                .jti(tokenId)
                .tokenHash(hashToken(refreshTokenValue))
                .expiresAt(expiresAt)
                .build());
        return refreshTokenValue;
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
