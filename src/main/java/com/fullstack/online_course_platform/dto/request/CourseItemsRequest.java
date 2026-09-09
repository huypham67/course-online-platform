package com.fullstack.online_course_platform.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CourseItemsRequest(
        @NotNull(message = "Items are required")
        List<@Valid CourseItemRequest> items
) {
}