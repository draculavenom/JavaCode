package com.draculavenom.whatsappConfig.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.draculavenom.company.CompanyName;
import com.draculavenom.company.CompanyNameRepository;
import com.draculavenom.whatsappConfig.dto.WhatsappConfigRequest;
import com.draculavenom.whatsappConfig.dto.WhatsappConfigResponse;
import com.draculavenom.whatsappConfig.model.WhatsappConfig;
import com.draculavenom.whatsappConfig.model.WhatsappConfigRepository;

@Service
public class WhatsappConfigService {
    
    @Autowired private WhatsappConfigRepository repository;
    @Autowired private CompanyNameRepository companyNameRepository;

    public void CreateOrUpdate(Integer managerId, WhatsappConfigRequest request){
        CompanyName company = companyNameRepository
            .findByUserId(managerId)
            .orElseThrow(() -> new RuntimeException("Company not found"));

        WhatsappConfig config = repository.findByCompanyId(company.getId()).orElse(new WhatsappConfig());

        config.setCompany(company);
        config.setPhoneNumberId(request.getPhoneNumberId());
        config.setAccessToken(request.getAccessToken());
        config.setWabaId(request.getWabaId());
        config.setVerifyToken(request.getVerifyToken());
        config.setActive(true);
        repository.save(config);
    }

    public WhatsappConfig getByPhoneNumberId(String phoneNumberId){
        return repository.findByPhoneNumberId(phoneNumberId).orElse(null);
    }

    public WhatsappConfigResponse getByManager(Integer managerId){
        CompanyName company = companyNameRepository.findByUserId(managerId).orElseThrow(() -> new RuntimeException("Company not found"));
        WhatsappConfig config = repository.findByCompanyId(company.getId()).orElseThrow(() -> new RuntimeException("Whatsapp config not found"));
        return new WhatsappConfigResponse(config);
    }

    public void toggleActive(Integer managerID, boolean active){
        CompanyName company = companyNameRepository.findByUserId(managerId).orElseThrow(() -> new RuntimeException("Company not found"));
        WhatsappConfig config = repository.findByCompanyId(company.getId()).orElseThrow(() -> new RuntimeException("Whatsapp config not found"));

        config.setActive(active);
        repository.save(config);
    }

}
