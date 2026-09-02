package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.utils.SecurityUtils;
import com.fullstack.online_course_platform.dto.request.RegisterInstructorRequest;
import com.fullstack.online_course_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_course_platform.dto.response.InstructorResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.mapper.InstructorMapper;
import com.fullstack.online_course_platform.model.Instructor;
import com.fullstack.online_course_platform.repository.InstructorRepository;
import com.fullstack.online_course_platform.repository.UserRepository;
import com.fullstack.online_course_platform.service.InstructorService;
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

    @Override
    @Transactional
    public UserResponse registerInstructor(RegisterInstructorRequest request) {
        UserResponse userResponse = userService.createUser(request.email(), request.password(), RoleType.INSTRUCTOR);
        Instructor instructor = Instructor.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userResponse.id())))
                .fullName(request.fullName())
                .avatarUrl(request.avatarUrl())
                .bio(request.bio())
                .expertise(request.expertise())
                .experienceYears(request.experienceYears())
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
        return instructorMapper.toInstructorResponse(instructor);
    }

    @Override
    @Transactional
    public InstructorResponse updateCurrentProfile(UpdateInstructorRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Instructor instructor = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if (request.fullName() != null) {
            instructor.setFullName(request.fullName());
        }
        if (request.avatarUrl() != null) {
            instructor.setAvatarUrl(request.avatarUrl());
        }
        if (request.bio() != null) {
            instructor.setBio(request.bio());
        }
        if (request.expertise() != null) {
            instructor.setExpertise(request.expertise());
        }
        if (request.experienceYears() != null) {
            instructor.setExperienceYears(request.experienceYears());
        }

        Instructor updatedInstructor = instructorRepository.save(instructor);
        log.info("Instructor profile updated: userId={}", userId);
        return instructorMapper.toInstructorResponse(updatedInstructor);
    }

    @Override
    @Transactional
    public InstructorResponse approveInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if (instructor.getStatus() != InstructorStatus.PENDING) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_PENDING);
        }

        instructor.setStatus(InstructorStatus.APPROVED);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        log.info("Instructor approved: instructorId={}", instructorId);
        return instructorMapper.toInstructorResponse(updatedInstructor);
    }

    @Override
    @Transactional
    public InstructorResponse rejectInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if (instructor.getStatus() != InstructorStatus.PENDING) {
            throw new AppException(ErrorCode.INSTRUCTOR_NOT_PENDING);
        }

        instructor.setStatus(InstructorStatus.REJECTED);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        log.info("Instructor rejected: instructorId={}", instructorId);
        return instructorMapper.toInstructorResponse(updatedInstructor);
    }
}
