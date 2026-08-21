package com.medbill.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medbill.entity.Patient;
import com.medbill.repository.PatientRepository;
import com.medbill.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient createPatient(Patient patient) {
        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Patient not found with id: " + id));
    }

    @Override
    public Patient updatePatient(Long id, Patient patient) {

        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Patient not found with id: " + id));

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

        existingPatient.setAddress1(patient.getAddress1());
        existingPatient.setAddress2(patient.getAddress2());
        existingPatient.setDistrict(patient.getDistrict());
        existingPatient.setCity(patient.getCity());
        existingPatient.setState(patient.getState());
        existingPatient.setCountry(patient.getCountry());
        existingPatient.setPincode(patient.getPincode());

        existingPatient.setMedicalHistory(patient.getMedicalHistory());
        existingPatient.setCurrentMedication(patient.getCurrentMedication());
        existingPatient.setAllergies(patient.getAllergies());

        existingPatient.setInsuranceProvider(patient.getInsuranceProvider());
        existingPatient.setPolicyNumber(patient.getPolicyNumber());
        existingPatient.setPolicyHolderName(patient.getPolicyHolderName());

        return patientRepository.save(existingPatient);
    }
    @Override
    public void deletePatient(Long id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() ->
                    new RuntimeException("Patient not found with id: " + id));

        patientRepository.delete(patient);
    }

    // ================= SEARCH PATIENT BY NAME =================

    @Override
    public List<Patient> searchPatientsByName(String name) {
        return patientRepository.findByNameContainingIgnoreCase(name);
    }

    // ================= SEARCH PATIENT BY MOBILE =================

    @Override
    public List<Patient> searchPatientsByMobile(String mobile) {
        return patientRepository.findByMobileContaining(mobile);
    }

    }