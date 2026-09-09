package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.online_course_platform.common.enums.CourseLevel;
import com.fullstack.online_course_platform.common.enums.CourseStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Builder
public record CourseDetailResponse(
        String id,
        String title,
        String slug,
        @JsonProperty("short_description") String shortDescription,
        String description,
        @JsonProperty("thumbnail_url") String thumbnailUrl,
        BigDecimal price,
        CourseLevel level,
        String language,
        CourseStatus status,
        @JsonProperty("rejection_reason") String rejectionReason,
        InstructorInfo instructor,
        List<CategoryInfo> categories,
        List<CourseItem> requirements,
        @JsonProperty("learning_outcomes") List<CourseItem> learningOutcomes,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt,
        @JsonProperty("published_at") Instant publishedAt
) {
    @Builder
    public record InstructorInfo(String id, @JsonProperty("full_name") String fullName) {
    }

    @Builder
    public record CategoryInfo(String id, String name, String slug) {
    }

    @Builder
    public record CourseItem(String id, String content, @JsonProperty("sort_order") int sortOrder) {
    }
}