package com.fullstack.online_couse_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.online_couse_platform.common.enums.RoleType;
import com.fullstack.online_couse_platform.common.enums.UserStatus;
import lombok.Builder;

@Builder
public record LearnerProfileResponse(
    String id,
    String email,
    RoleType role,
    UserStatus status,

    @JsonProperty("full_name")
    String fullName,

    @JsonProperty("avatar_url")
    String avatarUrl,

    String bio,

    @JsonProperty("created_at")
    String createdAt,

    @JsonProperty("updated_at")
    String updatedAt
) {}
