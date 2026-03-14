package com.draculavenom.company.dto;


import lombok.Getter;

@Getter
public class CompanyNumberResponse {
    private String companyNumber;

    public CompanyNumberResponse(String companyNumber){
        this.companyNumber = companyNumber;
    }
}
