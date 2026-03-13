package com.draculavenom.managementModules.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ManagementModulesRepository extends JpaRepository<ManagementModules, Integer>{
    Optional<ManagementModules> findByManagerId(int managerId);
}
