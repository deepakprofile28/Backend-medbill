package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.AccessPermission;

@Repository
public interface AccessPermissionRepository extends JpaRepository<AccessPermission, Long> {

    List<AccessPermission> findByCompanyId(Long companyId);

    List<AccessPermission> findByStatus(String status);

    List<AccessPermission> findByCompanyIdAndStatus(Long companyId, String status);

    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}
