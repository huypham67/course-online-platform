package com.fullstack.online_couse_platform.service.impl;

import com.fullstack.online_couse_platform.common.utils.SecurityUtils;
import com.fullstack.online_couse_platform.dto.request.UpdateLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.LearnerResponse;
import com.fullstack.online_couse_platform.exception.AppException;
import com.fullstack.online_couse_platform.exception.ErrorCode;
import com.fullstack.online_couse_platform.mapper.LearnerMapper;
import com.fullstack.online_couse_platform.model.Learner;
import com.fullstack.online_couse_platform.repository.LearnerRepository;
import com.fullstack.online_couse_platform.service.LearnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LearnerServiceImpl implements LearnerService {

    private final LearnerRepository learnerRepository;
    private final LearnerMapper learnerMapper;

    @Override
    @Transactional(readOnly = true)
    public LearnerResponse getCurrentProfile() {
        UUID userId = SecurityUtils.getCurrentUserId();
        Learner learner = learnerRepository.findByUserId(userId)
                .orElseThrow(() -> new AppException(ErrorCode.LEARNER_NOT_FOUND));
        return learnerMapper.toLearnerResponse(learner);
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
        if (request.avatarUrl() != null) {
            learner.setAvatarUrl(request.avatarUrl());
        }
        if (request.bio() != null) {
            learner.setBio(request.bio());
        }

        Learner updatedLearner = learnerRepository.save(learner);
        return learnerMapper.toLearnerResponse(updatedLearner);
    }
}
