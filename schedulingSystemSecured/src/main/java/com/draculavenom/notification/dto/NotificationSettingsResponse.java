package com.draculavenom.notification.dto;

import com.draculavenom.notification.model.NotificationSettings;

import lombok.Getter;

@Getter
public class NotificationSettingsResponse {
    
    private boolean emailEnabled;
    private boolean appointmentCreated;

    public NotificationSettingsResponse(NotificationSettings settings){
        this.emailEnabled = settings.isEmailEnabled();
        this.appointmentCreated = settings.isAppointmentCreated();
    }

}
