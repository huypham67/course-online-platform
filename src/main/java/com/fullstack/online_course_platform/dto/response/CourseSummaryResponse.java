package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.common.enums.CourseStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CourseSummaryResponse(
        String id,
        String title,
        String slug,
        @JsonProperty("short_description") String shortDescription,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        BigDecimal price,
        CourseLevel level,
        String language,
        CourseStatus status,
        @JsonProperty("instructor_name") String instructorName
) {
}