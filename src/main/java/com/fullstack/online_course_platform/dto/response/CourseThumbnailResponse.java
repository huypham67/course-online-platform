package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record CourseThumbnailResponse(
        @JsonProperty("thumbnail_url") String thumbnailUrl
) {
}