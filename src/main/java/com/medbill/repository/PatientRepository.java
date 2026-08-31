package com.medbill.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medbill.entity.Company;
import com.medbill.entity.Patient;
import com.medbill.entity.PatientStatus;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // =====================================================
    // SEARCH - COMPANY WISE
    // =====================================================

    List<Patient> findByCompanyAndNameContainingIgnoreCase(
            Company company,
            String name
    );

    List<Patient> findByCompanyAndMobileContaining(
            Company company,
            String mobile
    );

    // =====================================================
    // STATUS - COMPANY WISE
    // =====================================================

    List<Patient> findByCompanyAndStatus(
            Company company,
            PatientStatus status
    );

    List<Patient> findByCompanyAndStatusOrderByCreatedDateDesc(
            Company company,
            PatientStatus status
    );

    List<Patient> findByStatus(
            PatientStatus status
    );

    // =====================================================
    // ALL PATIENTS - COMPANY WISE
    // =====================================================

    List<Patient> findByCompany(Company company);

    // =====================================================
    // SINGLE PATIENT - COMPANY WISE
    // =====================================================

    Optional<Patient> findByIdAndCompany(
            Long id,
            Company company
    );

    // =====================================================
    // COUNT - COMPANY WISE
    // =====================================================

    long countByCompany(Company company);

    long countByCompanyAndStatus(
            Company company,
            PatientStatus status
    );
}