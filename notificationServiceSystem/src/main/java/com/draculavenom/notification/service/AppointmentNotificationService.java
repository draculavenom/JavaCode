package com.draculavenom.notification.service;

import org.springframework.stereotype.Service;

import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;
import com.draculavenom.schedulingSystem.utilities.EmailService;
import com.draculavenom.security.user.User;

@Service
public class AppointmentNotificationService {

    private final EmailService emailService;

    public AppointmentNotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void sendAppointmentCreated(User manager, Appointment appointment){

        String subject = "New Appointment Scheduled";

        String body = """
            Hello %s,

            A new appointment has been requested.

            Date: %s
            Time: %s

            Please log in to the system to manage it.
            """.formatted(
                manager.getName(), 
                appointment.getDate(),
                appointment.getTime()
            );

            emailService.sendSimpleMessage(manager.getEmail(), subject, body);
    }

    public void sendAppointmentStatusChanged(User user, Appointment appointment, AppointmentStatus status){

        String subject = "Appointment Update";

        String statusMessage = switch(status){
            case CONFIRMED -> "has been confirmed";
            case CANCELLED -> "has been cancelled";
            case COMPLETED -> "has been completed";
            default -> "hass been updated";
        };

        String body = """
            Hello %s,

            Your appointment scheduled for:
            
            Date: %s
            Time: %s 

            %s
            
            If you have any questions, please contact support.
            """.formatted(
                user.getName(), 
                appointment.getDate(),
                appointment.getTime(),
                statusMessage
            );

            emailService.sendSimpleMessage(user.getEmail(), subject, body);
    }

}
