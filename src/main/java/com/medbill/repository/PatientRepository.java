package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medbill.entity.Patient;
import com.medbill.entity.PatientStatus;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    // ================= SEARCH =================

    List<Patient> findByNameContainingIgnoreCase(String name);

    List<Patient> findByMobileContaining(String mobile);


    // ================= STATUS =================

    // Get only approved patients
    List<Patient> findByStatus(PatientStatus status);

    // Get only draft patients
    List<Patient> findByStatusOrderByCreatedDateDesc(PatientStatus status);

}