package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.dto.request.LoginRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.LoginResponse;
import com.fullstack.online_couse_platform.dto.response.UserResponse;

public interface AuthService {
    UserResponse registerLearner(RegisterLearnerRequest request);
    UserResponse registerInstructor(RegisterInstructorRequest request);
    LoginResponse login(LoginRequest request);
}
