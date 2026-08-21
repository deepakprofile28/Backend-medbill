package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medbill.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    List<Patient> findByNameContainingIgnoreCase(String name);

    List<Patient> findByMobileContaining(String mobile);

}