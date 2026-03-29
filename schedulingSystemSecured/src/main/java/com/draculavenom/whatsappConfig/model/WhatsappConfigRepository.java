package com.draculavenom.whatsappConfig.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhatsappConfigRepository extends JpaRepository<WhatsappConfig, Integer>{
    
    Optional<WhatsappConfig> findByPhoneNumberId(String phoneNumberId);

    Optional<WhatsappConfig> findByCompanyId(Integer companyId);

}
