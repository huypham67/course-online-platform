package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.dto.request.LoginRequest;
import com.fullstack.online_couse_platform.dto.request.RefreshTokenRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.TokenResponse;
import com.fullstack.online_couse_platform.dto.response.UserResponse;

public interface AuthService {
    UserResponse registerLearner(RegisterLearnerRequest request);
    UserResponse registerInstructor(RegisterInstructorRequest request);
    TokenResponse login(LoginRequest request);
    TokenResponse refreshToken(RefreshTokenRequest request);
}
