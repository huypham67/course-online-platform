package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.dto.request.UpdateInstructorProfileRequest;
import com.fullstack.online_couse_platform.dto.request.UpdateLearnerProfileRequest;
import com.fullstack.online_couse_platform.dto.response.InstructorProfileResponse;
import com.fullstack.online_couse_platform.dto.response.LearnerProfileResponse;

public interface ProfileService {

    LearnerProfileResponse getLearnerProfile();

    LearnerProfileResponse updateLearnerProfile(UpdateLearnerProfileRequest request);

    InstructorProfileResponse getInstructorProfile();

    InstructorProfileResponse updateInstructorProfile(UpdateInstructorProfileRequest request);
}
