package com.draculavenom.schedulingSystem.security.auth;

import com.draculavenom.schedulingSystem.security.config.JwtService;
import com.draculavenom.schedulingSystem.security.token.Token;
import com.draculavenom.schedulingSystem.security.token.TokenRepository;
import com.draculavenom.schedulingSystem.security.token.TokenType;
import com.draculavenom.schedulingSystem.security.user.Role;
import com.draculavenom.schedulingSystem.security.user.User;
import com.draculavenom.schedulingSystem.security.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
	  if(repository.findByEmail(request.getEmail()).orElse(null) == null) {
	    var user = User.builder()
	        .firstname(request.getFirstname())
	        .lastname(request.getLastname())
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
	  }else {
		  return AuthenticationResponse.builder()
				  .accessToken("User is already registered")
		            .refreshToken("User is already registered")
		        .build();
	  }
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
	  System.out.println(request);
	
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
	  
	  System.out.println("Here");
    var user = new User();
	  System.out.println(user);
    try {
    	user = repository.findByEmail(request.getEmail())
    	        .orElse(null);
    }catch(Exception e) {
    	System.out.println("inside catch");
    	e.printStackTrace();
    }
	  System.out.println(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
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
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
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
}
