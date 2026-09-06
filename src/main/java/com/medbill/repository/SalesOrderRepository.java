package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.SalesOrder;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    List<SalesOrder> findByCompanyId(Long companyId);
    List<SalesOrder> findByCompanyIdAndStatus(Long companyId, String status);
    List<SalesOrder> findByStatus(String status);
}
