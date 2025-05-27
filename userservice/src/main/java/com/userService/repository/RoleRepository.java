package com.userService.repository;

import com.common.entity.ERole;
import com.common.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRole(ERole role);

    Role findRoleByUserId(final String  userId);
}
