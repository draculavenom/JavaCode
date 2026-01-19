package com.draculavenom.schedulingSystem.scheduler;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.draculavenom.notification.service.NotificationService;
import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@Component
public class ManagerOptionsScheduler {
    
    private final ManagerOptionsRepository managerOptionsRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public ManagerOptionsScheduler(ManagerOptionsRepository managerOptionsRepository,
            NotificationService notificationService, UserRepository userRepository) {
        this.managerOptionsRepository = managerOptionsRepository;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @Scheduled(cron = "0 00 0 * * ?") // Ejecución de todos los días a la media noche
    public void notifyManagerAboutExpiration() {
        LocalDate today = LocalDate.now();

        List<ManagerOptions> options = managerOptionsRepository.findAll();

        for (ManagerOptions opt : options){
            long daysLeft = ChronoUnit.DAYS.between(today, opt.getActiveDate());

            if(daysLeft == 5 || daysLeft == 1){
                User manager = userRepository.findById(opt.getManagerId()).orElse(null);

                if(manager == null){
                    continue;
                }

                if(manager != null){
                    notificationService.notifySubscriptionReminder(
                        manager,
                        opt.getActiveDate(),
                        daysLeft
                    );
                }
            }
        }
        
    }

}
