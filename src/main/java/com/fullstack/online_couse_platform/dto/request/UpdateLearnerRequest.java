package com.fullstack.online_couse_platform.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateLearnerRequest(
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    String fullName,

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    String avatarUrl,

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    String bio
) {}
