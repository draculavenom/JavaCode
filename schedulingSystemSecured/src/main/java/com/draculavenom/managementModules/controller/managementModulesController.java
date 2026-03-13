package com.draculavenom.managementModules.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.managementModules.dto.ManagementModulesRequest;
import com.draculavenom.managementModules.dto.ManagementModulesResponse;
import com.draculavenom.managementModules.model.ManagementModules;
import com.draculavenom.security.config.JwtService;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@RestController
@RequestMapping("/api/v1/management-modules")
public class managementModulesController {
    
    @Autowired private ManagementModulesService service;
    @Autowired private JwtService jwtService;
    @Autowired private UserRepository repository;

    @GetMapping("/{managerId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ManagementModulesResponse> getModules(@PathVariable Integer managerId, @RequestHeader("Authorization") String authorizationHeader){
        User admin = getUserFromToken(authorizationHeader);
        User manager = repository.getById(managerId);
        ManagementModules modules = service.getOrCreate(admin, manager);
        return new ResponseEntity<>(new ManagementModulesResponse(modules), HttpStatus.OK);
    }

    @PutMapping("/{managerId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ManagementModulesResponse> updateModules(@PathVariable Integer managerId, @RequestBody ManagementModulesRequest request, @RequestHeader("Authorization") String authorizationHeader){
        User admin = getUserFromToken(authorizationHeader);
        User manager = repository.getById(managerId);
        ManagementModules updated = service.updateModules(admin, manager, request.isWhatsappNotification());
        return ResponseEntity.ok(new ManagementModulesResponse(updated));
    }

    private User getUserFromToken(String authorizationHeader){
        String accessToken = authorizationHeader.replace("Bearer","");
        Integer userId = Integer.parseInt(jwtService.extractId(accessToken));
        return repository.getById(userId);
    }
}
