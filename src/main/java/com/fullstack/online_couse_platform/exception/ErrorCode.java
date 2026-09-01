package com.fullstack.online_couse_platform.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    BAD_REQUEST(400, "Bad request", HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED(400, "Validation failed", HttpStatus.BAD_REQUEST),
    INVALID_BODY(400, "Malformed or missing request body", HttpStatus.BAD_REQUEST),

    // 401 Unauthorized
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(401, "Invalid email or password", HttpStatus.UNAUTHORIZED),

    // 403 Forbidden
    FORBIDDEN(403, "Access denied", HttpStatus.FORBIDDEN),

    // 404 Not Found
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),
    LEARNER_NOT_FOUND(404, "Learner profile not found", HttpStatus.NOT_FOUND),
    INSTRUCTOR_NOT_FOUND(404, "Instructor profile not found", HttpStatus.NOT_FOUND),

    // 409 Conflict
    EMAIL_ALREADY_EXISTS(409, "Email is already in use", HttpStatus.CONFLICT),
    USER_ALREADY_EXISTS(409, "User already exists", HttpStatus.CONFLICT),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
