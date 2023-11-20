package com.kma.cdhttt.repository;

import com.kma.cdhttt.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    RoleEntity findByRoleId(String roleId);
    RoleEntity findByRoleName(String roleName);
}
