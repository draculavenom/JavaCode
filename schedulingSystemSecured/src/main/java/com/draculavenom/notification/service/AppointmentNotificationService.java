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

        String commentsSection = switch(status){
            case CONFIRMED -> "\nAdditional info:\n" + appointment.getComment() + "\n";
            case CANCELLED -> "\nReason for cancellation:\n" + appointment.getComment() + "\n";
            case COMPLETED -> "\nManager notes:\n" + appointment.getComment() + "\n";
            default -> "\nComments:\n" + appointment.getComment() + "\n";
        };
        

        String body = """
            Hello %s,

            Your appointment scheduled for:
            
            Date: %s
            Time: %s 

            %s
            %s
            
            If you have any questions, please contact support.
            """.formatted(
                user.getName(), 
                appointment.getDate(),
                appointment.getTime(),
                statusMessage, commentsSection
            );

            emailService.sendSimpleMessage(user.getEmail(), subject, body);
    }

    public void sendAppointmentTimeManager(User manager, Appointment appointment,  User user){

        String subject = "Appointment Reminder - Start in 5 minutes";

        String body = """
            Hello %s,

            We are reminding you that you have an appointment scheduled fot today at %s.

            Client: %s.

            The appointment will begin in approximately 5 minutes.

            Please log in to the system to manage it.
            """.formatted(
                manager.getName(), 
                appointment.getTime(),
                user.getName()
            );

            emailService.sendSimpleMessage(manager.getEmail(), subject, body); 
    }

    public void sendAppointmentTimeUser(User user, Appointment appointment, User manager){
        String companyName = manager.getCompany() != null 
            ? manager.getCompany().getNameCompany() : manager.getName(); 
        String subject = "Your Appointment Start in 5 minutes";

        String body = """
            Hello %s,

            This is a reminder that you have an appointment scheduled today at %s with %s.

            The appointment will begin in approximately 5 minutes.

            We look forward to seeing you!
            """.formatted(
                user.getName(), 
                appointment.getTime(),
                companyName
            );

            emailService.sendSimpleMessage(user.getEmail(), subject, body); 
    }

}
