package com.medbill.service;

import java.util.List;

import com.medbill.entity.Patient;

public interface PatientService {

    // =====================================================
    // PATIENT
    // =====================================================

    Patient savePatient(Patient patient);

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient updatePatient(Long id, Patient patient);

    void deletePatient(Long id);

    // =====================================================
    // DRAFT
    // =====================================================

    Patient saveDraft(Patient patient);

    List<Patient> getDraftPatients();

    Patient approvePatient(Long id);

    void deleteDraft(Long id);
}