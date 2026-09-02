package com.fullstack.online_course_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineCoursePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineCoursePlatformApplication.class, args);
	}

}
