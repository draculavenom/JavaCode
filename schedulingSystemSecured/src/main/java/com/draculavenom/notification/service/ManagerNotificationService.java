package com.draculavenom.notification.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.draculavenom.schedulingSystem.utilities.EmailService;
import com.draculavenom.security.user.User;

@Service
public class ManagerNotificationService {
    private final EmailService emailService;

    public ManagerNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendSubscriptionReminder(User manager, LocalDate expirationDate, long daysLeft){
        String subject = "Your subscription is about to expire";

        String body = """
            Hello %s,

            Your system access expires on %s.
            
            You have %d days remaining.
            
            Please make your payment to avoid service suspension.

            Regards,
            Support.
            """.formatted(
                manager.getName(),
                expirationDate,
                daysLeft
            );

            emailService.sendSimpleMessage(manager.getEmail(), subject, body);
    }    

}
