package com.draculavenom.securityManagement.controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.securityManagement.dto.AuthResponseDto;
import com.draculavenom.securityManagement.dto.UserDto;
import com.draculavenom.securityManagement.model.Roles;
import com.draculavenom.securityManagement.model.RolesRepository;
import com.draculavenom.securityManagement.model.Users;
import com.draculavenom.securityManagement.model.UsersRepository;
import com.draculavenom.securityManagement.security.JWTGenerator;

@RestController
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JWTGenerator tokenGenerator;
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody UserDto user) {
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		Authentication authentication = authenticationManager.authenticate(token);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String tokenString = tokenGenerator.generateToken(authentication);
		
		return new ResponseEntity<>(new AuthResponseDto(tokenString), HttpStatus.OK);
	}
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserDto user){
		if (usersRepository.existsByUsername(user.getUsername())) {
            return new ResponseEntity<>("Username is taken!", HttpStatus.BAD_REQUEST);
        }

        Users newUser = new Users();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        Roles roles = verifyUserRoleExist();
        newUser.setRoles(Collections.singletonList(roles));

        usersRepository.save(newUser);

        return new ResponseEntity<>("User registered success!", HttpStatus.OK);
	}
	
	@PostMapping("/test")
	public ResponseEntity<String> test(@RequestBody UserDto user){
		return new ResponseEntity<>("User registered success!", HttpStatus.OK);
	}
	
	private Roles verifyUserRoleExist() {
		if (rolesRepository.existsByName("USER"))
			return rolesRepository.findByName("USER").get();
		else {
			Roles userRole = new Roles("USER");
			return rolesRepository.save(userRole);
		}
	}
}
