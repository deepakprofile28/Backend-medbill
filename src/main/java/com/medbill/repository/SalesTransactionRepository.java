package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.SalesTransaction;

@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Long> {
    List<SalesTransaction> findByCompanyId(Long companyId);
    List<SalesTransaction> findByCompanyIdAndStatus(Long companyId, String status);
    List<SalesTransaction> findByStatus(String status);
}
