package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    List<Brand> findByCompanyIdOrderByNameAsc(Long companyId);

    List<Brand> findByCompanyIdAndStatus(Long companyId, String status);

    List<Brand> findByStatus(String status);
}

