package com.fullstack.online_couse_platform.service;

import com.fullstack.online_couse_platform.dto.request.UpdateInstructorRequest;
import com.fullstack.online_couse_platform.dto.response.InstructorResponse;

import java.util.UUID;

public interface InstructorService {

    InstructorResponse getCurrentProfile();

    InstructorResponse updateCurrentProfile(UpdateInstructorRequest request);

    InstructorResponse approveInstructor(UUID instructorId);
}
