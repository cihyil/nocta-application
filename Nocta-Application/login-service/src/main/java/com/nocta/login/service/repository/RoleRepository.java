package com.nocta.login.service.repository;

import java.util.Optional;

import com.nocta.login.service.models.ERole;
import com.nocta.login.service.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
