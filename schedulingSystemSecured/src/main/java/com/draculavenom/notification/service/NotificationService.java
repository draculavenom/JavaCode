package com.draculavenom.notification.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.notification.controller.NotificationSettingsService;
import com.draculavenom.schedulingSystem.model.Appointment;
import com.draculavenom.schedulingSystem.model.AppointmentStatus;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserManagement;
import com.draculavenom.security.user.UserManagementRepository;
import com.draculavenom.security.user.UserRepository;

@Service
public class NotificationService {

    @Autowired private NotificationSettingsService settingsService;
    @Autowired private AppointmentNotificationService appointmentNotificationService;
    @Autowired private UserRepository userRepository;
    @Autowired private IncorporationCredentialService incorporationCredentialService;
    @Autowired private ManagerNotificationService managerNotificationService;
    @Autowired private UserManagementRepository userManagementRepository;

    public NotificationService(NotificationSettingsService settingsService, AppointmentNotificationService appointmentNotificationService, UserRepository userRepository, IncorporationCredentialService incorporationCredentialService){
        this.settingsService = settingsService;
        this.appointmentNotificationService = appointmentNotificationService;
        this.userRepository = userRepository;
        this.incorporationCredentialService = incorporationCredentialService;
    }

    public void notifyAppointmentCreated(Appointment appointment){
        User user = userRepository.findById(appointment.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserManagement> relations = userManagementRepository.findByCustomer(user);
        if(relations.isEmpty()) return;
        User manager = relations.get(0).getManager();
        if(!settingsService.shouldNotifyAppointmentCreated(manager)){
            return;
        }

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
        
        /*if(user.getManagedBy() == null) {
            return;
        }
        User manager = userRepository.findById(user.getManagedBy())
            .orElseThrow(() -> new IllegalStateException("Manager not found"));
        */
        List<UserManagement> relations = userManagementRepository.findByCustomer(user);
        if(relations.isEmpty()) return;
        User manager = relations.get(0).getManager();
        if(!settingsService.shouldNotifyAppointmentStatusChanges(manager)) {
            return;
        }

        try{
            appointmentNotificationService.sendAppointmentStatusChanged(user, appointment, status);
        }catch(Exception e){
            System.err.println("Error sending appointment confirmed: " + e.getMessage());
        }
    }

    public void notifySubscriptionReminder(User manager, LocalDate expirationDate, long daysLeft){
        if(!settingsService.shouldNotifyPaymentRunOut(manager)) {
            return;
        }

        try{
            managerNotificationService.sendSubscriptionReminder(manager, expirationDate, daysLeft);    
        }catch(Exception e){
            System.err.println("Error sending subscription expired email: " + e.getMessage());
        } 
    }

    public void notifyAppointmentTimeManager(Appointment appointment){
        User user = userRepository.findById(appointment.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        /*if(user.getManagedBy() == null) {
            return;
        }
        User manager = userRepository.findById(user.getManagedBy())
            .orElseThrow(() -> new IllegalStateException("Manager not found"));
        */
        List<UserManagement> relations = userManagementRepository.findByCustomer(user);
        if(relations.isEmpty()) return;
        User manager = relations.get(0).getManager();
        if(!settingsService.shouldNotifyAppointmentTimeManager(manager)) {
            return;
        }

        try{
            appointmentNotificationService.sendAppointmentTimeManager(manager, appointment, user);
        }catch(Exception e){
            System.err.println("Error sending appointment confirmed: " + e.getMessage());
        }
    }

    public void notifyAppointmentTimeUser(Appointment appointment){
        User user = userRepository.findById(appointment.getUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        /*if(user.getManagedBy() == null) {
            return;
        }
        User manager = userRepository.findById(user.getManagedBy())
            .orElseThrow(() -> new IllegalStateException("Manager not found"));
        */
        List<UserManagement> relations = userManagementRepository.findByCustomer(user);
        if(relations.isEmpty()) return;
        User manager = relations.get(0).getManager();
        if(!settingsService.shouldNotifyAppointmentTimeUser(manager)) {
            return;
        }

        try{
            appointmentNotificationService.sendAppointmentTimeUser(user, appointment, manager);
        }catch(Exception e){
            System.err.println("Error sending appointment confirmed: " + e.getMessage());
        }
    }

}
