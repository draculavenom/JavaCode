package com.draculavenom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.draculavenom.company.CompanyName;
import com.draculavenom.company.CompanyNameRepository;
import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;
import com.draculavenom.security.auth.AuthenticationService;
import com.draculavenom.security.auth.RegisterRequest;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import static com.draculavenom.security.user.Role.ADMIN;
import static com.draculavenom.security.user.Role.MANAGER;

import java.time.LocalDate;

@SpringBootApplication
@EnableScheduling
public class SecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}
	
	//DV Notes: For the first time running the application, there won't be any admin user to start creating everything else.
	//There is a method AuthenticationController.registerAdmin that is commented, you need to allow it to create the first user
	
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
			AuthenticationService service,
			UserRepository userRepository,
            CompanyNameRepository companyRepository,
            ManagerOptionsRepository managerOptionsRepository
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstName("Admin")
					.lastName("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var demoManager = RegisterRequest.builder()
					.firstName("Demo")
					.lastName("Manager")
					.email("demo@demo.com")
					.password("demopassword")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(demoManager).getAccessToken());
			
			User demoUser = userRepository.findByEmail("demo@demo.com")
			        .orElseThrow(() -> new RuntimeException("User not found"));
			User adminUser = userRepository.findByEmail("admin@mail.com")
			        .orElseThrow(() -> new RuntimeException("User not found"));
			
			CompanyName myCompany = new CompanyName("Demo company", demoUser);
			
			companyRepository.save(myCompany);
			
			ManagerOptions managerOptions = new ManagerOptions();
			managerOptions.setManagerId(demoUser.getId());
			managerOptions.setActiveDate(LocalDate.now().plusYears(1));
			managerOptions.setUserId(adminUser.getId());
			managerOptions.setAmmountPaid(0.0);
			managerOptions.setComments("enable of the Demo Manager for a year");
			
			managerOptionsRepository.save(managerOptions);

		};
	}*/
}
