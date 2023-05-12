package com.draculavenom.securityManagement.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.securityManagement.model.Users;
import com.draculavenom.securityManagement.model.UsersRepository;

@RestController()
public class UserController {
	
	@Autowired
	private UsersRepository usersRepository;
	
	@GetMapping("/users")
	public List<Users> getUsers() {
		return usersRepository.findAll();
	}
	
	@GetMapping("/users/{id}")
	public Users getUser(@PathVariable long id) {
		return usersRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/users")
	public Users createUsers(@RequestBody Users user) {
		return usersRepository.save(user);
	}
	
	@PutMapping("/users/{id}")
	public Users updateUsers(@PathVariable long id, @RequestBody Users user) {
		return usersRepository.findById(id)
				.map((newUser) -> usersRepository.save(new Users(user.getName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getEmail(), LocalDate.now())))
				.orElseGet(() -> usersRepository.save(user));
	}
	
	@DeleteMapping("/users/{id}")
	public void deleteUsers(@PathVariable long id) {
		usersRepository.deleteById(id);
	}
}
