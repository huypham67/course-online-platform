package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.utils.SecurityUtils;
import com.fullstack.online_course_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_course_platform.dto.request.UpdateAvatarRequest;
import com.fullstack.online_course_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_course_platform.dto.response.AvatarResponse;
import com.fullstack.online_course_platform.dto.response.InstructorResponse;
import com.fullstack.online_course_platform.dto.response.InstructorStatusResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.mapper.InstructorMapper;
import com.fullstack.online_course_platform.model.Instructor;
import com.fullstack.online_course_platform.repository.InstructorRepository;
import com.fullstack.online_course_platform.repository.UserRepository;
import com.fullstack.online_course_platform.service.InstructorService;
import com.fullstack.online_course_platform.service.StorageService;
import com.fullstack.online_course_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "INSTRUCTOR-SERVICE")
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final InstructorMapper instructorMapper;
    private final StorageService storageService;

    @Override
    @Transactional
    public UserResponse registerInstructor(RegisterInstructorRequest request) {
        UserResponse userResponse = userService.createUser(
            request.email(), request.password(), request.fullName(), RoleType.INSTRUCTOR);
        Instructor instructor = Instructor.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userResponse.id())))
                .bio(request.bio())
                .phone(request.phone())
                .headline(request.headline())
                .expertise(request.expertise())
                .experienceYears(request.experienceYears())
                .websiteUrl(request.websiteUrl())
                .linkedinUrl(request.linkedinUrl())
                .status(InstructorStatus.PENDING)
                .build();
        instructorRepository.save(instructor);
        log.info("Instructor registered: userId={}, email={}, status={}", userResponse.id(), request.email(), InstructorStatus.PENDING);
        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorResponse getCurrentProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        Instructor instructor = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        InstructorResponse response = instructorMapper.toInstructorResponse(instructor);
        return resolvePresignedAvatarUrl(response, instructor.getAvatarUrl());
    }

    @Override
    @Transactional
    public void updateCurrentProfile(UpdateInstructorRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Instructor instructor = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if (request.fullName() != null) {
            instructor.getUser().setFullName(request.fullName());
        }
        if (request.bio() != null) {
            instructor.setBio(request.bio());
        }
        if (request.phone() != null) {
            instructor.setPhone(request.phone());
        }
        if (request.headline() != null) {
            instructor.setHeadline(request.headline());
        }
        if (request.expertise() != null) {
            instructor.setExpertise(request.expertise());
        }
        if (request.experienceYears() != null) {
            instructor.setExperienceYears(request.experienceYears());
        }
        if (request.websiteUrl() != null) {
            instructor.setWebsiteUrl(request.websiteUrl());
        }
        if (request.linkedinUrl() != null) {
            instructor.setLinkedinUrl(request.linkedinUrl());
        }

        instructorRepository.save(instructor);
        log.info("Instructor profile updated: userId={}", userId);
    }

    @Override
    @Transactional
    public AvatarResponse updateAvatar(UpdateAvatarRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Instructor instructor = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        instructor.setAvatarUrl(request.avatarUrl());
        instructorRepository.save(instructor);
        log.info("Instructor avatar updated: userId={}", userId);
        return AvatarResponse.builder()
                .avatarUrl(storageService.generatePresignedGetUrl(request.avatarUrl()))
                .build();
    }

    @Override
    @Transactional
    public InstructorStatusResponse approveInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if (instructor.getStatus() != InstructorStatus.PENDING) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_PENDING);
        }

        instructor.setStatus(InstructorStatus.APPROVED);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        log.info("Instructor approved: instructorId={}", instructorId);
        return InstructorStatusResponse.builder()
                .id(updatedInstructor.getId().toString())
                .instructorStatus(updatedInstructor.getStatus())
                .build();
    }

    @Override
    @Transactional
    public InstructorStatusResponse rejectInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if (instructor.getStatus() != InstructorStatus.PENDING) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_PENDING);
        }

        instructor.setStatus(InstructorStatus.REJECTED);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        log.info("Instructor rejected: instructorId={}", instructorId);
        return InstructorStatusResponse.builder()
                .id(updatedInstructor.getId().toString())
                .instructorStatus(updatedInstructor.getStatus())
                .build();
    }

    private InstructorResponse resolvePresignedAvatarUrl(InstructorResponse response, String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return response;
        }
        String presignedUrl = storageService.generatePresignedGetUrl(avatarUrl);
        return InstructorResponse.builder()
                .id(response.id())
                .email(response.email())
                .role(response.role())
                .status(response.status())
                .fullName(response.fullName())
                .avatarUrl(presignedUrl)
                .bio(response.bio())
                .phone(response.phone())
                .headline(response.headline())
                .expertise(response.expertise())
                .experienceYears(response.experienceYears())
                .websiteUrl(response.websiteUrl())
                .linkedinUrl(response.linkedinUrl())
                .instructorStatus(response.instructorStatus())
                .createdAt(response.createdAt())
                .updatedAt(response.updatedAt())
                .build();
    }
}
