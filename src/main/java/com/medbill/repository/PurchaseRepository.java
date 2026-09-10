package com.medbill.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.medbill.entity.Purchase;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByCompanyIdOrderByIdDesc(Long companyId);
    List<Purchase> findAllByOrderByIdDesc();
}
