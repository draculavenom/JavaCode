package com.draculavenom.company;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyNameRepository extends JpaRepository<CompanyName, Integer> {
 
    Optional<CompanyName> findByUserId(Integer userId);

    Optional<CompanyName> findByCompanyNumber(String companyNumber);
}
