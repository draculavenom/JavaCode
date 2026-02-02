package com.draculavenom.security.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


import com.draculavenom.security.user.Role;
import com.draculavenom.usersHandler.dto.UserDTO;
import com.draculavenom.usersHandler.dto.UserInputDTO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  /*@PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(
      @RequestBody RegisterRequest request
  ) {
    return ResponseEntity.ok(service.register(request));
  }*/
  
	@PostMapping("/registerUser")
	public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody UserInputDTO request) {
		return ResponseEntity.ok(service.registerUser(request));
	}
  
//  @PostMapping("/registerAdmin")//This method allows me to create the admin user to start using the app. if there is no admin user in the database, nothing will work.
//	public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
//  		if(request.getRole() == Role.ADMIN && request.getEmail().equals("admin@admin.com"))//It will only work if the role is ADMIN and the email is: admin@admin.com
//  			return ResponseEntity.ok(service.register(request));
//  		return null;
//	}
  
  @PostMapping("/authenticate")
  public ResponseEntity<?> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    try{
      return ResponseEntity.ok(service.authenticate(request));
    } catch (ResponseStatusException ex){
      Map<String, String> errorBody = new HashMap<>();
      errorBody.put("message", ex.getReason());
      return ResponseEntity.status(ex.getStatusCode()).body(errorBody);
    }
    catch (Exception ex){
      Map<String, String> errorBody = new HashMap<>();
      errorBody.put("message", "INVALID_CREDENTIALS");
      return ResponseEntity.status(401).body(errorBody);
  }
}

  @PostMapping("/refresh-token")
  public void refreshToken(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }


}
