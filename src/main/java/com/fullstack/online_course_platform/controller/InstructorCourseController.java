package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.common.enums.CourseStatus;
import com.fullstack.online_course_platform.dto.request.CourseCategoriesRequest;
import com.fullstack.online_course_platform.dto.request.CourseItemsRequest;
import com.fullstack.online_course_platform.dto.request.CreateCourseRequest;
import com.fullstack.online_course_platform.dto.request.UpdateCourseRequest;
import com.fullstack.online_course_platform.dto.request.UpdateCourseThumbnailRequest;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.CourseCreatedResponse;
import com.fullstack.online_course_platform.dto.response.CourseDetailResponse;
import com.fullstack.online_course_platform.dto.response.CourseStatusResponse;
import com.fullstack.online_course_platform.dto.response.CourseSummaryResponse;
import com.fullstack.online_course_platform.dto.response.CourseThumbnailResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/instructor/courses")
@RequiredArgsConstructor
@PreAuthorize("hasRole('INSTRUCTOR')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Instructor Courses", description = "Instructor course management APIs")
public class InstructorCourseController {

    private final CourseService courseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create draft course")
    public ApiResult<CourseCreatedResponse> createCourse(@Valid @RequestBody CreateCourseRequest request) {
        return ApiResult.of(HttpStatus.CREATED, "Course created successfully", courseService.createCourse(request));
    }

    @GetMapping
    @Operation(summary = "List current instructor courses")
    public ApiResult<PageResponse<CourseSummaryResponse>> getCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CourseStatus status,
            @PageableDefault(size = 20, sort = "updatedAt") Pageable pageable) {
        return ApiResult.of(HttpStatus.OK, "Instructor courses retrieved successfully",
                courseService.findInstructorCourses(keyword, status, pageable));
    }

    @GetMapping("/{courseId}")
    @Operation(summary = "Get owned course details")
    public ApiResult<CourseDetailResponse> getCourse(@PathVariable UUID courseId) {
        return ApiResult.of(HttpStatus.OK, "Course retrieved successfully", courseService.getInstructorCourse(courseId));
    }

    @PatchMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Update draft or rejected course")
    public void updateCourse(@PathVariable UUID courseId, @Valid @RequestBody UpdateCourseRequest request) {
        courseService.updateCourse(courseId, request);
    }

    @DeleteMapping("/{courseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete draft course")
    public void deleteCourse(@PathVariable UUID courseId) {
        courseService.deleteDraft(courseId);
    }

    @PutMapping("/{courseId}/categories")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Replace course categories")
    public void replaceCategories(@PathVariable UUID courseId,
                                  @Valid @RequestBody CourseCategoriesRequest request) {
        courseService.replaceCategories(courseId, request);
    }

    @PutMapping("/{courseId}/requirements")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Replace course requirements")
    public void replaceRequirements(@PathVariable UUID courseId,
                                    @Valid @RequestBody CourseItemsRequest request) {
        courseService.replaceRequirements(courseId, request);
    }

    @PutMapping("/{courseId}/learning-outcomes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Replace course learning outcomes")
    public void replaceLearningOutcomes(@PathVariable UUID courseId,
                                        @Valid @RequestBody CourseItemsRequest request) {
        courseService.replaceLearningOutcomes(courseId, request);
    }

    @PatchMapping("/{courseId}/thumbnail")
    @Operation(summary = "Update course thumbnail")
    public ApiResult<CourseThumbnailResponse> updateThumbnail(
            @PathVariable UUID courseId,
            @Valid @RequestBody UpdateCourseThumbnailRequest request) {
        return ApiResult.of(HttpStatus.OK, "Course thumbnail updated successfully",
                courseService.updateThumbnail(courseId, request));
    }

    @PostMapping("/{courseId}/submit-review")
    public ApiResult<CourseStatusResponse> submitReview(@PathVariable UUID courseId) {
        return statusResult("Course submitted for review", courseService.submitForReview(courseId));
    }

    @PostMapping("/{courseId}/pause")
    public ApiResult<CourseStatusResponse> pauseCourse(@PathVariable UUID courseId) {
        return statusResult("Course paused successfully", courseService.pauseOwnCourse(courseId));
    }

    @PostMapping("/{courseId}/resume")
    public ApiResult<CourseStatusResponse> resumeCourse(@PathVariable UUID courseId) {
        return statusResult("Course resumed successfully", courseService.resumeOwnCourse(courseId));
    }

    @PostMapping("/{courseId}/archive")
    public ApiResult<CourseStatusResponse> archiveCourse(@PathVariable UUID courseId) {
        return statusResult("Course archived successfully", courseService.archiveOwnCourse(courseId));
    }

    private ApiResult<CourseStatusResponse> statusResult(String message, CourseStatusResponse response) {
        return ApiResult.of(HttpStatus.OK, message, response);
    }
}