package com.draculavenom.schedulingSystem.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.draculavenom.schedulingSystem.dto.WorkScheduleRequest;
import com.draculavenom.schedulingSystem.dto.WorkShiftResponse;

@RestController
@RequestMapping("/api/v1/managers")
public class WorkScheduleController {
    @Autowired private WorkScheduleService service;

    @PostMapping("/{managerId}/work-shift")
    @PreAuthorize("hasAnyAuthority('admin:create','manager:create')")
    public ResponseEntity<Void> crate(@PathVariable Integer managerId, @RequestBody WorkScheduleRequest request, Authentication authentication) {
        service.createOrReplaceSchedule(managerId, request, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{managerId}/work-shift")
    @PreAuthorize("hasAnyAuthority('admin:read','manager:read')")
    public ResponseEntity<List<WorkShiftResponse>> get(@PathVariable Integer managerId, Authentication authentication) {
        return ResponseEntity.ok(service.getSchedule(managerId, authentication));
    }

    @PutMapping("/{managerId}/work-shift")
    @PreAuthorize("hasAnyAuthority('admin:update','manager:update')")
    public ResponseEntity<Void> update(@PathVariable Integer managerId, @RequestBody WorkScheduleRequest request, Authentication authentication) {
        service.createOrReplaceSchedule(managerId, request, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{managerId}/available-slots")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<List<LocalTime>> getAvailableSlots(@PathVariable Integer managerId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return ResponseEntity.ok(service.getAvailableSlots(managerId, localDate));
    }

}
