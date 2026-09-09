package com.fullstack.online_course_platform.dto.response;

import com.fullstack.online_course_platform.common.enums.CourseStatus;
import lombok.Builder;

@Builder
public record CourseCreatedResponse(String id, String slug, CourseStatus status) {
}