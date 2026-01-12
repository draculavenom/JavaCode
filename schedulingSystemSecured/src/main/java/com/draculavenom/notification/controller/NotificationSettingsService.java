package com.draculavenom.notification.controller;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.draculavenom.notification.model.NotificationSettings;
import com.draculavenom.notification.model.NotificationSettingsRepository;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;

@Service
public class NotificationSettingsService {
    private final NotificationSettingsRepository repository;

    public NotificationSettingsService(NotificationSettingsRepository repository){
        this.repository = repository;
    }

    public NotificationSettings getOrCreate(User user){
        return repository.findByUserId(user.getId()).orElseGet(() -> repository.save(new NotificationSettings(user)));
    }

    public NotificationSettings updateSettings(
        User user, 
        boolean emailEnabled,
        boolean appointmentCreated){
            if(!user.getRole().equals(Role.MANAGER)){
                throw new AccessDeniedException("Solo manager pueden modificar las notificaciones");
            }

        NotificationSettings settings = getOrCreate(user);

        settings.setEmailEnabled(emailEnabled);
        settings.setAppointmentCreated(appointmentCreated);

        return repository.save(settings);
    }

    public boolean shouldNotifyAppointmentCreated(User manager){
        return repository.findByUserId(manager.getId())
            .map(s -> s.isEmailEnabled() && s.isAppointmentCreated())
            .orElse(true);
    }

}
