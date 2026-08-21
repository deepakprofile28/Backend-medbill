package com.medbill.service;

import java.util.List;

import com.medbill.entity.Patient;

public interface PatientService {

    Patient createPatient(Patient patient);

    List<Patient> getAllPatients();

    Patient getPatientById(Long id);

    Patient updatePatient(Long id, Patient patient);

    void deletePatient(Long id);

    List<Patient> searchPatientsByName(String name);

    List<Patient> searchPatientsByMobile(String mobile);
}