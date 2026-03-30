package com.draculavenom.personalization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManagerProfileResponse {
    
    private Integer id;
    private String introduction;
    private String logo;
    private String managerFullName;

    public static final String DEFAULT_SYSTEM_LOGO = "SYSTEM_DEFAULT_CREAR_LOGO";

    public String getLogo() {
        if (this.logo == null || this.logo.trim().isEmpty()) {
            return DEFAULT_SYSTEM_LOGO;
        }
        return this.logo;
    }
}