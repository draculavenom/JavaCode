package com.draculavenom.schedulingSystem; 

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SchedulingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchedulingSystemApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/person/**").allowedOrigins("http://localhost:4200");
			}
		};
	}

}
/*
 * database credentials:
 * root
 * QWErty4563
 */