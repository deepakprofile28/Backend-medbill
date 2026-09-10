package com.medbill.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.medbill.entity.PurchaseReturn;

@Repository
public interface PurchaseReturnRepository extends JpaRepository<PurchaseReturn, Long> {
    List<PurchaseReturn> findByCompanyIdOrderByIdDesc(Long companyId);
    List<PurchaseReturn> findAllByOrderByIdDesc();
}
