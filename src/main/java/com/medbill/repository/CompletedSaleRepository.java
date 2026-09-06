package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.CompletedSale;

@Repository
public interface CompletedSaleRepository extends JpaRepository<CompletedSale, Long> {
    List<CompletedSale> findByCompanyId(Long companyId);
    List<CompletedSale> findByCompanyIdAndStatus(Long companyId, String status);
    List<CompletedSale> findByStatus(String status);
}
