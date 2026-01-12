package com.draculavenom.notification.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingsRequest {
    
    private boolean emailEnabled;
    private boolean appointmentCreated;
    
}
