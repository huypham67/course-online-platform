package com.fullstack.online_course_platform.exception;

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
    INVALID_FILE_TYPE(400, "File type is not supported for this category", HttpStatus.BAD_REQUEST),
    FILE_SIZE_EXCEEDED(400, "File size exceeds the allowable limit for this category", HttpStatus.BAD_REQUEST),
    INVALID_CURRENT_PASSWORD(400, "Current password is incorrect", HttpStatus.BAD_REQUEST),
    NEW_PASSWORD_SAME_AS_CURRENT(400, "New password must differ from current password", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_RESET_TOKEN(400, "Invalid or expired password reset token", HttpStatus.BAD_REQUEST),

    // 401 Unauthorized
    UNAUTHORIZED(401, "Unauthorized", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(401, "Invalid email or password", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(401, "Invalid or expired refresh token", HttpStatus.UNAUTHORIZED),

    // 403 Forbidden
    FORBIDDEN(403, "Access denied", HttpStatus.FORBIDDEN),

    // 404 Not Found
    USER_NOT_FOUND(404, "User not found", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(404, "Role not found", HttpStatus.NOT_FOUND),
    LEARNER_NOT_FOUND(404, "Learner profile not found", HttpStatus.NOT_FOUND),
    INSTRUCTOR_NOT_FOUND(404, "Instructor profile not found", HttpStatus.NOT_FOUND),
    COURSE_NOT_FOUND(404, "Course not found", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND(404, "Category not found", HttpStatus.NOT_FOUND),

    // 409 Conflict
    EMAIL_ALREADY_EXISTS(409, "Email is already in use", HttpStatus.CONFLICT),
    USER_ALREADY_EXISTS(409, "User already exists", HttpStatus.CONFLICT),
    INSTRUCTOR_ALREADY_APPROVED(409, "Instructor is already approved", HttpStatus.CONFLICT),
    INSTRUCTOR_NOT_PENDING(409, "Instructor is not in pending status", HttpStatus.CONFLICT),
    INSTRUCTOR_STATUS_CONFLICT(409, "Instructor status does not allow this operation", HttpStatus.CONFLICT),
    COURSE_STATUS_CONFLICT(409, "Course status does not allow this operation", HttpStatus.CONFLICT),
    CATEGORY_SLUG_ALREADY_EXISTS(409, "Category slug is already in use", HttpStatus.CONFLICT),
    CATEGORY_IN_USE(409, "Category is assigned to one or more courses", HttpStatus.CONFLICT),
    CATEGORY_HAS_CHILDREN(409, "Category has child categories", HttpStatus.CONFLICT),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(500, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatus httpStatus;
}
