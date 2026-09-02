package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.request.LoginRequest;
import com.fullstack.online_course_platform.dto.request.RefreshTokenRequest;
import com.fullstack.online_course_platform.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
    TokenResponse refreshToken(RefreshTokenRequest request);
    void logout();
}
