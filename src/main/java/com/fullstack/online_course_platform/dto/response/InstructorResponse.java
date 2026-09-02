package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.enums.UserStatus;
import lombok.Builder;

@Builder
public record InstructorResponse(
    String id,
    String email,
    RoleType role,
    UserStatus status,

    @JsonProperty("full_name")
    String fullName,

    @JsonProperty("avatar_url")
    String avatarUrl,

    String bio,

    String expertise,

    @JsonProperty("experience_years")
    Integer experienceYears,

    @JsonProperty("instructor_status")
    InstructorStatus instructorStatus,

    @JsonProperty("created_at")
    String createdAt,

    @JsonProperty("updated_at")
    String updatedAt
) {}
