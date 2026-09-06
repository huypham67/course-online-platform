package com.fullstack.online_course_platform.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateAvatarRequest(
    @NotBlank(message = "Avatar URL is required")
    @JsonProperty("avatar_url")
    String avatarUrl
) {}
