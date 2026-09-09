package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import lombok.Builder;

@Builder
public record InstructorStatusResponse(
        String id,

        @JsonProperty("instructor_status")
        InstructorStatus instructorStatus
) {
}