package com.fullstack.online_course_platform.dto.request;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateLearnerRequest(
    @Size(max = 150, message = "Full name must not exceed 150 characters")
    String fullName,

    @Size(max = 1000, message = "Bio must not exceed 1000 characters")
    String bio,

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    String phone,

    @Past(message = "Date of birth must be in the past")
    LocalDate dateOfBirth,

    @Size(max = 100, message = "Occupation must not exceed 100 characters")
    String occupation,

    @Size(max = 500, message = "Learning goal must not exceed 500 characters")
    String learningGoal
) {}
