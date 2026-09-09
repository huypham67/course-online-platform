package com.fullstack.online_course_platform.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCourseThumbnailRequest(
        @NotBlank(message = "Thumbnail URL is required")
        @Size(max = 500, message = "Thumbnail URL must not exceed 500 characters")
        @JsonProperty("thumbnail_url")
        String thumbnailUrl
) {
}