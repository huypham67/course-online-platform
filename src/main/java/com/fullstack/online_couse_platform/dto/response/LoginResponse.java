package com.fullstack.online_couse_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record LoginResponse(
    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("expires_in")
    Long expiresIn
) {
    public LoginResponse {
        if (tokenType == null) {
            tokenType = "Bearer";
        }
    }
}
