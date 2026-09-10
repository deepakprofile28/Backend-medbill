package com.medbill.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.medbill.entity.Rack;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long> {
    List<Rack> findByCompanyIdOrderByNameAsc(Long companyId);
    List<Rack> findByWarehouseIdOrderByNameAsc(Long warehouseId);
    List<Rack> findAllByOrderByNameAsc();
}
