package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.dto.request.UpdateLearnerRequest;
import com.fullstack.online_couse_platform.dto.response.LearnerResponse;

public interface LearnerService {

    LearnerResponse getCurrentProfile();

    LearnerResponse updateCurrentProfile(UpdateLearnerRequest request);
}
