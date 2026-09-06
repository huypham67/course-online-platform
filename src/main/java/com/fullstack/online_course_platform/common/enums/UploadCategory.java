package com.fullstack.online_course_platform.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public enum UploadCategory {

    AVATAR(
        "avatars",
        Set.of("image/jpeg", "image/png", "image/webp"),
        2 * 1024 * 1024L // 2MB
    ),
    COURSE_THUMBNAIL(
        "courses/thumbnails",
        Set.of("image/jpeg", "image/png", "image/webp"),
        5 * 1024 * 1024L // 5MB
    ),
    COURSE_ATTACHMENT(
        "courses/attachments",
        Set.of("application/pdf", "application/zip", "application/x-zip-compressed", "video/mp4"),
        50 * 1024 * 1024L // 50MB
    ),
    INVOICE(
        "invoices",
        Set.of("application/pdf", "image/jpeg", "image/png"),
        10 * 1024 * 1024L // 10MB
    ),
    GENERAL_IMAGE(
        "images/general",
        Set.of("image/jpeg", "image/png", "image/webp"),
        5 * 1024 * 1024L // 5MB
    );

    private final String folder;
    private final Set<String> allowedMimeTypes;
    private final long maxSizeBytes;

    public boolean isAllowedMimeType(String mimeType) {
        return mimeType != null && allowedMimeTypes.contains(mimeType.toLowerCase());
    }

    public boolean isAllowedSize(long sizeBytes) {
        return sizeBytes > 0 && sizeBytes <= maxSizeBytes;
    }
}
