package com.draculavenom.whatsappConfig.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhatsappConfigRequest {
    
    private String phoneNumberId;
    private String accessToken;
    private String wabaId;
    private String verifyToken;

}
