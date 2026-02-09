package com.draculavenom.payments.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaymentDTO {
    private Integer managerOptionsId;

    public Integer getManagerOptionsId() {
        return managerOptionsId;
    }

    public void setManagerOptionsId(Integer managerOptionsId) {
        this.managerOptionsId = managerOptionsId;
    }
        
}
