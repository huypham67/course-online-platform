package com.fullstack.online_couse_platform.controller;

import com.fullstack.online_couse_platform.dto.request.UpdateLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.ApiResult;
import com.fullstack.online_couse_platform.dto.response.LearnerResponse;
import com.fullstack.online_couse_platform.service.LearnerService;
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
@RequestMapping("/api/v1/learner")
@RequiredArgsConstructor
@Tag(name = "Learner", description = "Learner APIs")
@SecurityRequirement(name = "bearerAuth")
public class LearnerController {

    private final LearnerService learnerService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Get current learner profile", description = "Retrieve the authenticated learner profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = LearnerResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "404", description = "Learner profile not found",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<LearnerResponse> getCurrentProfile() {
        return ApiResult.of(HttpStatus.OK, "Learner profile retrieved successfully", learnerService.getCurrentProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('LEARNER')")
    @Operation(summary = "Update current learner profile", description = "Update the authenticated learner profile")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = LearnerResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed",
                    content = @Content(schema = @Schema(implementation = ApiResult.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                    content = @Content(schema = @Schema(implementation = ApiResult.class)))
    })
    public ApiResult<LearnerResponse> updateCurrentProfile(@Valid @RequestBody UpdateLearnerRequest request) {
        return ApiResult.of(HttpStatus.OK, "Learner profile updated successfully", learnerService.updateCurrentProfile(request));
    }
}
