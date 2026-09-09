package com.fullstack.online_course_platform.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateCategoryRequest(
        @Size(max = 100, message = "Name must not exceed 100 characters")
        @Pattern(regexp = ".*\\S.*", message = "Name must not be blank")
        String name,

        @Size(max = 120, message = "Slug must not exceed 120 characters")
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Slug must use lowercase letters, numbers and hyphens")
        String slug,

        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        UUID parentId,

        boolean clearParent
) {
}