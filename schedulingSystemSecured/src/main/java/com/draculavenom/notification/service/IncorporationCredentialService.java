package com.draculavenom.notification.service;

import org.springframework.stereotype.Service;

import com.draculavenom.schedulingSystem.utilities.EmailService;
import com.draculavenom.security.user.User;

@Service
public class IncorporationCredentialService {

    private final EmailService emailService;

    public IncorporationCredentialService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendIncorporationCredential(User manager){
        String subject = "Welcome to the system";

        String body = """
            Hello %s,

            Your manager account has been created successfully.

            Your temporary password is: password
            
            Please log in and change your password as soon as possible.
            """.formatted(
                manager.getName()
            );

            emailService.sendSimpleMessage(manager.getEmail(), subject, body);
    }

}
