package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.InstructorResponse;
import com.fullstack.online_course_platform.service.InstructorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/instructor")
@RequiredArgsConstructor
@Tag(name = "Instructor", description = "Instructor APIs")
@SecurityRequirement(name = "bearerAuth")
public class InstructorController {

    private final InstructorService instructorService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Get current instructor profile", description = "Retrieve the authenticated instructor profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Instructor profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<InstructorResponse> getCurrentInstructorProfile() {
        return ApiResult.of(HttpStatus.OK, "Instructor profile retrieved successfully", instructorService.getCurrentProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Update current instructor profile", description = "Update the authenticated instructor profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<InstructorResponse> updateCurrentInstructorProfile(@Valid @RequestBody UpdateInstructorRequest request) {
        return ApiResult.of(HttpStatus.OK, "Instructor profile updated successfully", instructorService.updateCurrentProfile(request));
    }

    @PutMapping("/admin/{instructorId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Approve instructor registration", description = "Admin approves a pending instructor profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instructor approved successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Instructor profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<InstructorResponse> approveInstructor(@PathVariable UUID instructorId) {
        return ApiResult.of(HttpStatus.OK, "Instructor approved successfully", instructorService.approveInstructor(instructorId));
    }

    @PutMapping("/admin/{instructorId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Reject instructor registration", description = "Admin rejects a pending instructor profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Instructor rejected successfully",
                    content = @Content(schema = @Schema(implementation = InstructorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Instructor profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "409", description = "Instructor is not pending",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<InstructorResponse> rejectInstructor(@PathVariable UUID instructorId) {
        return ApiResult.of(HttpStatus.OK, "Instructor rejected successfully", instructorService.rejectInstructor(instructorId));
    }
}
