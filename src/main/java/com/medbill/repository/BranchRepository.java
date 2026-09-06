package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.Branch;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findByCompanyIdOrderByNameAsc(Long companyId);

    List<Branch> findByCompanyIdAndStatus(Long companyId, String status);

    List<Branch> findByStatus(String status);
}

