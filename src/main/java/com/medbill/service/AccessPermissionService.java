package com.medbill.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.AccessPermission;
import com.medbill.entity.Company;
import com.medbill.repository.AccessPermissionRepository;
import com.medbill.repository.CompanyRepository;

@Service
@Transactional
public class AccessPermissionService {

    private final AccessPermissionRepository accessPermissionRepository;
    private final CompanyRepository companyRepository;

    public AccessPermissionService(AccessPermissionRepository accessPermissionRepository, CompanyRepository companyRepository) {
        this.accessPermissionRepository = accessPermissionRepository;
        this.companyRepository = companyRepository;
    }

    public List<AccessPermission> getAll(Long companyId) {
        if (companyId != null) {
            List<AccessPermission> list = accessPermissionRepository.findByCompanyId(companyId);
            if (list.isEmpty()) {
                return accessPermissionRepository.findAll();
            }
            return list;
        }
        return accessPermissionRepository.findAll();
    }

    public List<AccessPermission> getActive(Long companyId) {
        if (companyId != null) {
            List<AccessPermission> list = accessPermissionRepository.findByCompanyIdAndStatus(companyId, "ACTIVE");
            if (list.isEmpty()) {
                return accessPermissionRepository.findByStatus("ACTIVE");
            }
            return list;
        }
        return accessPermissionRepository.findByStatus("ACTIVE");
    }

    public AccessPermission getById(Long id) {
        return accessPermissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Access permission profile not found with id: " + id));
    }

    public AccessPermission create(AccessPermission accessPermission, Long companyId) {
        if (accessPermission.getName() == null || accessPermission.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Access name is required");
        }

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            accessPermission.setCompany(company);
        }

        if (accessPermission.getStatus() == null || accessPermission.getStatus().trim().isEmpty()) {
            accessPermission.setStatus("ACTIVE");
        }

        return accessPermissionRepository.save(accessPermission);
    }

    public AccessPermission update(Long id, AccessPermission updated) {
        AccessPermission existing = getById(id);

        if (updated.getName() != null && !updated.getName().trim().isEmpty()) {
            existing.setName(updated.getName().trim());
        }

        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().trim());
        }

        if (updated.getPosPermissions() != null) {
            existing.setPosPermissions(updated.getPosPermissions().trim());
        }

        if (updated.getModulePermissions() != null) {
            existing.setModulePermissions(updated.getModulePermissions().trim());
        }

        if (updated.getStatus() != null && !updated.getStatus().trim().isEmpty()) {
            existing.setStatus(updated.getStatus().trim());
        }

        return accessPermissionRepository.save(existing);
    }

    public void delete(Long id) {
        AccessPermission existing = getById(id);
        accessPermissionRepository.delete(existing);
    }
}
