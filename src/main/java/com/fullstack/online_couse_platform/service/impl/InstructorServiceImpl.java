package com.fullstack.online_couse_platform.service.impl;

import com.fullstack.online_couse_platform.common.enums.InstructorStatus;
import com.fullstack.online_couse_platform.common.utils.SecurityUtils;
import com.fullstack.online_couse_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_couse_platform.dto.response.InstructorResponse;
import com.fullstack.online_couse_platform.exception.AppException;
import com.fullstack.online_couse_platform.exception.ErrorCode;
import com.fullstack.online_couse_platform.mapper.InstructorMapper;
import com.fullstack.online_couse_platform.model.Instructor;
import com.fullstack.online_couse_platform.repository.InstructorRepository;
import com.fullstack.online_couse_platform.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {

    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;

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
        return instructorMapper.toInstructorResponse(updatedInstructor);
    }

    @Override
    @Transactional
    public InstructorResponse approveInstructor(UUID instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));

        if (instructor.getStatus() == InstructorStatus.APPROVED) {
            return instructorMapper.toInstructorResponse(instructor);
        }

        instructor.setStatus(InstructorStatus.APPROVED);
        Instructor updatedInstructor = instructorRepository.save(instructor);
        return instructorMapper.toInstructorResponse(updatedInstructor);
    }
}
