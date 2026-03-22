package com.draculavenom.whatsapp.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WhatsappSessionRepository extends JpaRepository<WhatsappSession, String> {
    
}
