package com.fullstack.online_course_platform.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateInstructorRequest(
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    String fullName,

    @Size(max = 500, message = "Avatar URL must not exceed 500 characters")
    String avatarUrl,

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    String bio,

    @Size(max = 500, message = "Expertise must not exceed 500 characters")
    String expertise,

    @Min(value = 0, message = "Experience years must be positive")
    Integer experienceYears
) {}
