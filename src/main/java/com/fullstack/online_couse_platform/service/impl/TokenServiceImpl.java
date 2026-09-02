package com.fullstack.online_couse_platform.service.impl;

import com.fullstack.online_couse_platform.common.enums.TokenType;
import com.fullstack.online_couse_platform.config.JwtProperties;
import com.fullstack.online_couse_platform.dto.response.TokenResponse;
import com.fullstack.online_couse_platform.exception.AppException;
import com.fullstack.online_couse_platform.exception.ErrorCode;
import com.fullstack.online_couse_platform.model.User;
import com.fullstack.online_couse_platform.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private static final String TOKEN_TYPE_CLAIM = "token_type";

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final JwtProperties jwtProperties;

    @Override
    public TokenResponse createTokenResponse(User user) {
        return TokenResponse.builder()
                .accessToken(generateAccessToken(user))
                .refreshToken(generateRefreshToken(user.getId()))
                .build();
    }

    @Override
    public UUID getRefreshTokenUserId(String refreshToken) {
        try {
            var jwt = jwtDecoder.decode(refreshToken);
            String tokenType = jwt.getClaimAsString(TOKEN_TYPE_CLAIM);
            if (!TokenType.REFRESH.name().equals(tokenType)) {
                throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
            }
            return UUID.fromString(Objects.requireNonNull(jwt.getSubject()));
        } catch (JwtException | IllegalArgumentException exception) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
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

    private String generateRefreshToken(UUID userId) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId.toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(jwtProperties.getRefreshExpirySeconds()))
                .claim(TOKEN_TYPE_CLAIM, TokenType.REFRESH.name())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
