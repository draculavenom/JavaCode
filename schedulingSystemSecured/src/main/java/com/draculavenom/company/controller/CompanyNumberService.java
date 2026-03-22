package com.draculavenom.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.company.CompanyName;
import com.draculavenom.company.CompanyNameRepository;
import com.draculavenom.company.dto.CompanyNumberRequest;
import com.draculavenom.company.dto.CompanyNumberResponse;

@Service
public class CompanyNumberService {
    
    @Autowired private CompanyNameRepository repository;

    public void createOrUpdate(Integer managerId, CompanyNumberRequest request){
        CompanyName company = repository.findByUserId(managerId).orElseThrow(() -> new RuntimeException("Company not found"));

        company.setCompanyNumber(request.getCompanyNumber());
        repository.save(company);
    }

    public CompanyNumberResponse get(Integer managerId){
        CompanyName company = repository.findByUserId(managerId).orElseThrow(() -> new RuntimeException("Company not found"));
        return new CompanyNumberResponse(company.getCompanyNumber());
    }

    public Integer getManagerIdByCompanyNumber(String companyNumber){
        CompanyName company = repository.findByCompanyNumber(companyNumber)
            .orElseThrow(() -> new RuntimeException("Company not found"));
        
        return company.getUser().getId();
    }

}
