package com.fullstack.online_course_platform.dto.request;

import com.fullstack.online_course_platform.common.enums.UserStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateUserStatusRequest(
        @NotNull(message = "User status is required")
        UserStatus status
) {
}