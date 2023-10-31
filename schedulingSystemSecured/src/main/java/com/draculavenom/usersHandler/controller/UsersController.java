package com.draculavenom.usersHandler.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.UserDTO;

@RestController
@RequestMapping("/api/v1/Users")
//@PreAuthorize("hasRole('MANAGER')")
public class UsersController {
	
	@Autowired
	private UserRepository repository;
	
	@GetMapping
	@PreAuthorize("hasAuthority(manager:read)")
	public List<UserDTO> getAll(){
		List users = new ArrayList<UserDTO>();
		repository.findAll().stream().forEach(u -> {
			UserDTO user = new UserDTO(u.getId(), u.getEmail(), u.getFirstname() + " " + u.getLastname(), u.getPhoneNumber(), u.getDateOfBirth(), u.getManagedBy());
			users.add(user);
		});
		return users;
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('user:read')")
	public UserDTO get(@PathVariable int id) {
		User fullUser = repository.getById(id);
		if(fullUser != null) {
			UserDTO user = new UserDTO(fullUser.getId(), fullUser.getEmail(), fullUser.getFirstname() + " " + fullUser.getLastname(), fullUser.getPhoneNumber(), fullUser.getDateOfBirth(), fullUser.getManagedBy());
			return user;
		}
		return null;
	}
	
	@GetMapping("/byEmail/{email}")
	@PreAuthorize("hasAuthority('user:read')")
	public UserDTO getByEmail(@PathVariable String email) {
		Optional<User> optionalUser = repository.findByEmail(email);
		if(!optionalUser.isEmpty()) {
			User fullUser = optionalUser.get();
			UserDTO user = new UserDTO(fullUser.getId(), fullUser.getEmail(), fullUser.getFirstname() + " " + fullUser.getLastname(), fullUser.getPhoneNumber(), fullUser.getDateOfBirth(), fullUser.getManagedBy());
			return user;
		}
		return null;
	}
	
	
}
