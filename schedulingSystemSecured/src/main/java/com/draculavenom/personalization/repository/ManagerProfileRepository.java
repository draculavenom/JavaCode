package com.draculavenom.personalization.repository;

import com.draculavenom.personalization.model.ManagerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ManagerProfileRepository extends JpaRepository<ManagerProfile, Integer> {
    Optional<ManagerProfile> findByManagerId(Integer managerId);
}