package com.fullstack.online_course_platform.dto.response;

import com.fullstack.online_course_platform.common.enums.UserStatus;
import lombok.Builder;

@Builder
public record UserStatusResponse(String id, UserStatus status) {
}