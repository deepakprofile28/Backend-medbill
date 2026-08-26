package com.medbill.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Patient;
import com.medbill.entity.PatientStatus;
import com.medbill.repository.PatientRepository;
import com.medbill.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    // =========================================================
    // SAVE PATIENT
    // =========================================================

    @Override
    public Patient savePatient(Patient patient) {

        if (patient.getId() == null) {
            patient.setCreatedDate(LocalDateTime.now());
        }

        patient.setStatus(PatientStatus.APPROVED);

        return patientRepository.save(patient);
    }

    // =========================================================
    // GET ALL APPROVED PATIENTS
    // =========================================================

    @Override
    public List<Patient> getAllPatients() {

        return patientRepository.findByStatus(
                PatientStatus.APPROVED
        );
    }

    // =========================================================
    // GET PATIENT BY ID
    // =========================================================

    @Override
    public Patient getPatientById(Long id) {

        return patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: " + id
                        )
                );
    }

    // =========================================================
    // UPDATE PATIENT
    // =========================================================

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: " + id
                        )
                );

        // ================= PERSONAL DETAILS =================

        existingPatient.setName(patient.getName());
        existingPatient.setMobile(patient.getMobile());
        existingPatient.setEmail(patient.getEmail());
        existingPatient.setDob(patient.getDob());
        existingPatient.setGender(patient.getGender());
        existingPatient.setBloodGroup(patient.getBloodGroup());
        existingPatient.setMaritalStatus(patient.getMaritalStatus());
        existingPatient.setOccupation(patient.getOccupation());
        existingPatient.setAadhaar(patient.getAadhaar());
        existingPatient.setPan(patient.getPan());
        existingPatient.setEmergencyContact(patient.getEmergencyContact());
        existingPatient.setEmergencyName(patient.getEmergencyName());

        // ================= ADDRESS DETAILS =================

        existingPatient.setAddress1(patient.getAddress1());
        existingPatient.setAddress2(patient.getAddress2());
        existingPatient.setDistrict(patient.getDistrict());
        existingPatient.setCity(patient.getCity());
        existingPatient.setState(patient.getState());
        existingPatient.setCountry(patient.getCountry());
        existingPatient.setPincode(patient.getPincode());

        // ================= MEDICAL DETAILS =================

        existingPatient.setMedicalHistory(patient.getMedicalHistory());
        existingPatient.setCurrentMedication(patient.getCurrentMedication());
        existingPatient.setAllergies(patient.getAllergies());

        // ================= INSURANCE DETAILS =================

        existingPatient.setInsuranceProvider(
                patient.getInsuranceProvider()
        );

        existingPatient.setPolicyNumber(
                patient.getPolicyNumber()
        );

        existingPatient.setPolicyHolderName(
                patient.getPolicyHolderName()
        );

        // ================= STATUS =================

        existingPatient.setStatus(PatientStatus.APPROVED);

        return patientRepository.save(existingPatient);
    }

    // =========================================================
    // DELETE PATIENT
    // =========================================================

    @Override
    public void deletePatient(Long id) {

        if (!patientRepository.existsById(id)) {
            throw new RuntimeException(
                    "Patient not found with ID: " + id
            );
        }

        patientRepository.deleteById(id);
    }

    // =========================================================
    // SAVE DRAFT
    // =========================================================

    @Override
    public Patient saveDraft(Patient patient) {

        if (patient.getId() == null) {
            patient.setCreatedDate(LocalDateTime.now());
        }

        patient.setStatus(PatientStatus.DRAFT);

        return patientRepository.save(patient);
    }

    // =========================================================
    // GET ALL DRAFT PATIENTS
    // =========================================================

    @Override
    public List<Patient> getDraftPatients() {

        return patientRepository
                .findByStatusOrderByCreatedDateDesc(
                        PatientStatus.DRAFT
                );
    }

    // =========================================================
    // APPROVE DRAFT
    // =========================================================

    @Override
    @Transactional
    public Patient approvePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: " + id
                        )
                );

        patient.setStatus(PatientStatus.APPROVED);

        return patientRepository.save(patient);
    }

    // =========================================================
    // DELETE DRAFT
    // =========================================================

    @Override
    public void deleteDraft(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: " + id
                        )
                );

        if (patient.getStatus() != PatientStatus.DRAFT) {
            throw new RuntimeException(
                    "Only draft patients can be removed"
            );
        }

        patientRepository.deleteById(id);
    }
}