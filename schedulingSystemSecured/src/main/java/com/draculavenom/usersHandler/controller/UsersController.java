package com.draculavenom.usersHandler.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.security.config.JwtService;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.UserDTO;
import com.draculavenom.usersHandler.dto.UserInputDTO;

@RestController
@RequestMapping("/api/v1/Users")
public class UsersController {
	
	@Autowired private UserRepository repository;
	@Autowired private UsersManager manager;
	@Autowired private JwtService jwtService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('admin:read')")
	public List<UserDTO> getAll(){
		List users = new ArrayList<UserDTO>();
		repository.findAll().stream().forEach(u -> {
			UserDTO user = new UserDTO(u.getId(), u.getEmail(), u.getFirstName() + " " + u.getLastName(), u.getPhoneNumber(), u.getDateOfBirth(), u.getManagedBy(), u.getRole().name());
			users.add(user);
		});
		return users;
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('user:read')")
	public UserInputDTO get(@PathVariable int id) {
		User fullUser = repository.getById(id);
		if(fullUser != null) {
			UserInputDTO user = new UserInputDTO(fullUser.getId(), fullUser.getEmail(), fullUser.getFirstName(), fullUser.getLastName(), "", fullUser.getPhoneNumber(), fullUser.getDateOfBirth(), fullUser.getManagedBy(), fullUser.getRole().name());
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
			UserDTO user = new UserDTO(fullUser.getId(), fullUser.getEmail(), fullUser.getFirstName() + " " + fullUser.getLastName(), fullUser.getPhoneNumber(), fullUser.getDateOfBirth(), fullUser.getManagedBy(), fullUser.getRole().name());
			return user;
		}
		return null;
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	public ResponseEntity<UserInputDTO> create(@RequestBody UserInputDTO user) {
		User fullUser = manager.create(user);
		user.setId(fullUser.getId());
		user.setFirstName(fullUser.getFirstName());
		user.setLastName(fullUser.getLastName());
		user.setEmail(fullUser.getEmail());
		user.setPhoneNumber(fullUser.getPhoneNumber());
		user.setDateOfBirth(fullUser.getDateOfBirth());
		user.setManagedBy(fullUser.getManagedBy());
		user.setRole(fullUser.getRole().name());
		return new ResponseEntity<UserInputDTO>(user, HttpStatusCode.valueOf(200));
	}
	
	@PutMapping
	@PreAuthorize("hasAuthority('admin:update')")
	public ResponseEntity<UserInputDTO> update(@RequestBody UserInputDTO user) {
		User fullUser = manager.update(user);
		user.setId(fullUser.getId());
		user.setFirstName(fullUser.getFirstName());
		user.setLastName(fullUser.getLastName());
		user.setEmail(fullUser.getEmail());
		user.setPhoneNumber(fullUser.getPhoneNumber());
		user.setDateOfBirth(fullUser.getDateOfBirth());
		user.setManagedBy(fullUser.getManagedBy());
		user.setRole(fullUser.getRole().name());
		return new ResponseEntity<UserInputDTO>(user, HttpStatusCode.valueOf(200));
	}
	
	@PutMapping("/self")
	public ResponseEntity<UserInputDTO> updateSelf(@RequestBody UserInputDTO user, @RequestHeader("Authorization") String authorizationHeader) {
		String accessToken = authorizationHeader.replace("Bearer ", "");
		String id = jwtService.extractId(accessToken);
		if(Integer.parseInt(id) != user.getId())
			return new ResponseEntity("You cannot edit a different user from yourself", HttpStatusCode.valueOf(400));
		User fullUser = manager.update(user);
		user.setId(fullUser.getId());
		user.setFirstName(fullUser.getFirstName());
		user.setLastName(fullUser.getLastName());
		user.setEmail(fullUser.getEmail());
		user.setPhoneNumber(fullUser.getPhoneNumber());
		user.setDateOfBirth(fullUser.getDateOfBirth());
		user.setManagedBy(fullUser.getManagedBy());
		user.setRole(fullUser.getRole().name());
		return new ResponseEntity<UserInputDTO>(user, HttpStatusCode.valueOf(200));
	}
}
