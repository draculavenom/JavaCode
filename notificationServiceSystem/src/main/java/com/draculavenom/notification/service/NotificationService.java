package com.draculavenom.notification.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.notification.controller.NotificationSettingsService;
import com.draculavenom.notification.model.UserClient;
import com.draculavenom.notification.model.UserDTO;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;

@Service
public class NotificationService {

    @Autowired private NotificationSettingsService settingsService;
    @Autowired private AppointmentNotificationService appointmentNotificationService;
    @Autowired private UserClient userClient;
    @Autowired private IncorporationCredentialService incorporationCredentialService;
    @Autowired private ManagerNotificationService managerNotificationService;

    public NotificationService(NotificationSettingsService settingsService, AppointmentNotificationService appointmentNotificationService, UserClient userClient, IncorporationCredentialService incorporationCredentialService){
        this.settingsService = settingsService;
        this.appointmentNotificationService = appointmentNotificationService;
        this.userClient = userClient;
        this.incorporationCredentialService = incorporationCredentialService;
    }

    public void notifyAppointmentCreated(Appointment appointment){
        
        UserDTO user = userClient.getUserById(appointment.getUserId());

        if(user.getManagedBy() == null){
            return;
        }

        UserDTO manager = userClient.getUserById(user.getManagedBy());

        if(!settingsService.shouldNotifyAppointmentCreated(manager)){
            return;
        }

        appointmentNotificationService.sendAppointmentCreated(manager, appointment);
    }

    public void notifyIncorporationCredentials(Integer managerId, String temporaryPassword){
        try{
            UserDTO manager = userClient.getUserById(managerId);
            incorporationCredentialService.sendIncorporationCredential(manager, temporaryPassword);    
        }catch(Exception e){
            System.err.println("Error sending incorporation email: " + e.getMessage());
        }        
    }

    public void notifyAppointmentStatusChanged(Appointment appointment, AppointmentStatus status){
        
        UserDTO user = userClient.getUserById(appointment.getUserId());

        if(user.getManagedBy() == null) {
            return;
        }
        UserDTO manager = userClient.getUserById(user.getManagedBy());
        
        if(!settingsService.shouldNotifyAppointmentStatusChanges(manager)) {
            return;
        }

        try{
            appointmentNotificationService.sendAppointmentStatusChanged(user, appointment, status);
        }catch(Exception e){
            System.err.println("Error sending appointment confirmed: " + e.getMessage());
        }
    }

    public void notifySubscriptionReminder(Integer managerId, LocalDate expirationDate, long daysLeft){
        if(!settingsService.shouldNotifyPaymentRunOut(managerId)) {
            return;
        }

        try{
            UserDTO manager = userClient.getUserById(managerId);
            managerNotificationService.sendSubscriptionReminder(manager, expirationDate, daysLeft);    
        }catch(Exception e){
            System.err.println("Error sending subscription expired email: " + e.getMessage());
        } 
    }

}
