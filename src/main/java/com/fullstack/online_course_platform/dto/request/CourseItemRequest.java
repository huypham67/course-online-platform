package com.fullstack.online_course_platform.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CourseItemRequest(
        @NotBlank(message = "Content is required")
        @Size(max = 500, message = "Content must not exceed 500 characters")
        String content,

        @Min(value = 0, message = "Sort order must not be negative")
        int sortOrder
) {
}