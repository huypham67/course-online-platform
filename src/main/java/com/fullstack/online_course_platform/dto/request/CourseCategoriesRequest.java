package com.fullstack.online_course_platform.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CourseCategoriesRequest(
        @NotNull(message = "Category IDs are required")
        Set<UUID> categoryIds
) {
}