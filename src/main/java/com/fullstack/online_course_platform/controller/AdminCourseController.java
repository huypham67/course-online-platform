package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.common.enums.CourseStatus;
import com.fullstack.online_course_platform.dto.request.RejectCourseRequest;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.CourseDetailResponse;
import com.fullstack.online_course_platform.dto.response.CourseStatusResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.service.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/courses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Courses", description = "Course moderation APIs")
public class AdminCourseController {

    private final CourseService courseService;

    @GetMapping
    public ApiResult<PageResponse<CourseSummaryResponse>> getCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CourseStatus status,
            @RequestParam(required = false) UUID instructorId,
            @PageableDefault(size = 20, sort = "updatedAt") Pageable pageable) {
        return ApiResult.of(HttpStatus.OK, "Courses retrieved successfully",
                courseService.findAdminCourses(keyword, status, instructorId, pageable));
    }

    @GetMapping("/{courseId}")
    public ApiResult<CourseDetailResponse> getCourse(@PathVariable UUID courseId) {
        return ApiResult.of(HttpStatus.OK, "Course retrieved successfully", courseService.getAdminCourse(courseId));
    }

    @PostMapping("/{courseId}/approve")
    public ApiResult<CourseStatusResponse> approve(@PathVariable UUID courseId) {
        return statusResult("Course approved successfully", courseService.approveCourse(courseId));
    }

    @PostMapping("/{courseId}/reject")
    public ApiResult<CourseStatusResponse> reject(@PathVariable UUID courseId,
                                                   @Valid @RequestBody RejectCourseRequest request) {
        return statusResult("Course rejected successfully", courseService.rejectCourse(courseId, request.reason()));
    }

    @PostMapping("/{courseId}/pause")
    public ApiResult<CourseStatusResponse> pause(@PathVariable UUID courseId) {
        return statusResult("Course paused successfully", courseService.pauseCourse(courseId));
    }

    @PostMapping("/{courseId}/resume")
    public ApiResult<CourseStatusResponse> resume(@PathVariable UUID courseId) {
        return statusResult("Course resumed successfully", courseService.resumeCourse(courseId));
    }

    @PostMapping("/{courseId}/archive")
    public ApiResult<CourseStatusResponse> archive(@PathVariable UUID courseId) {
        return statusResult("Course archived successfully", courseService.archiveCourse(courseId));
    }

    private ApiResult<CourseStatusResponse> statusResult(String message, CourseStatusResponse response) {
        return ApiResult.of(HttpStatus.OK, message, response);
    }
}