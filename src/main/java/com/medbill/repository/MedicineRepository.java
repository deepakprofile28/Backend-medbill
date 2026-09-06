package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.Company;
import com.medbill.entity.Medicine;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    List<Medicine> findByCompanyOrderByNameAsc(Company company);

    List<Medicine> findByCompanyIdOrderByNameAsc(Long companyId);

    List<Medicine> findByCompanyAndNameContainingIgnoreCase(Company company, String name);

    List<Medicine> findAllByOrderByNameAsc();
}

