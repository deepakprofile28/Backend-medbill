package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.SalesReturn;

@Repository
public interface SalesReturnRepository extends JpaRepository<SalesReturn, Long> {
    List<SalesReturn> findByCompanyId(Long companyId);
    List<SalesReturn> findByCompanyIdAndStatus(Long companyId, String status);
    List<SalesReturn> findByStatus(String status);
}
