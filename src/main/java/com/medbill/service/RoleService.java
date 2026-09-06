package com.medbill.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Company;
import com.medbill.entity.Role;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;

    public RoleService(RoleRepository roleRepository, CompanyRepository companyRepository) {
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
    }

    public List<Role> getAllRoles(Long companyId) {
        if (companyId != null) {
            List<Role> roles = roleRepository.findByCompanyId(companyId);
            if (roles.isEmpty()) {
                return roleRepository.findAll();
            }
            return roles;
        }
        return roleRepository.findAll();
    }

    public List<Role> getActiveRoles(Long companyId) {
        if (companyId != null) {
            List<Role> roles = roleRepository.findByCompanyIdAndStatus(companyId, "ACTIVE");
            if (roles.isEmpty()) {
                return roleRepository.findByStatus("ACTIVE");
            }
            return roles;
        }
        return roleRepository.findByStatus("ACTIVE");
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    public Role createRole(Role role, Long companyId) {
        if (role.getRoleName() == null || role.getRoleName().trim().isEmpty()) {
            throw new IllegalArgumentException("Role name is required");
        }

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            role.setCompany(company);
        }

        if (role.getStatus() == null || role.getStatus().trim().isEmpty()) {
            role.setStatus("ACTIVE");
        }

        return roleRepository.save(role);
    }

    public Role updateRole(Long id, Role updatedData) {
        Role existing = getRoleById(id);

        if (updatedData.getRoleName() != null && !updatedData.getRoleName().trim().isEmpty()) {
            existing.setRoleName(updatedData.getRoleName().trim());
        }

        if (updatedData.getDesignation() != null) {
            existing.setDesignation(updatedData.getDesignation().trim());
        }

        if (updatedData.getAccessLevel() != null) {
            existing.setAccessLevel(updatedData.getAccessLevel().trim());
        }

        if (updatedData.getDescription() != null) {
            existing.setDescription(updatedData.getDescription().trim());
        }

        if (updatedData.getStatus() != null && !updatedData.getStatus().trim().isEmpty()) {
            existing.setStatus(updatedData.getStatus().trim());
        }

        return roleRepository.save(existing);
    }

    public void deleteRole(Long id) {
        Role role = getRoleById(id);
        roleRepository.delete(role);
    }
}
