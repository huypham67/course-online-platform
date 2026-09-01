package com.fullstack.online_couse_platform.service.impl;

import com.fullstack.online_couse_platform.common.utils.SecurityUtils;
import com.fullstack.online_couse_platform.dto.request.UpdateInstructorProfileRequest;
import com.fullstack.online_couse_platform.dto.request.UpdateLearnerProfileRequest;
import com.fullstack.online_couse_platform.dto.response.InstructorProfileResponse;
import com.fullstack.online_couse_platform.dto.response.LearnerProfileResponse;
import com.fullstack.online_couse_platform.exception.AppException;
import com.fullstack.online_couse_platform.exception.ErrorCode;
import com.fullstack.online_couse_platform.mapper.InstructorProfileMapper;
import com.fullstack.online_couse_platform.mapper.LearnerProfileMapper;
import com.fullstack.online_couse_platform.model.Instructor;
import com.fullstack.online_couse_platform.model.Learner;
import com.fullstack.online_couse_platform.model.User;
import com.fullstack.online_couse_platform.repository.InstructorRepository;
import com.fullstack.online_couse_platform.repository.LearnerRepository;
import com.fullstack.online_couse_platform.repository.UserRepository;
import com.fullstack.online_couse_platform.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final LearnerRepository learnerRepository;
    private final InstructorRepository instructorRepository;
    private final LearnerProfileMapper learnerProfileMapper;
    private final InstructorProfileMapper instructorProfileMapper;

    @Override
    @Transactional(readOnly = true)
    public LearnerProfileResponse getLearnerProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));
        return learnerProfileMapper.toLearnerProfileResponse(learner);
    }

    @Override
    @Transactional
    public LearnerProfileResponse updateLearnerProfile(UpdateLearnerProfileRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));

        if (request.fullName() != null) {
            learner.setFullName(request.fullName());
        }
        if (request.avatarUrl() != null) {
            learner.setAvatarUrl(request.avatarUrl());
        }
        if (request.bio() != null) {
            learner.setBio(request.bio());
        }

        Learner updatedLearner = learnerRepository.save(learner);
        return learnerProfileMapper.toLearnerProfileResponse(updatedLearner);
    }

    @Override
    @Transactional(readOnly = true)
    public InstructorProfileResponse getInstructorProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        Instructor instructor = instructorRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.INSTRUCTOR_NOT_FOUND));
        return instructorProfileMapper.toInstructorProfileResponse(instructor);
    }

    @Override
    @Transactional
    public InstructorProfileResponse updateInstructorProfile(UpdateInstructorProfileRequest request) {
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
        return instructorProfileMapper.toInstructorProfileResponse(updatedInstructor);
    }
}
