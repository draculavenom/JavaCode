package com.draculavenom.personalization.service;

import com.draculavenom.personalization.dto.ManagerProfileRequest;
import com.draculavenom.personalization.dto.ManagerProfileResponse;
import com.draculavenom.personalization.model.ManagerProfile;
import com.draculavenom.personalization.service.FileStorageService;
import com.draculavenom.personalization.repository.ManagerProfileRepository;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;



@Service
@RequiredArgsConstructor
public class ManagerProfileService {

    private final ManagerProfileRepository repository;
    private final UserRepository userRepository;
    @Autowired private FileStorageService fileStorageService;

    public ManagerProfileResponse getProfileByManagerId(Integer managerId) {
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        ManagerProfile profile = repository.findByManagerId(managerId)
                .orElse(ManagerProfile.builder()
                        .manager(manager)
                        .build());
        return ManagerProfileResponse.builder()
                .id(profile.getId())
                .introduction(profile.getIntroduction())
                .logo(profile.getLogo())
                .managerFullName(manager.getFirstName() + " " + manager.getLastName()) 
                .build();
    }

  

@Transactional
    public void updateProfileWithFile(User user, String introduction, MultipartFile logoFile) throws IOException {
        ManagerProfile profile = repository.findByManagerId(user.getId())
        .orElseGet(() -> ManagerProfile.builder()
                .manager(user) 
                .logo("SYSTEM_DEFAULT_CREAR_LOGO")
                .introduction("")
                .build());

        profile.setIntroduction(introduction);

        if (logoFile != null && !logoFile.isEmpty()) {
            String oldLogo = profile.getLogo(); 
            String fileName = fileStorageService.saveFile(logoFile, oldLogo);
            profile.setLogo(fileName);
        }

        repository.save(profile);
    }
public ManagerProfileResponse getProfileByManager(User manager) {
    ManagerProfile profile = repository.findByManagerId(manager.getId())
            .orElse(ManagerProfile.builder()
                    .manager(manager)
                    .build());
    return ManagerProfileResponse.builder()
            .id(profile.getId())
            .introduction(profile.getIntroduction())
            .logo(profile.getLogo())
            .managerFullName(manager.getFirstName() + " " + manager.getLastName()) 
            .build();
}
}