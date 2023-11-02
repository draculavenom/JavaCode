package com.draculavenom.usersHandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.UserInputDTO;

@Controller
public class UsersManager {
	
	@Autowired private UserRepository repository;
	@Autowired private PasswordEncoder passwordEncoder;
	
	public User create(UserInputDTO user) {
		User newUser = new User();
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setPhoneNumber(user.getPhoneNumber());
		newUser.setDateOfBirth(user.getDateOfBirth());
		newUser.setManagedBy(user.getManagedBy());
		newUser.setRole(Role.valueOf(user.getRole()));
		return repository.save(newUser);
	}
	
	public User update(UserInputDTO user) {
		User newUser = repository.getById(user.getId());
		newUser.setFirstName(user.getFirstName());
		newUser.setLastName(user.getLastName());
		newUser.setEmail(user.getEmail());
		newUser.setPhoneNumber(user.getPhoneNumber());
		newUser.setDateOfBirth(user.getDateOfBirth());
		return repository.save(newUser);
	}
}
