package com.draculavenom.schedulingSystem.model;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.draculavenom.security.user.User;

public interface ScheduleConfigRepository extends JpaRepository<ScheduleConfig, Integer> {
    
    Optional<ScheduleConfig> findByManagerAndDayOfWeek(User manager, DayOfWeekEnum dayOfWeek);
    
    boolean existsByManagerAndDayOfWeek(User manager, DayOfWeekEnum dayOfWeek);    

    void deleteByManager(User manager);
    
    List<ScheduleConfig> findByManager(User manager);
}
