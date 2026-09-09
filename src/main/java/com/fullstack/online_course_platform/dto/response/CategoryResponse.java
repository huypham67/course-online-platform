package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.List;

@Builder
public record CategoryResponse(
        String id,
        @JsonProperty("parent_id") String parentId,
        String name,
        String slug,
        String description,
        List<CategoryResponse> children
) {
}