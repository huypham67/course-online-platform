package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.PublicInstructorResponse;
import com.fullstack.online_course_platform.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/instructors")
@RequiredArgsConstructor
public class PublicInstructorController {

    private final InstructorService instructorService;

    @GetMapping("/{instructorId}")
    public ApiResult<PublicInstructorResponse> getInstructor(@PathVariable UUID instructorId) {
        return ApiResult.of(HttpStatus.OK, "Instructor retrieved successfully",
                instructorService.getPublicInstructor(instructorId));
    }
}