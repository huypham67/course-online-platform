package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.config.AwsS3Properties;
import com.fullstack.online_course_platform.dto.request.PresignedUploadRequest;
import com.fullstack.online_course_platform.dto.response.PresignedUploadResponse;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final S3Presigner s3Presigner;
    private final S3Client s3Client;
    private final AwsS3Properties s3Properties;

    @Override
    public PresignedUploadResponse generatePresignedUploadUrl(PresignedUploadRequest request) {
        var category = request.category();

        // 1. Validate MIME Type
        if (!category.isAllowedMimeType(request.contentType())) {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }

        // 2. Validate File Size
        if (request.fileSize() != null && !category.isAllowedSize(request.fileSize())) {
            throw new AppException(ErrorCode.FILE_SIZE_EXCEEDED);
        }

        // 3. Generate unique S3 Key: folder/yyyy/MM/uuid.ext
        String extension = extractExtension(request.fileName());
        String datePath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String s3Key = String.format("%s/%s/%s%s", category.getFolder(), datePath, UUID.randomUUID(), extension);

        // 4. Build S3 PutObjectRequest & Presign request
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(s3Properties.getBucketName())
                .key(s3Key)
                .contentType(request.contentType())
                .build();

        Duration duration = Duration.ofMinutes(s3Properties.getPresignedExpiryMinutes());

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(duration)
                .putObjectRequest(objectRequest)
                .build();

        String uploadUrl = s3Presigner.presignPutObject(presignRequest).url().toString();
        String publicUrl = String.format("%s/%s", s3Properties.getPublicBaseUrl(), s3Key);

        return PresignedUploadResponse.builder()
                .uploadUrl(uploadUrl)
                .s3Key(s3Key)
                .publicUrl(publicUrl)
                .expiresInSeconds(duration.toSeconds())
                .build();
    }

    @Override
    public boolean doesObjectExist(String s3Key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build());
            return true;
        } catch (NoSuchKeyException e) {
            return false;
        } catch (Exception e) {
            log.error("Error checking S3 object existence for key: {}", s3Key, e);
            return false;
        }
    }

    @Override
    public void deleteObject(String s3Key) {
        try {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(s3Key)
                    .build());
        } catch (Exception e) {
            log.error("Error deleting S3 object with key: {}", s3Key, e);
        }
    }

    private String extractExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        }
        return "";
    }
}
