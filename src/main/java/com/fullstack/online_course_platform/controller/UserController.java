package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.dto.request.UpdateCurrentUserRequest;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Current user account APIs")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ApiResult<UserResponse> getCurrentUser() {
        return ApiResult.of(HttpStatus.OK, "Current user retrieved successfully", userService.getCurrentUser());
    }

    @PatchMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUser(@Valid @RequestBody UpdateCurrentUserRequest request) {
        userService.updateCurrentUser(request);
    }
}