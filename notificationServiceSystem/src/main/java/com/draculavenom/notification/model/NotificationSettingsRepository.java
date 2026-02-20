package com.draculavenom.notification.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface NotificationSettingsRepository extends JpaRepository<NotificationSettings, Integer> {
    
    Optional<NotificationSettings> findByUserId(int userId); 

}
