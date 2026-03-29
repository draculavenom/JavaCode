package com.draculavenom.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.notification.controller.NotificationSettingsService;
import com.draculavenom.notification.utilities.WhatsappService;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;
import com.draculavenom.schedulingSystem.utilities.EmailService;
import com.draculavenom.security.user.User;

@Service
public class AppointmentNotificationService {

    private final EmailService emailService;
    private final WhatsappService whatsappService;
    private final NotificationSettingsService settingsService;

    public AppointmentNotificationService(EmailService emailService, WhatsappService whatsappService, NotificationSettingsService settingsService) {
        this.emailService = emailService;
        this.whatsappService = whatsappService;
        this.settingsService = settingsService;
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

            if(settingsService.shouldNotifyAppointmentCreated(manager)){
                emailService.sendSimpleMessage(manager.getEmail(), subject, body);
            }

            /*if(settingsService.shouldNotifyAppointmentCreatedByWhatsapp(manager) && manager.getPhoneNumber() != null){
                whatsappService.sendMessage(manager.getPhoneNumber(), body);
            }*/
    }

    public void sendAppointmentStatusChanged(User user, Appointment appointment, AppointmentStatus status, User manager){

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

        if(settingsService.shouldNotifyAppointmentStatusChanges(manager)){
            emailService.sendSimpleMessage(user.getEmail(), subject, body);
        }

        /*if(settingsService.shouldNotifyAppointmentStatusChangesByWhatsapp(manager) && user.getPhoneNumber() != null){
            whatsappService.sendMessage(user.getPhoneNumber(), body);
        }*/
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

        if(settingsService.shouldNotifyAppointmentTimeManager(manager)){
            emailService.sendSimpleMessage(manager.getEmail(), subject, body); 
        }

        /*if(settingsService.shouldNotifyAppointmentTimeManagerByWhatsapp(manager) && manager.getPhoneNumber() != null){
            whatsappService.sendMessage(manager.getPhoneNumber(), body);
        }*/
    }

    public void sendAppointmentTimeUser(User user, Appointment appointment, User manager){
        String companyName = manager.getCompanyName() != null 
            ? manager.getCompanyName().getNameCompany() : manager.getName(); 
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

        if(settingsService.shouldNotifyAppointmentTimeUser(manager)){
            emailService.sendSimpleMessage(user.getEmail(), subject, body); 
        }

        /*if(settingsService.shouldNotifyAppointmentTimeUserByWhatsapp(manager) && user.getPhoneNumber() != null){
            whatsappService.sendMessage(user.getPhoneNumber(), body);
        }*/
    }

}
