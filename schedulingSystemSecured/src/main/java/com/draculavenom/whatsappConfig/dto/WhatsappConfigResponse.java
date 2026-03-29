package com.draculavenom.whatsappConfig.dto;

import com.draculavenom.whatsappConfig.model.WhatsappConfig;

import lombok.Getter;

@Getter
public class WhatsappConfigResponse {
    
    private String phoneNumberId;
    private boolean active;

    public WhatsappConfigResponse(WhatsappConfig config){
        this.phoneNumberId = config.getPhoneNumberId();
        this.active = config.isActive();
    }
}
