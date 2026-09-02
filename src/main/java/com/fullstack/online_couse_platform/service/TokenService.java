package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.dto.response.TokenResponse;
import com.fullstack.online_couse_platform.model.User;

import java.util.UUID;

public interface TokenService {

    TokenResponse createTokenResponse(User user);

    UUID getRefreshTokenUserId(String refreshToken);
}
