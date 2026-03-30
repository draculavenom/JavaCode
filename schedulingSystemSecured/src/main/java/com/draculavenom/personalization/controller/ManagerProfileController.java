package com.draculavenom.personalization.controller;

import com.draculavenom.personalization.dto.ManagerProfileRequest; // <--- Asegúrate que sea este
import com.draculavenom.personalization.dto.ManagerProfileResponse;
import com.draculavenom.personalization.service.ManagerProfileService;
import com.draculavenom.security.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/personalization")
@RequiredArgsConstructor
public class ManagerProfileController {

    private final ManagerProfileService service;

    @GetMapping("/my-profile")
    @PreAuthorize("hasAuthority('manager:read')") 
    public ResponseEntity<ManagerProfileResponse> getMyProfile(@AuthenticationPrincipal User manager) {
        return ResponseEntity.ok(service.getProfileByManager(manager));
    }

    @PutMapping(value = "/my-profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@PreAuthorize("hasAuthority('manager:update')")
public ResponseEntity<String> updateMyProfile(
        @AuthenticationPrincipal User manager,
        @RequestParam("introduction") String introduction,
        @RequestParam(value = "logoFile", required = false) MultipartFile logoFile) {
    if (logoFile != null && logoFile.getSize() > 5 * 1024 * 1024) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("The file is very large (max 5MB)");
    }

    try {
        service.updateProfileWithFile(manager, introduction, logoFile);
        return ResponseEntity.ok("Perfil successfully updated");
    } catch (IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving the file");
    }
}

    @GetMapping("/public/{managerId}")
public ResponseEntity<ManagerProfileResponse> getPublicProfile(@PathVariable Integer managerId) {
    return ResponseEntity.ok(service.getProfileByManagerId(managerId));
}
}