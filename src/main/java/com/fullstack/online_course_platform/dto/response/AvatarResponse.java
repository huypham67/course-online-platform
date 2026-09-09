package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record AvatarResponse(
        @JsonProperty("avatar_url")
        String avatarUrl
) {
}