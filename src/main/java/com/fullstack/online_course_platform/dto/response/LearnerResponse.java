package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.enums.UserStatus;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LearnerResponse(
    String id,
    String email,
    RoleType role,
    UserStatus status,

    @JsonProperty("full_name")
    String fullName,

    @JsonProperty("avatar_url")
    String avatarUrl,

    String bio,

    String phone,

    @JsonProperty("date_of_birth")
    LocalDate dateOfBirth,

    String occupation,

    @JsonProperty("learning_goal")
    String learningGoal,

    @JsonProperty("created_at")
    String createdAt,

    @JsonProperty("updated_at")
    String updatedAt
) {}
