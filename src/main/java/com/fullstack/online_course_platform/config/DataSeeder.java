package com.fullstack.online_course_platform.config;

import com.fullstack.online_course_platform.common.enums.RoleType;
import com.fullstack.online_course_platform.common.enums.UserStatus;
import com.fullstack.online_course_platform.model.Role;
import com.fullstack.online_course_platform.model.User;
import com.fullstack.online_course_platform.repository.RoleRepository;
import com.fullstack.online_course_platform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j(topic = "DATA-SEEDER")
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedAdminUser();
    }

    private void seedRoles() {
        for (RoleType roleType : RoleType.values()) {
            if (!roleRepository.existsByName(roleType.name())) {
                Role role = Role.builder()
                        .name(roleType.name())
                        .description(roleType.name() + " role")
                        .build();
                roleRepository.save(role);
                log.info("Role seeded: {}", roleType.name());
            }
        }
    }

    private void seedAdminUser() {
        if (!userRepository.existsByEmail(adminEmail)) {
            Role adminRole = roleRepository.findByName(RoleType.ADMIN.name())
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name(RoleType.ADMIN.name())
                                    .description("ADMIN role")
                                    .build()
                    ));

            User adminUser = User.builder()
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .role(adminRole)
                    .status(UserStatus.ACTIVE)
                    .build();

            userRepository.save(adminUser);
            log.info("Default Admin account created: email={}", adminEmail);
        }
    }
}
