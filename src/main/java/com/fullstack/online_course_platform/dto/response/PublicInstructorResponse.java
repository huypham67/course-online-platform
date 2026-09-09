package com.fullstack.online_course_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record PublicInstructorResponse(
        String id,
        @JsonProperty("full_name") String fullName,
        @JsonProperty("avatar_url") String avatarUrl,
        String headline,
        String bio,
        String expertise,
        @JsonProperty("experience_years") Integer experienceYears,
        @JsonProperty("website_url") String websiteUrl,
        @JsonProperty("linkedin_url") String linkedinUrl
) {
}