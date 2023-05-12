package com.draculavenom.securityManagement.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByUsername(String username);
    Boolean existsByUsername(String username);
}
