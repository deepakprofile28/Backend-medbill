package com.medbill.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Branch;
import com.medbill.entity.Company;
import com.medbill.repository.BranchRepository;
import com.medbill.repository.CompanyRepository;

@Service
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;
    private final CompanyRepository companyRepository;

    public BranchService(BranchRepository branchRepository, CompanyRepository companyRepository) {
        this.branchRepository = branchRepository;
        this.companyRepository = companyRepository;
    }

    public List<Branch> getAllBranches(Long companyId) {
        if (companyId != null) {
            List<Branch> list = branchRepository.findByCompanyIdOrderByNameAsc(companyId);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return branchRepository.findAll();
    }

    public List<Branch> getActiveBranches(Long companyId) {
        if (companyId != null) {
            return branchRepository.findByCompanyIdAndStatus(companyId, "ACTIVE");
        }
        return branchRepository.findByStatus("ACTIVE");
    }

    public Branch getBranchById(Long id) {
        return branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with ID: " + id));
    }

    public Branch createBranch(Branch branch, Long companyId) {
        if (branch.getName() == null || branch.getName().trim().isEmpty()) {
            throw new RuntimeException("Branch name is required");
        }

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            if (company != null) {
                branch.setCompany(company);
            }
        }

        return branchRepository.save(branch);
    }

    public Branch updateBranch(Long id, Branch details) {
        Branch existing = getBranchById(id);
        existing.setName(details.getName());
        existing.setBranchCode(details.getBranchCode());
        existing.setAddress(details.getAddress());
        existing.setCity(details.getCity());
        existing.setPhone(details.getPhone());
        existing.setEmail(details.getEmail());
        existing.setGstin(details.getGstin());
        existing.setDlNumber(details.getDlNumber());
        existing.setBranchManager(details.getBranchManager());
        existing.setStatus(details.getStatus());

        return branchRepository.save(existing);
    }

    public void deleteBranch(Long id) {
        Branch existing = getBranchById(id);
        branchRepository.delete(existing);
    }
}

