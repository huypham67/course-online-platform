package com.fullstack.online_course_platform.dto.request;

import com.fullstack.online_course_platform.common.enums.CourseLevel;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateCourseRequest(
        @Size(max = 200, message = "Title must not exceed 200 characters")
        @Pattern(regexp = ".*\\S.*", message = "Title must not be blank")
        String title,

        @Size(max = 500, message = "Short description must not exceed 500 characters")
        String shortDescription,

        String description,

        @DecimalMin(value = "0.00", message = "Price must not be negative")
        BigDecimal price,

        CourseLevel level,

        @Size(max = 50, message = "Language must not exceed 50 characters")
        @Pattern(regexp = ".*\\S.*", message = "Language must not be blank")
        String language
) {
}