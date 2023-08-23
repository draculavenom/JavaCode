package com.draculavenom.securityManagement.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, Long>{
	Optional<Roles> findByName(String name);
	Boolean existsByName(String name);
}
