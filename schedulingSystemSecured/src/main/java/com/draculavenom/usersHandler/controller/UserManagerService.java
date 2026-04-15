package com.draculavenom.usersHandler.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.draculavenom.notification.service.NotificationService;
import com.draculavenom.security.auth.AuthenticationService;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.UserInputDTO;

@Service
public class UserManagerService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TemporaryPasswordService temporaryPasswordService;
    private final NotificationService notificationService;
    

    public UserManagerService(UserRepository repository, PasswordEncoder passwordEncoder, TemporaryPasswordService temporaryPasswordService, NotificationService notificationService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.temporaryPasswordService = temporaryPasswordService;
        this.notificationService = notificationService;
    }

    public User create(UserInputDTO user) {
        validateEmailRole(user.getEmail(), Role.valueOf(user.getRole()));
        String temporaryPassword = temporaryPasswordService.generateLoginPassword();

        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setDateOfBirth(user.getDateOfBirth());
        newUser.setManagedBy(user.getManagedBy());
        newUser.setRole(Role.valueOf(user.getRole()));

        newUser.setPassword(passwordEncoder.encode(temporaryPassword));
        newUser.setPasswordChange(false);

        User savedUser = repository.save(newUser);

        notificationService.notifyIncorporationCredentials(savedUser, temporaryPassword);

        return savedUser;

    }

    private void validateEmailRole(String email, Role role){
    List<User> existingUsers = repository.findAllByEmail(email);
    boolean roleExists = existingUsers.stream()
      .anyMatch(user -> user.getRole() == role);

    if(roleExists){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "ACCOUNT_ALREADY_EXIST_FOR_THIS_ROLE");
    }
  }

}
