package com.fullstack.online_course_platform.service;

import com.fullstack.online_course_platform.dto.request.RegisterLearnerRequest;
import com.fullstack.online_course_platform.dto.request.UpdateAvatarRequest;
import com.fullstack.online_course_platform.dto.request.UpdateLearnerRequest;
import com.fullstack.online_course_platform.dto.response.AvatarResponse;
import com.fullstack.online_course_platform.dto.response.LearnerResponse;
import com.fullstack.online_course_platform.dto.response.UserResponse;

public interface LearnerService {

    UserResponse registerLearner(RegisterLearnerRequest request);

    LearnerResponse getCurrentProfile();

    void updateCurrentProfile(UpdateLearnerRequest request);

    AvatarResponse updateAvatar(UpdateAvatarRequest request);
}
