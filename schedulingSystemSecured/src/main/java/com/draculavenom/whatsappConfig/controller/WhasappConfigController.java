package com.draculavenom.whatsappConfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.whatsappConfig.dto.WhatsappConfigRequest;
import com.draculavenom.whatsappConfig.dto.WhatsappConfigResponse;
import com.draculavenom.whatsappConfig.service.WhatsappConfigService;

@RestController
@RequestMapping("/api/v1/whatsapp-config")
public class WhasappConfigController {
    
    @Autowired private WhatsappConfigService service;
    
    @PostMapping("/{managerId}")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Void> create(@PathVariable Integer managerId, @RequestBody WhatsappConfigRequest request){
        service.CreateOrUpdate(managerId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{managerId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<WhatsappConfigResponse> get(@PathVariable Integer managerId){
        return ResponseEntity.ok(service.getByManager(managerId));
    }

    @PutMapping("/{managerId}/toggle")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Void> toogle(@PathVariable Integer managerId, @RequestParam boolean active){
        service.toggleActive(managerId, active);
        return ResponseEntity.ok().build();
    }
}
