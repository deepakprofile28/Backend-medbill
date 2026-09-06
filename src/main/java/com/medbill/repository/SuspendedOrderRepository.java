package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.SuspendedOrder;

@Repository
public interface SuspendedOrderRepository extends JpaRepository<SuspendedOrder, Long> {
    List<SuspendedOrder> findByCompanyId(Long companyId);
    List<SuspendedOrder> findByCompanyIdAndStatus(Long companyId, String status);
    List<SuspendedOrder> findByStatus(String status);
}
