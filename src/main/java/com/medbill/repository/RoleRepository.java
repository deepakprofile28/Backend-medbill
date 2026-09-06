package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByCompanyId(Long companyId);

    List<Role> findByStatus(String status);

    List<Role> findByCompanyIdAndStatus(Long companyId, String status);

    boolean existsByRoleNameIgnoreCase(String roleName);

    boolean existsByRoleNameIgnoreCaseAndIdNot(String roleName, Long id);
}
