package com.medbill.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.medbill.entity.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByCompanyIdOrderByNameAsc(Long companyId);
    List<Supplier> findByCompanyIdAndStatusOrderByNameAsc(Long companyId, String status);
    List<Supplier> findAllByOrderByNameAsc();
}
