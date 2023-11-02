package com.draculavenom.security.user;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);
  
  Optional<List<User>> findAllByManagedBy(int userId);
  
  Optional<List<User>> getAllByRole(Role role);

}
