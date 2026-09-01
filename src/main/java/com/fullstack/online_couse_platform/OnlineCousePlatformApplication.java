package com.fullstack.online_couse_platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OnlineCousePlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineCousePlatformApplication.class, args);
	}

}
