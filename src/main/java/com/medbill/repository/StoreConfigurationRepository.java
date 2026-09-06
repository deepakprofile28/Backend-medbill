package com.medbill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.StoreConfiguration;

@Repository
public interface StoreConfigurationRepository extends JpaRepository<StoreConfiguration, Long> {

    Optional<StoreConfiguration> findFirstByCompanyId(Long companyId);

    Optional<StoreConfiguration> findFirstByOrderByIdAsc();
}

