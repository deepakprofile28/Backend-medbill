package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.SalesExchange;

@Repository
public interface SalesExchangeRepository extends JpaRepository<SalesExchange, Long> {
    List<SalesExchange> findByCompanyId(Long companyId);
    List<SalesExchange> findByCompanyIdAndStatus(Long companyId, String status);
    List<SalesExchange> findByStatus(String status);
}
