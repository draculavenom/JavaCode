package com.draculavenom.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.company.dto.CompanyNumberRequest;
import com.draculavenom.company.dto.CompanyNumberResponse;

@RestController
@RequestMapping("/api/v1/companyNumber")
public class CompanyNumberController {

    @Autowired private CompanyNumberService service;
    
    @GetMapping("/{managerId}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<CompanyNumberResponse> get(@PathVariable Integer managerId){
        return ResponseEntity.ok(service.get(managerId));
    }

    @PostMapping("/{managerId}")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<Void> create(@PathVariable Integer managerId, @RequestBody CompanyNumberRequest request){
        service.createOrUpdate(managerId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{managerId}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<Void> update(@PathVariable Integer managerId, @RequestBody CompanyNumberRequest request){
        service.createOrUpdate(managerId, request);
        return ResponseEntity.ok().build();
    }
    
}
