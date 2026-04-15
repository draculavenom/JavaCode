package com.draculavenom.security.auth;

import com.draculavenom.security.config.JwtService;
import com.draculavenom.security.token.Token;
import com.draculavenom.security.token.TokenRepository;
import com.draculavenom.security.token.TokenType;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.AccountOption;
import com.draculavenom.usersHandler.dto.UserInputDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.server.ResponseStatusException;

import com.draculavenom.schedulingSystem.controller.ManagerOptionsService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final ManagerOptionsService optionsService;

  public AuthenticationResponse register(RegisterRequest request) {
    validateEmailRole(request.getEmail(), request.getRole());
    var user = User.builder()
    	.id(request.getId())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(request.getRole())
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

	public AuthenticationResponse registerUser(UserInputDTO request) {
    validateEmailRole(request.getEmail(), Role.USER);
		User user = User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.phoneNumber(request.getPhoneNumber())
				.dateOfBirth(request.getDateOfBirth())
				.managedBy(request.getManagedBy())
				.role(Role.USER)
				.build();
		var savedUser = repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder()
				.accessToken(jwtToken)
				.refreshToken(refreshToken)
				.build();
	}

  public AuthenticationResponse authenticate(AuthenticationRequest request){
    List<User> users = repository.findAllByEmail(request.getEmail());
    if(users.isEmpty()){
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "USER_NOT_FOUND");
    }
    List<User> validUsers = users.stream()
      .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
      .toList();

    System.out.println("usuarios validos" + validUsers);
    if(validUsers.isEmpty()){
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS");
    }

    if(validUsers.size() == 1){     // Case 1: only one
      User user = validUsers.get(0);
      validateManager(user);
      return buildAuthResponse(user);
    }
    return buildSelectionResponse(validUsers);      // Case 2: multiple + selector
  }

  private void validateManager(User user){
    if(user.getRole() == Role.MANAGER){
      boolean active = optionsService.isManagerActive(user.getId());
      if(!active){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "SUBSCRIPTION_EXPIRED");
      }
    }
  }

  private AuthenticationResponse buildAuthResponse(User user){
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
        .build();
  }

  private AuthenticationResponse buildSelectionResponse(List<User> users){
    List<AccountOption> options = users.stream()
      .map(user -> new AccountOption(
          user.getId(), 
          user.getRole(),
          user.getManagedBy()
        ))
        .toList();
    
    return AuthenticationResponse.builder()
      .requiresSelection(true)
      .accounts(options)
      .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
        List<User> users = this.repository.findAllByEmail(userEmail);
        if(users.isEmpty()) return;
        var user = users.get(0);
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }

  private void validateEmailRole(String email, Role role){
    List<User> existingUsers = repository.findAllByEmail(email);
    boolean roleExists = existingUsers.stream()
      .anyMatch(user -> user.getRole() == role);
    if(roleExists == true){
      System.out.println("ACCOUNT_ALREADY_EXIST_FOR_THIS_ROLE");
      throw new ResponseStatusException(HttpStatus.CONFLICT, "ACCOUNT_ALREADY_EXIST_FOR_THIS_ROLE");
    }

    if(!existingUsers.isEmpty() && role == Role.USER){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "EMAIL_ALREADY_REGISTERED_AS_USER");
    }
  }
}
