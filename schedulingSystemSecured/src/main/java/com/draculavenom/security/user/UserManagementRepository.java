package com.draculavenom.security.user;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserManagementRepository extends JpaRepository<UserManagement, Integer>{
    List<UserManagement> findByManager(User manager);

    List<UserManagement> findByCustomer(User customer);

    void deleteByCustomer(User customer);
}
