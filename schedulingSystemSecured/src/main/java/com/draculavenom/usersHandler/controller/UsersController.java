package com.draculavenom.usersHandler.controller;

import static org.junit.jupiter.api.DynamicTest.stream;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.draculavenom.security.config.JwtService;
import com.draculavenom.security.user.Role;
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
	@Autowired private UserManagerService managerService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('admin:read')")
	public List<UserDTO> getAll(){
		List<UserDTO> users = new ArrayList<UserDTO>();
		repository.findAll().stream().forEach(u -> {
			UserDTO user = new UserDTO(u.getId(), u.getEmail(), u.getFirstName() + " " + u.getLastName(), u.getPhoneNumber(), u.getDateOfBirth(), u.getManagedBy(), u.getRole().name(), u.getPasswordChange());
			users.add(user);
		});
		return users;
	}
	
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('user:read')")
	public UserInputDTO get(@PathVariable int id) {
		User fullUser = repository.getById(id);
		if(fullUser != null) {
			UserInputDTO user = new UserInputDTO(fullUser.getId(), fullUser.getEmail(), fullUser.getFirstName(), fullUser.getLastName(), "", fullUser.getPhoneNumber(), fullUser.getDateOfBirth(), (int) (fullUser.getManagedBy() != null ? fullUser.getManagedBy() : 0), fullUser.getRole().name());
			return user;
		}
		return null;
	}
	
	// Method to obtain information related to the email address, if there are multiple accounts with that email address, it displays them all
	@GetMapping("/byEmail/{email}")		
	@PreAuthorize("hasAuthority('user:read')")
	public List<UserDTO> getByEmail(@PathVariable String email) {
		List<User> users = repository.findAllByEmail(email);
		if(users.isEmpty()) {
			throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "USERS_NOT_FOUND");
		}
		
		return users.stream()
			.map(user -> new UserDTO(
				user.getId(), user.getEmail(), user.getFirstName() + " " + user.getLastName(), user.getPhoneNumber(), user.getDateOfBirth(), user.getManagedBy(), user.getRole().name(), user.getPasswordChange()
			))
			.toList();
	}

	// Method to obtain information about that user
	@GetMapping("/byEmail/{email}/role/{role}")			
	@PreAuthorize("hasAuthority('user:read')")
	public UserDTO getByEmailAndRole(@PathVariable String email, @PathVariable Role role){
		List<User> users = repository.findAllByEmail(email);
		if(users.isEmpty()){
			throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "USERS_NOT_FOUND");
		}

		User user;
		if(users.size() == 1){
			user = users.get(0);
		}else{
			user = users.stream()
			.filter(u -> u.getRole() == role)
			.findFirst()
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND_WITH_ROLE"));

		}		
		return new UserDTO(
			user.getId(), user.getEmail(), user.getFirstName() + " " + user.getLastName(), user.getPhoneNumber(), user.getDateOfBirth(), user.getManagedBy(), user.getRole().name(), user.getPasswordChange()
		);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('admin:create')")
	public ResponseEntity<UserInputDTO> create(@RequestBody UserInputDTO user) {
		User fullUser = managerService.create(user);
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
	
	@GetMapping("/resetPassword/{id}")
	public ResponseEntity<Boolean> resetPassword(@PathVariable int id){
		return new ResponseEntity<Boolean>(manager.resetPassword(id), HttpStatusCode.valueOf(200));
	}
	
	@PutMapping("/passwordChange")
	public ResponseEntity<Boolean> passwordChange(@RequestBody User user){
		return new ResponseEntity<Boolean>(manager.passwordChange(user), HttpStatusCode.valueOf(200));
	}
}
