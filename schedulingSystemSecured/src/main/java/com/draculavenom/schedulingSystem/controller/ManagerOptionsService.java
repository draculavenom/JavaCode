package com.draculavenom.schedulingSystem.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.company.CompanyName;
import com.draculavenom.schedulingSystem.model.ManagerOptions;
import com.draculavenom.schedulingSystem.model.ManagerOptionsRepository;
import com.draculavenom.security.user.Role;
import com.draculavenom.security.user.User;
import com.draculavenom.security.user.UserRepository;

@Service
public class ManagerOptionsService {
    
    @Autowired private ManagerOptionsRepository managerRepository;
    @Autowired private UserRepository repository;

    public boolean isCompanyActive(Integer companyId){
      ManagerOptions options = managerRepository.findTopByCompanyIdOrderByActiveDateDesc(companyId);

      if(options == null){
        return false;
      }

      return !options.getActiveDate().isBefore(LocalDate.now());
    }

    public List<User> getPersons(User currentUser){
      User user = repository.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("User not found"));
      CompanyName company = user.getCompany();
      switch(user.getRole()){
        case OWNER:
          return repository.findByCompany(company);
        
          case MANAGER:
            return repository.findByCompanyAndRole(company, Role.USER);

          default:
            throw new RuntimeException("Unauthorized");
      }
    }
}
