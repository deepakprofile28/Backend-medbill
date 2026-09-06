package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.TaxConfiguration;

@Repository
public interface TaxConfigurationRepository extends JpaRepository<TaxConfiguration, Long> {

    List<TaxConfiguration> findByCompanyIdOrderByIdAsc(Long companyId);

    List<TaxConfiguration> findAllByOrderByIdAsc();

    List<TaxConfiguration> findByStatusOrderByIdAsc(String status);
}

