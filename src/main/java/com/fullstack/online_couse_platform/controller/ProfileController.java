package com.fullstack.online_couse_platform.controller;

import com.fullstack.online_couse_platform.dto.request.UpdateInstructorProfileRequest;
import com.fullstack.online_couse_platform.dto.request.UpdateLearnerProfileRequest;
import com.fullstack.online_couse_platform.dto.response.ApiResult;
import com.fullstack.online_couse_platform.dto.response.InstructorProfileResponse;
import com.fullstack.online_couse_platform.dto.response.LearnerProfileResponse;
import com.fullstack.online_couse_platform.service.ProfileService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "User Profile APIs")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping("/learner")
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Get learner profile", description = "Retrieve the current authenticated learner's profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LearnerProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Learner profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<LearnerProfileResponse> getLearnerProfile() {
        return ApiResult.of(HttpStatus.OK, "Learner profile retrieved successfully", profileService.getLearnerProfile());
    }

    @PutMapping("/learner")
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Update learner profile", description = "Update the current authenticated learner's profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = LearnerProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Learner profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<LearnerProfileResponse> updateLearnerProfile(@Valid @RequestBody UpdateLearnerProfileRequest request) {
        return ApiResult.of(HttpStatus.OK, "Learner profile updated successfully", profileService.updateLearnerProfile(request));
    }

    @GetMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Get instructor profile", description = "Retrieve the current authenticated instructor's profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = InstructorProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Instructor profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<InstructorProfileResponse> getInstructorProfile() {
        return ApiResult.of(HttpStatus.OK, "Instructor profile retrieved successfully", profileService.getInstructorProfile());
    }

    @PutMapping("/instructor")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Update instructor profile", description = "Update the current authenticated instructor's profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = InstructorProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Instructor profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<InstructorProfileResponse> updateInstructorProfile(@Valid @RequestBody UpdateInstructorProfileRequest request) {
        return ApiResult.of(HttpStatus.OK, "Instructor profile updated successfully", profileService.updateInstructorProfile(request));
    }
}
