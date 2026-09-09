package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.utils.SecurityUtils;
import com.fullstack.online_course_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_course_platform.dto.request.UpdateAvatarRequest;
import com.fullstack.online_course_platform.dto.request.UpdateLearnerRequest;
import com.fullstack.online_course_platform.dto.response.AvatarResponse;
import com.fullstack.online_course_platform.dto.response.LearnerResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;
import com.fullstack.online_course_platform.exception.AppException;
import com.fullstack.online_course_platform.exception.ErrorCode;
import com.fullstack.online_course_platform.mapper.LearnerMapper;
import com.fullstack.online_course_platform.model.Learner;
import com.fullstack.online_course_platform.repository.LearnerRepository;
import com.fullstack.online_course_platform.repository.UserRepository;
import com.fullstack.online_course_platform.service.LearnerService;
import com.fullstack.online_course_platform.service.StorageService;
import com.fullstack.online_course_platform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "LEARNER-SERVICE")
public class LearnerServiceImpl implements LearnerService {

    private final LearnerRepository learnerRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final LearnerMapper learnerMapper;
    private final StorageService storageService;

    @Override
    @Transactional
    public UserResponse registerLearner(RegisterLearnerRequest request) {
        UserResponse userResponse = userService.createUser(
            request.email(), request.password(), request.fullName(), RoleType.LEARNER);
        Learner learner = Learner.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userResponse.id())))
                .bio(request.bio())
                .phone(request.phone())
                .dateOfBirth(request.dateOfBirth())
                .occupation(request.occupation())
                .learningGoal(request.learningGoal())
                .build();
        learnerRepository.save(learner);
        log.info("Learner registered: userId={}, email={}", userResponse.id(), request.email());
        return userResponse;
    }

    @Override
    @Transactional(readOnly = true)
    public LearnerResponse getCurrentProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));
        LearnerResponse response = learnerMapper.toLearnerResponse(learner);
        return resolvePresignedAvatarUrl(response, learner.getAvatarUrl());
    }

    @Override
    @Transactional
    public void updateCurrentProfile(UpdateLearnerRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));

        if (request.fullName() != null) {
            learner.getUser().setFullName(request.fullName());
        }
        if (request.bio() != null) {
            learner.setBio(request.bio());
        }
        if (request.phone() != null) {
            learner.setPhone(request.phone());
        }
        if (request.dateOfBirth() != null) {
            learner.setDateOfBirth(request.dateOfBirth());
        }
        if (request.occupation() != null) {
            learner.setOccupation(request.occupation());
        }
        if (request.learningGoal() != null) {
            learner.setLearningGoal(request.learningGoal());
        }

        learnerRepository.save(learner);
        log.info("Learner profile updated: userId={}", userId);
    }

    @Override
    @Transactional
    public AvatarResponse updateAvatar(UpdateAvatarRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));

        learner.setAvatarUrl(request.avatarUrl());
        learnerRepository.save(learner);
        log.info("Learner avatar updated: userId={}", userId);
        return AvatarResponse.builder()
                .avatarUrl(storageService.generatePresignedGetUrl(learner.getAvatarUrl()))
                .build();
    }

    private LearnerResponse resolvePresignedAvatarUrl(LearnerResponse response, String avatarUrl) {
        if (avatarUrl == null || avatarUrl.isBlank()) {
            return response;
        }
        String presignedUrl = storageService.generatePresignedGetUrl(avatarUrl);
        return LearnerResponse.builder()
                .id(response.id())
                .email(response.email())
                .role(response.role())
                .status(response.status())
                .fullName(response.fullName())
                .avatarUrl(presignedUrl)
                .bio(response.bio())
                .phone(response.phone())
                .dateOfBirth(response.dateOfBirth())
                .occupation(response.occupation())
                .learningGoal(response.learningGoal())
                .createdAt(response.createdAt())
                .updatedAt(response.updatedAt())
                .build();
    }
}
