package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.enums.UserStatus;
import com.fullstack.online_course_platform.dto.request.UpdateUserStatusRequest;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.dto.response.UserStatusResponse;
import com.fullstack.online_course_platform.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Users", description = "User administration APIs")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ApiResult<PageResponse<UserResponse>> getUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) RoleType role,
            @RequestParam(required = false) UserStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ApiResult.of(HttpStatus.OK, "Users retrieved successfully",
                userService.findUsers(keyword, role, status, pageable));
    }

    @GetMapping("/{userId}")
    public ApiResult<UserResponse> getUser(@PathVariable UUID userId) {
        return ApiResult.of(HttpStatus.OK, "User retrieved successfully", userService.getUser(userId));
    }

    @PatchMapping("/{userId}/status")
    public ApiResult<UserStatusResponse> updateStatus(
            @PathVariable UUID userId,
            @Valid @RequestBody UpdateUserStatusRequest request) {
        return ApiResult.of(HttpStatus.OK, "User status updated successfully",
                userService.updateStatus(userId, request.status()));
    }
}