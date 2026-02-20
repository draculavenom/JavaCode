package com.draculavenom.usersHandler.controller;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;

import com.draculavenom.schedulingSystem.utilities.EmailService;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.UserInputDTO;

@Controller
public class UsersManager {
	
	@Autowired private UserRepository repository;
	@Autowired private PasswordEncoder passwordEncoder;
	@Autowired private EmailService emailService;
	
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
	
	public boolean resetPassword(int id) {
		User user = repository.getById(id);
		String newPassword = generateRandomPassword();
		try {
			emailService.sendSimpleMessage(user.getEmail(), "Please change your password"
					, "Your manager restarted your password" 
					+ "\nThe new password is: " + newPassword 
					+ "\nPlease click this link to change the password to a different one: "
					+ "\nhttp://localhost:4200/resetPassword?email=" + user.getEmail() + "&password=" + newPassword
					);
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setPasswordChange(true);;
			repository.save(user);
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean passwordChange(User user) {
		String password = user.getPassword();
		user = repository.getById(user.getId());
		user.setPassword(passwordEncoder.encode(password));
		user.setPasswordChange(false);
		repository.save(user);
		return true;
	}
	
	private static String generateRandomPassword() {
		int length = 10;
        String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789().@-";
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(symbols.length());
            password.append(symbols.charAt(index));
        }

        return password.toString();
    }
}
