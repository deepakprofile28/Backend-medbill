package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByCompanyId(Long companyId);

    List<Doctor> findByCompanyIdOrderByNameAsc(Long companyId);

    List<Doctor> findByStatus(String status);

    List<Doctor> findByCompanyIdAndStatus(Long companyId, String status);

    List<Doctor> findByCompanyIdAndSpecializationIgnoreCase(Long companyId, String specialization);

    boolean existsByNameIgnoreCaseAndCompanyId(String name, Long companyId);

    boolean existsByNameIgnoreCaseAndCompanyIdAndIdNot(String name, Long companyId, Long id);
}

