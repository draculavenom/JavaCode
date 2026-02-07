package com.draculavenom.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.notification.dto.NotificationSettingsRequest;
import com.draculavenom.notification.dto.NotificationSettingsResponse;
import com.draculavenom.notification.model.NotificationSettings;
import com.draculavenom.security.config.JwtService;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@RestController
@RequestMapping("/api/v1/notification-settings")
public class NotificationSettingsController {
    @Autowired private NotificationSettingsService service; 
    @Autowired private JwtService jwtService;
    @Autowired private UserRepository repository;

    @GetMapping("/self")
    @PreAuthorize("hasAuthority('manager:read')")
    public ResponseEntity<NotificationSettingsResponse> getSelf(@RequestHeader("Authorization") String authorizationHeader){
        User manager = getUserFromToken(authorizationHeader);
        NotificationSettings settings = service.getOrCreate(manager);
        return new ResponseEntity<>(new NotificationSettingsResponse(settings), HttpStatus.OK);
    }

    @PutMapping("/self")
    @PreAuthorize("hasAuthority('manager:update')")
    public ResponseEntity<NotificationSettingsResponse> updateSelf(@RequestBody NotificationSettingsRequest request, @RequestHeader("Authorization") String authorizationHeader){
        User manager = getUserFromToken(authorizationHeader);
        NotificationSettings updated = service.updateSettings(
            manager, request.isEmailEnabled(), request.isAppointmentCreated(), request.isPaymentRunsOut(), request.isAppointmentStatusChanges()
        );

        return new ResponseEntity<>(new NotificationSettingsResponse(updated), HttpStatus.OK);
    }

    private User getUserFromToken(String authorizationHeader){
        String accessToken = authorizationHeader.replace("Bearer","");
        Integer userId = Integer.parseInt(jwtService.extractId(accessToken));
        return repository.getById(userId);
    }

}
