package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.InstructorResponse;
import com.fullstack.online_course_platform.dto.response.InstructorStatusResponse;
import com.fullstack.online_course_platform.dto.response.PageResponse;
import com.fullstack.online_course_platform.service.InstructorService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/instructors")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Admin Instructors", description = "Instructor moderation APIs")
public class AdminInstructorController {

    private final InstructorService instructorService;

    @GetMapping
    public ApiResult<PageResponse<InstructorResponse>> getInstructors(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) InstructorStatus status,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {
        return ApiResult.of(HttpStatus.OK, "Instructors retrieved successfully",
                instructorService.findInstructors(keyword, status, pageable));
    }

    @GetMapping("/{instructorId}")
    public ApiResult<InstructorResponse> getInstructor(@PathVariable UUID instructorId) {
        return ApiResult.of(HttpStatus.OK, "Instructor retrieved successfully",
                instructorService.getInstructor(instructorId));
    }

    @PostMapping("/{instructorId}/approve")
    public ApiResult<InstructorStatusResponse> approve(@PathVariable UUID instructorId) {
        return ApiResult.of(HttpStatus.OK, "Instructor approved successfully",
                instructorService.approveInstructor(instructorId));
    }

    @PostMapping("/{instructorId}/reject")
    public ApiResult<InstructorStatusResponse> reject(@PathVariable UUID instructorId) {
        return ApiResult.of(HttpStatus.OK, "Instructor rejected successfully",
                instructorService.rejectInstructor(instructorId));
    }
}