package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.response.TokenResponse;
import com.fullstack.online_course_platform.model.User;

import java.util.UUID;

public interface TokenService {

    TokenResponse createTokenResponse(User user);

    User consumeRefreshToken(String refreshToken);

    void revokeActiveTokens(UUID userId);
}
