package com.fullstack.online_couse_platform.controller;

import com.fullstack.online_couse_platform.dto.request.LoginRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.ApiResult;
import com.fullstack.online_couse_platform.dto.response.LoginResponse;
import com.fullstack.online_couse_platform.dto.response.UserResponse;
import com.fullstack.online_couse_platform.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/learner")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<UserResponse> registerLearner(@Valid @RequestBody RegisterLearnerRequest request) {
        return ApiResult.of(HttpStatus.CREATED, "Learner registered successfully", authService.registerLearner(request));
    }

    @PostMapping("/register/instructor")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResult<UserResponse> registerInstructor(@Valid @RequestBody RegisterInstructorRequest request) {
        return ApiResult.of(HttpStatus.CREATED, "Instructor registered successfully", authService.registerInstructor(request));
    }

    @PostMapping("/login")
    public ApiResult<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResult.of(HttpStatus.OK, "Login successful", authService.login(request));
    }
}
