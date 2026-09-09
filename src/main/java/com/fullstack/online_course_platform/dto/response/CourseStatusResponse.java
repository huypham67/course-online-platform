package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.online_course_platform.common.enums.CourseStatus;
import lombok.Builder;

import java.time.Instant;

@Builder
public record CourseStatusResponse(
        String id,
        CourseStatus status,
        @JsonProperty("rejection_reason") String rejectionReason,
        @JsonProperty("published_at") Instant publishedAt
) {
}