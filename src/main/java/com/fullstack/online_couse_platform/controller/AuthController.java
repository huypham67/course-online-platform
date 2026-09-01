package com.fullstack.online_couse_platform.controller;

import com.fullstack.online_couse_platform.dto.request.LoginRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.ApiResult;
import com.fullstack.online_couse_platform.dto.response.LoginResponse;
import com.fullstack.online_couse_platform.dto.response.UserResponse;
import com.fullstack.online_couse_platform.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "Authentication & Registration APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register/learner")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new learner account", description = "Creates a new user with LEARNER role and creates the associated learner profile.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Learner registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "409", description = "Email is already in use",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<UserResponse> registerLearner(@Valid @RequestBody RegisterLearnerRequest request) {
        return ApiResult.of(HttpStatus.CREATED, "Learner registered successfully", authService.registerLearner(request));
    }

    @PostMapping("/register/instructor")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new instructor account", description = "Creates a new user with INSTRUCTOR role and creates the associated instructor profile in PENDING status.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Instructor registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "409", description = "Email is already in use",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<UserResponse> registerInstructor(@Valid @RequestBody RegisterInstructorRequest request) {
        return ApiResult.of(HttpStatus.CREATED, "Instructor registered successfully", authService.registerInstructor(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login and receive access token", description = "Authenticates user with email and password, returning JWT access and refresh tokens.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed or malformed body",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResult.of(HttpStatus.OK, "Login successful", authService.login(request));
    }
}
