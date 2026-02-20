package com.draculavenom.notification.dto;

import com.draculavenom.notification.model.NotificationSettings;

import lombok.Getter;

@Getter
public class NotificationSettingsResponse {
    
    private boolean emailEnabled;
    private boolean appointmentCreated;
    private boolean paymentRunsOut;
    private boolean appointmentStatusChanges;

    public NotificationSettingsResponse(NotificationSettings settings){
        this.emailEnabled = settings.isEmailEnabled();
        this.appointmentCreated = settings.isAppointmentCreated();
        this.paymentRunsOut = settings.isPaymentRunsOut();
        this.appointmentStatusChanges = settings.isAppointmentStatusChanges();
    }

}
