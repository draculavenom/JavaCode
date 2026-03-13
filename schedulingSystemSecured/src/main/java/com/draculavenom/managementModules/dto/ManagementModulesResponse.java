package com.draculavenom.managementModules.dto;

import com.draculavenom.managementModules.model.ManagementModules;

import lombok.Getter;

@Getter
public class ManagementModulesResponse {
    
    private boolean whatsappNotification;

    public ManagementModulesResponse(ManagementModules modules){
        this.whatsappNotification = modules.isWhatsappNotification();
    }

}
