package com.fullstack.online_couse_platform.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResult<T>(
    int code,
    String message,
    T data
) {
    public static <T> ApiResult<T> of(HttpStatus status, String message, T data) {
        return ApiResult.<T>builder()
                .code(status.value())
                .message(message)
                .data(data)
                .build();
    }
}
