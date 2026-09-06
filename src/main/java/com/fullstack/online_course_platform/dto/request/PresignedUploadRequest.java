package com.fullstack.online_course_platform.dto.request;

import com.fullstack.online_course_platform.common.enums.UploadCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record PresignedUploadRequest(
    @NotNull(message = "Upload category is required")
    UploadCategory category,

    @NotBlank(message = "File name is required")
    String fileName,

    @NotBlank(message = "Content type is required")
    String contentType,

    @Positive(message = "File size must be greater than 0")
    Long fileSize
) {}
