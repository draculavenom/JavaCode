package com.draculavenom.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static com.draculavenom.security.user.Permission.ADMIN_CREATE;
import static com.draculavenom.security.user.Permission.ADMIN_DELETE;
import static com.draculavenom.security.user.Permission.ADMIN_READ;
import static com.draculavenom.security.user.Permission.ADMIN_UPDATE;
import static com.draculavenom.security.user.Permission.MANAGER_CREATE;
import static com.draculavenom.security.user.Permission.MANAGER_DELETE;
import static com.draculavenom.security.user.Permission.MANAGER_READ;
import static com.draculavenom.security.user.Permission.MANAGER_UPDATE;
import static com.draculavenom.security.user.Permission.USER_CREATE;
import static com.draculavenom.security.user.Permission.USER_DELETE;
import static com.draculavenom.security.user.Permission.USER_READ;
import static com.draculavenom.security.user.Permission.USER_UPDATE;
import static com.draculavenom.security.user.Role.ADMIN;
import static com.draculavenom.security.user.Role.USER;
import static com.draculavenom.security.user.Role.MANAGER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

  private final JwtAuthenticationFilter jwtAuthFilter;
  private final AuthenticationProvider authenticationProvider;
  private final LogoutHandler logoutHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf()
        .disable()
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests()
        .requestMatchers(
                "/api/v1/auth/**",
                "/api/v1/Manager/select",
                "/v2/api-docs",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources",
                "/swagger-resources/**",
                "/configuration/ui",
                "/configuration/security",
                "/swagger-ui/**",
                "/webjars/**",
                "/swagger-ui.html"
        )
          .permitAll()


        .requestMatchers("/api/v1/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())


        .requestMatchers(GET, "/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
        .requestMatchers(POST, "/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
        .requestMatchers(PUT, "/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
        .requestMatchers(DELETE, "/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())


        .requestMatchers("/api/v1/admin/**").hasRole(ADMIN.name())

        .requestMatchers(GET, "/api/v1/admin/**").hasAuthority(ADMIN_READ.name())
        .requestMatchers(POST, "/api/v1/admin/**").hasAuthority(ADMIN_CREATE.name())
        .requestMatchers(PUT, "/api/v1/admin/**").hasAuthority(ADMIN_UPDATE.name())
        .requestMatchers(DELETE, "/api/v1/admin/**").hasAuthority(ADMIN_DELETE.name())
        
//        .requestMatchers("/api/v1/Appointments/**").hasAnyRole(ADMIN.name(), MANAGER.name(), USER.name())
//
//        .requestMatchers(GET, "/api/v1/Appointments/**").hasAuthority(USER_READ.name())
//        .requestMatchers(POST, "/api/v1/Appointments/**").hasAuthority(USER_CREATE.name())
//        .requestMatchers(PUT, "/api/v1/Appointments/**").hasAuthority(USER_UPDATE.name())
//        .requestMatchers(DELETE, "/api/v1/Appointments/**").hasAuthority(USER_DELETE.name())


        .anyRequest()
          .authenticated()
        .and()
          .sessionManagement()
          .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .logout()
        .logoutUrl("/api/v1/auth/logout")
        .addLogoutHandler(logoutHandler)
        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
    ;

    return http.build();
  }
  
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
	    configuration.setAllowedMethods(Arrays.asList("*"));
	    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "access-control-allow-origin"));
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}
}
