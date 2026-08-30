package com.medbill.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medbill.entity.Company;

public interface CompanyRepository
        extends JpaRepository<Company, Long> {

    Optional<Company> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Company> findByMobile(String mobile);

    boolean existsByMobile(String mobile);

    Optional<Company> findTopByMobileOrderByIdDesc(String mobile);

    Optional<Company> findByCountryCodeAndMobile(
            String countryCode,
            String mobile
    );

    Optional<Company> findTopByCountryCodeAndMobileOrderByIdDesc(
            String countryCode,
            String mobile
    );
}