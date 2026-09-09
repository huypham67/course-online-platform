package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.dto.request.UpdateAvatarRequest;
import com.fullstack.online_course_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.AvatarResponse;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @PatchMapping("/me")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Update current instructor profile", description = "Update the authenticated instructor profile")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void updateCurrentInstructorProfile(@Valid @RequestBody UpdateInstructorRequest request) {
                instructorService.updateCurrentProfile(request);
    }

    @PatchMapping("/me/avatar")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(summary = "Update current instructor avatar", description = "Update the authenticated instructor's avatar URL")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avatar updated successfully",
                    content = @Content(schema = @Schema(implementation = AvatarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Instructor profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
        public ApiResult<AvatarResponse> updateAvatar(@Valid @RequestBody UpdateAvatarRequest request) {
        return ApiResult.of(HttpStatus.OK, "Avatar updated successfully", instructorService.updateAvatar(request));
    }

}
