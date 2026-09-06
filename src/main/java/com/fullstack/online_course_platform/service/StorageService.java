package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.request.PresignedUploadRequest;
import com.fullstack.online_course_platform.dto.response.PresignedUploadResponse;

public interface StorageService {

    PresignedUploadResponse generatePresignedUploadUrl(PresignedUploadRequest request);

    String generatePresignedGetUrl(String s3KeyOrUrl);

    boolean doesObjectExist(String s3Key);

    void deleteObject(String s3Key);
}
