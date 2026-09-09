package com.fullstack.online_course_platform.dto.request;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateCourseRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 200, message = "Title must not exceed 200 characters")
        String title,

        @Size(max = 500, message = "Short description must not exceed 500 characters")
        String shortDescription,

        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.00", message = "Price must not be negative")
        BigDecimal price,

        @NotNull(message = "Level is required")
        CourseLevel level,

        @NotBlank(message = "Language is required")
        @Size(max = 50, message = "Language must not exceed 50 characters")
        String language
) {
}