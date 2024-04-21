package com.nhhp.identityservices.repository;

import com.nhhp.identityservices.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
