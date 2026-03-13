package com.draculavenom.managementModules.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.draculavenom.managementModules.model.ManagementModules;
import com.draculavenom.managementModules.model.ManagementModulesRepository;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;

@Service
public class ManagementModulesService {
    private final ManagementModulesRepository repository;

    public ManagementModulesService(ManagementModulesRepository repository){
        this.repository = repository;
    }

    public ManagementModules getOrCreate(User admin, User manager){
        return repository.findByManagerId(manager.getId()).orElseGet(() -> repository.save(new ManagementModules(admin, manager)));
    }

    public ManagementModules updateModules(
        User admin, User manager,
        boolean whatsappNotification){
        if(!admin.getRole().equals(Role.ADMIN)){
            throw new AccessDeniedException("Only admin can modify modules");
        }

        ManagementModules modules = getOrCreate(admin, manager);

        modules.setWhatsappNotification(whatsappNotification);

        return repository.save(modules);
    }
}
