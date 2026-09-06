package com.fullstack.online_course_platform.controller;

import com.fullstack.online_course_platform.dto.request.PresignedUploadRequest;
import com.fullstack.online_course_platform.dto.response.ApiResult;
import com.fullstack.online_course_platform.dto.response.PresignedUploadResponse;
import com.fullstack.online_course_platform.service.StorageService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/storage")
@RequiredArgsConstructor
@Tag(name = "Storage", description = "S3 File Storage & Presigned URL APIs")
@SecurityRequirement(name = "bearerAuth")
public class StorageController {

    private final StorageService storageService;

    @PostMapping("/presigned-upload-url")
    @Operation(
        summary = "Generate S3 Presigned Upload URL",
        description = "Generates a temporary S3 Presigned URL allowing client to upload file directly to Amazon S3."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Presigned upload URL generated successfully",
            content = @Content(schema = @Schema(implementation = PresignedUploadResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid file type or file size exceeded",
            content = @Content(schema = @Schema(implementation = ApiResult.class))
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized",
            content = @Content(schema = @Schema(implementation = ApiResult.class))
        )
    })
    public ApiResult<PresignedUploadResponse> getPresignedUploadUrl(@Valid @RequestBody PresignedUploadRequest request) {
        return ApiResult.of(
            HttpStatus.OK,
            "Presigned upload URL generated successfully",
            storageService.generatePresignedUploadUrl(request)
        );
    }
}
