package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PresignedUploadResponse(
    @JsonProperty("upload_url")
    String uploadUrl,

    @JsonProperty("s3_key")
    String s3Key,

    @JsonProperty("public_url")
    String publicUrl,

    @JsonProperty("expires_in_seconds")
    Long expiresInSeconds
) {}
