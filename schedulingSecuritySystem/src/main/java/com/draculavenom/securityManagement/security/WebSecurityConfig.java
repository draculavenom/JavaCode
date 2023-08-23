package com.draculavenom.securityManagement.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JWTAuthEntryPoint authEntryPoint;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	//@Order(1)
	public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
		System.out.println(http);
		http.csrf().disable()
			.securityMatcher("/users/**", "/person/**")
			.authorizeHttpRequests((requests) -> requests
				.anyRequest().hasAnyRole("USER")
			)
			//.authorizeHttpRequests((requests) -> requests
				//.requestMatchers("/", "/error", "/auth/**").permitAll()
				//.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
				//.requestMatchers("/users").hasAnyRole("USER")
				//.requestMatchers("/person/**").permitAll()
				//.anyRequest().authenticated()
			//)
			.logout((logout) -> logout.permitAll())
			.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/error"))
			//.authenticationEntryPoint(authEntryPoint)
			//.and()
			.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			;
		
		http.addFilterBefore(JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		//http.headers().frameOptions().disable();

		return http.build();
	}
	
	@Bean
	@Order(2)
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		System.out.println(http);
		http.csrf().disable()
			.securityMatcher("/", "/error", "/auth/**", "/h2-console/**")
			.authorizeHttpRequests((requests) -> requests
				.anyRequest().permitAll()
			)
			//.logout((logout) -> logout.permitAll())
			//.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/error"))
			//.authenticationEntryPoint(authEntryPoint)
			//.and()
			//.sessionManagement()
	        //.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			;
		return http.build();
	}
	
	@Bean
	public SecurityFilterChain authenticatedFilterChain(HttpSecurity http) throws Exception {
		System.out.println(http);
		http.csrf().disable()
			.authorizeHttpRequests((requests) -> requests
				.anyRequest().authenticated()
			)
			;
	
		return http.build();
	}
	
//	@Bean
//	public DataSource dataSource() {
//		return new EmbeddedDatabaseBuilder()
//		.setType(EmbeddedDatabaseType.H2)
////		.addScript(JdbcDaoImpl.DEFAULT_USER_SCHEMA_DDL_LOCATION)
//		.build();
//	}

//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//			 User
////			 	.withDefaultPasswordEncoder()
//			 	.builder()
//				.username("user")
//				.password(passwordEncoder().encode("password"))
//				.roles("USER")
//				.build();
//		JdbcUserDetailsManager users = new JdbcUserDetailsManager(jdbcTemplate.getDataSource());
//	    users.createUser(user);
//	    return users;
//
////		return new InMemoryUserDetailsManager(user);
//	}

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
//	    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Bean
	public JWTAuthenticationFilter JWTAuthenticationFilter() {
		return new JWTAuthenticationFilter();
	}
}
