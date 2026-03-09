package com.draculavenom.notification.dto;

import com.draculavenom.notification.model.NotificationSettings;

import lombok.Getter;

@Getter
public class NotificationSettingsResponse {
    
    private boolean emailEnabled;
    private boolean whatsappEnabled;
    private boolean appointmentCreated;
    private boolean paymentRunsOut;
    private boolean appointmentStatusChanges;
    private boolean appointmentTimeManager;
    private boolean appointmentTimeUser;

    public NotificationSettingsResponse(NotificationSettings settings){
        this.emailEnabled = settings.isEmailEnabled();
        this.whatsappEnabled = settings.isWhatsappEnabled();
        this.appointmentCreated = settings.isAppointmentCreated();
        this.paymentRunsOut = settings.isPaymentRunsOut();
        this.appointmentStatusChanges = settings.isAppointmentStatusChanges();
        this.appointmentTimeManager = settings.isAppointmentTimeManager();
        this.appointmentTimeUser = settings.isAppointmentTimeUser();
    }

}
