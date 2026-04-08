package com.draculavenom.usersHandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.draculavenom.company.CompanyName;
import com.draculavenom.company.CompanyNameRepository;
import com.draculavenom.notification.service.NotificationService;
import com.draculavenom.security.auth.AuthenticationService;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;
import com.draculavenom.usersHandler.dto.UserInputDTO;

@Service
public class UserManagerService {
    
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TemporaryPasswordService temporaryPasswordService;
    private final NotificationService notificationService;
    @Autowired private CompanyNameRepository companyRepository;

    public UserManagerService(UserRepository repository, PasswordEncoder passwordEncoder, TemporaryPasswordService temporaryPasswordService, NotificationService notificationService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.temporaryPasswordService = temporaryPasswordService;
        this.notificationService = notificationService;
    }

    public User create(UserInputDTO user){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        Role requestedRole = Role.valueOf(user.getRole());
        switch(currentUser.getRole()){
            case ADMIN:
                if(requestedRole == Role.OWNER){
                    return createOwner(user, currentUser);
                }
                break;

            case OWNER:
                if(requestedRole == Role.MANAGER){
                    return createManager(user, currentUser);
                }
                break;

            case USER:
                if(requestedRole == Role.USER){
                    return createUser(user);
                }
                break;

            default:
                throw new RuntimeException("Invalid role");

        }
        throw new RuntimeException("Unauthorized");
    }

    private User createOwner(UserInputDTO user, User admin){
        User newUser = buildBaseUser(user);
        
        newUser.setRole(Role.OWNER);
        newUser.setManagedBy(admin.getId());
        CompanyName company = new CompanyName();
        company.setNameCompany(user.getCompanyName());
        company.setAdmin(admin);
        company.setMaxManager(user.getMaxManager());
        company = companyRepository.save(company);
        newUser.setCompany(company);

        return repository.save(newUser);
    }

    private User createManager(UserInputDTO user, User owner){
        CompanyName company = companyRepository.findById(owner.getCompany().getId())
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        long currentManagers = repository.countByCompanyAndRole(company, Role.MANAGER);
        if(company.getMaxManager() != null && currentManagers >= company.getMaxManager()){
            throw new RuntimeException("You have reached the manager limit for your plan.");
        }

        User newUser = buildBaseUser(user);
        
        newUser.setRole(Role.MANAGER);
        newUser.setManagedBy(owner.getId());
        newUser.setCompany(company);

        return repository.save(newUser);
    }

    private User createUser(UserInputDTO user){
        User newUser = buildBaseUser(user);
        
        newUser.setRole(Role.USER);
        CompanyName company = companyRepository.findById(user.getCompany()).orElseThrow(() -> new RuntimeException("Company not found"));
        newUser.setCompany(company);
        newUser.setManagedBy(user.getManagedBy());
        
        return repository.save(newUser);
    }

    public User buildBaseUser(UserInputDTO user) {
        String temporaryPassword = temporaryPasswordService.generateLoginPassword();

        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setDateOfBirth(user.getDateOfBirth());

        newUser.setPassword(passwordEncoder.encode(temporaryPassword));
        newUser.setPasswordChange(false);

        User savedUser = repository.save(newUser);

        notificationService.notifyIncorporationCredentials(savedUser, temporaryPassword);

        return savedUser;

    }

}
