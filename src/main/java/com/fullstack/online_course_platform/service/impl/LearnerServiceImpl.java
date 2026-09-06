package com.fullstack.online_course_platform.service.impl;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.utils.SecurityUtils;
import com.fullstack.online_course_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_course_platform.dto.request.UpdateAvatarRequest;
import com.fullstack.online_course_platform.dto.request.UpdateLearnerRequest;
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
        UserResponse userResponse = userService.createUser(request.email(), request.password(), RoleType.LEARNER);
        Learner learner = Learner.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userResponse.id())))
                .fullName(request.fullName())
                .bio(request.bio())
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
    public LearnerResponse updateCurrentProfile(UpdateLearnerRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));

        if (request.fullName() != null) {
            learner.setFullName(request.fullName());
        }
        if (request.bio() != null) {
            learner.setBio(request.bio());
        }

        Learner updatedLearner = learnerRepository.save(learner);
        log.info("Learner profile updated: userId={}", userId);
        LearnerResponse response = learnerMapper.toLearnerResponse(updatedLearner);
        return resolvePresignedAvatarUrl(response, updatedLearner.getAvatarUrl());
    }

    @Override
    @Transactional
    public LearnerResponse updateAvatar(UpdateAvatarRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));

        learner.setAvatarUrl(request.avatarUrl());
        Learner updatedLearner = learnerRepository.save(learner);
        log.info("Learner avatar updated: userId={}", userId);
        LearnerResponse response = learnerMapper.toLearnerResponse(updatedLearner);
        return resolvePresignedAvatarUrl(response, updatedLearner.getAvatarUrl());
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
                .createdAt(response.createdAt())
                .updatedAt(response.updatedAt())
                .build();
    }
}
