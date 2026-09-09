package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.enums.UserStatus;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.dto.response.UserStatusResponse;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserService {

    UserResponse createUser(String email, String password, String fullName, RoleType roleType);

    PageResponse<UserResponse> findUsers(String keyword, RoleType role, UserStatus status, Pageable pageable);

    UserResponse getUser(UUID userId);

    UserStatusResponse updateStatus(UUID userId, UserStatus status);
}
