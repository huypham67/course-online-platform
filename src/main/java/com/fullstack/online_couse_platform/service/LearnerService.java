package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.dto.request.UpdateLearnerRequest;
import com.fullstack.online_couse_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.LearnerResponse;
import com.fullstack.online_couse_platform.dto.response.UserResponse;

public interface LearnerService {

    UserResponse registerLearner(RegisterLearnerRequest request);

    LearnerResponse getCurrentProfile();

    LearnerResponse updateCurrentProfile(UpdateLearnerRequest request);
}
