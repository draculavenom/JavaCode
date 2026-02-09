package com.draculavenom.schedulingSystem.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;

@Service
public class ManagerOptionsService {
    
    @Autowired private ManagerOptionsRepository managerRepository;

    public boolean isManagerActive(Integer managerId){
      ManagerOptions options = managerRepository.findTopByManagerIdOrderByActiveDateDesc(managerId);

      if(options == null){
        return false;
      }

      return !options.getActiveDate().isBefore(LocalDate.now());
    }

    public static void activateOptional(String managerId, String optionId) {
      ManagerOptions options = managerRepository.findById(Integer.parseInt(optionId)).orElseThrow(() -> new RuntimeException("Option not found"));
      options.setActiveDate(LocalDate.now().plusMonths(1));
      managerRepository.save(options);
    }

}
