package com.fullstack.online_course_platform.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RefreshTokenRequest(
    @NotBlank(message = "Refresh token is required")
    @JsonProperty("refresh_token")
    String refreshToken
) {}
