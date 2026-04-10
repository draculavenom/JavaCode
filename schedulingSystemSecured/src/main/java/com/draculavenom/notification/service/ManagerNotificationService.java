package com.draculavenom.notification.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.draculavenom.schedulingSystem.model.Appointment;
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
    
    public void sendBlockedCompany(User user, Appointment appointment){
        String subject = "Tried to schedule an appointment";

        String body = """
            Hello %s,

            Someone tried to schedule an appointment, but was unsuccessful because the account is deactivated.

            Don't miss any more opportunities and reactivate your account today.
            """.formatted(
                user.getName()
            );

            emailService.sendSimpleMessage(user.getEmail(), subject, body);
    }

}
