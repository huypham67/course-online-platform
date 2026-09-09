package com.fullstack.online_course_platform.model;

import com.fullstack.online_course_platform.common.enums.InstructorStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Instructor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "headline", length = 200)
    private String headline;

    @Column(name = "expertise", length = 500)
    private String expertise;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "website_url", length = 500)
    private String websiteUrl;

    @Column(name = "linkedin_url", length = 500)
    private String linkedinUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status", nullable = false, length = 20)
    private InstructorStatus status = InstructorStatus.PENDING;

    @Builder.Default
    @OneToMany(mappedBy = "instructor")
    private List<Course> courses = new ArrayList<>();
}
