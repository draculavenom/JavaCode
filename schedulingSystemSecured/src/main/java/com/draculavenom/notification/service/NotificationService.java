package com.draculavenom.notification.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.notification.controller.NotificationSettingsService;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@Service
public class NotificationService {

    @Autowired private NotificationSettingsService settingsService;
    @Autowired private AppointmentNotificationService appointmentNotificationService;
    @Autowired private UserRepository userRepository;
    @Autowired private IncorporationCredentialService incorporationCredentialService;
    @Autowired private ManagerNotificationService managerNotificationService;

    public NotificationService(NotificationSettingsService settingsService, AppointmentNotificationService appointmentNotificationService, UserRepository userRepository, IncorporationCredentialService incorporationCredentialService){
        this.settingsService = settingsService;
        this.appointmentNotificationService = appointmentNotificationService;
        this.userRepository = userRepository;
        this.incorporationCredentialService = incorporationCredentialService;
    }

    public void notifyAppointmentCreated(Appointment appointment){
        User user = userRepository.findById(appointment.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getManagedBy() == null){
            return;
        }

        User manager = userRepository.findById(user.getManagedBy())
        .orElseThrow(() -> new IllegalStateException("Manager not found"));

        appointmentNotificationService.sendAppointmentCreated(manager, appointment);
    }

    public void notifyIncorporationCredentials(User manager, String temporaryPassword){
        try{
            incorporationCredentialService.sendIncorporationCredential(manager, temporaryPassword);    
        }catch(Exception e){
            System.err.println("Error sending incorporation email: " + e.getMessage());
        }        
    }

    public void notifyAppointmentStatusChanged(Appointment appointment, AppointmentStatus status){
        User user = userRepository.findById(appointment.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(user.getManagedBy() == null) {
            return;
        }
        User manager = userRepository.findById(user.getManagedBy())
            .orElseThrow(() -> new IllegalStateException("Manager not found"));

        try{
            appointmentNotificationService.sendAppointmentStatusChanged(user, appointment, status, manager);
        }catch(Exception e){
            System.err.println("Error sending appointment confirmed: " + e.getMessage());
        }
    }

    public void notifySubscriptionReminder(User manager, LocalDate expirationDate, long daysLeft){
        try{
            managerNotificationService.sendSubscriptionReminder(manager, expirationDate, daysLeft);    
        }catch(Exception e){
            System.err.println("Error sending subscription expired email: " + e.getMessage());
        } 
    }

    public void notifyAppointmentTimeManager(Appointment appointment){
        User user = userRepository.findById(appointment.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(user.getManagedBy() == null) {
            return;
        }
        User manager = userRepository.findById(user.getManagedBy())
            .orElseThrow(() -> new IllegalStateException("Manager not found"));
        
        try{
            appointmentNotificationService.sendAppointmentTimeManager(manager, appointment, user);
        }catch(Exception e){
            System.err.println("Error sending appointment confirmed: " + e.getMessage());
        }
    }

    public void notifyAppointmentTimeUser(Appointment appointment){
        User user = userRepository.findById(appointment.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if(user.getManagedBy() == null) {
            return;
        }
        User manager = userRepository.findById(user.getManagedBy())
            .orElseThrow(() -> new IllegalStateException("Manager not found"));

        try{
            appointmentNotificationService.sendAppointmentTimeUser(user, appointment, manager);
        }catch(Exception e){
            System.err.println("Error sending appointment confirmed: " + e.getMessage());
        }
    }

}
