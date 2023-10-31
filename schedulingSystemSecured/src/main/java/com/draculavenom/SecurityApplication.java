package com.draculavenom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import org.springframework.context.annotation.Bean;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//import com.draculavenom.security.auth.AuthenticationService;
//import com.draculavenom.security.auth.RegisterRequest;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import static com.draculavenom.security.user.Role.ADMIN;
//import static com.draculavenom.security.user.Role.MANAGER;

@SpringBootApplication
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
	
	/*@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				//registry.addMapping("/person/**").allowedOrigins("http://localhost:4200").allowedHeaders("*").allowedMethods("*");
				registry.addMapping("/api/v1/**").allowedOrigins("http://localhost:4200").allowedHeaders("*").allowedMethods("*").allowCredentials(true);
			}
		};
	}*/

	/*@Bean
	public CommandLineRunner commandLineRunner(//This bean can create by default two users with admin and manager roles.
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("Admin")
					.lastname("Admin")
					.email("manager@mail.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());

		};
	}*/
}
