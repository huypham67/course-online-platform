package com.fullstack.online_course_platform.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateCategoryRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @Size(max = 120, message = "Slug must not exceed 120 characters")
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must use lowercase letters, numbers and hyphens")
        String slug,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        UUID parentId
) {
}