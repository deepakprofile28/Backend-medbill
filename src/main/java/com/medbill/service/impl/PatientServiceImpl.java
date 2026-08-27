package com.medbill.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Company;
import com.medbill.entity.Patient;
import com.medbill.entity.PatientStatus;
import com.medbill.entity.User;
import com.medbill.repository.PatientRepository;
import com.medbill.repository.UserRepository;
import com.medbill.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            UserRepository userRepository) {

        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    // =========================================================
    // GET LOGGED-IN USER
    // =========================================================

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User is not authenticated"
            );
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Logged-in user not found"
                        )
                );
    }

    // =========================================================
    // GET LOGGED-IN COMPANY
    // =========================================================

    private Company getLoggedInCompany() {

        User user = getLoggedInUser();

        if (user.getCompany() == null) {

            throw new RuntimeException(
                    "User is not assigned to any company"
            );
        }

        return user.getCompany();
    }

    // =========================================================
    // SAVE PATIENT
    // =========================================================

    @Override
    public Patient savePatient(Patient patient) {

        Company company = getLoggedInCompany();

        // Automatically assign logged-in user's company
        patient.setCompany(company);

        if (patient.getId() == null) {

            patient.setCreatedDate(
                    LocalDateTime.now()
            );
        }

        patient.setStatus(PatientStatus.APPROVED);

        return patientRepository.save(patient);
    }

    // =========================================================
    // GET ALL APPROVED PATIENTS - COMPANY WISE
    // =========================================================

    @Override
    public List<Patient> getAllPatients() {

        Company company = getLoggedInCompany();

        return patientRepository.findByCompanyAndStatus(
                company,
                PatientStatus.APPROVED
        );
    }

    // =========================================================
    // GET PATIENT BY ID - COMPANY WISE
    // =========================================================

    @Override
    public Patient getPatientById(Long id) {

        Company company = getLoggedInCompany();

        return patientRepository
                .findByIdAndCompany(id, company)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Patient not found with ID: " + id
                        )
                );
    }

    // =========================================================
    // UPDATE PATIENT - COMPANY WISE
    // =========================================================

    @Override
    public Patient updatePatient(
            Long id,
            Patient patient) {

        Company company = getLoggedInCompany();

        Patient existingPatient =
                patientRepository
                        .findByIdAndCompany(id, company)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found with ID: " + id
                                )
                        );

        // ================= PERSONAL DETAILS =================

        existingPatient.setName(
                patient.getName()
        );

        existingPatient.setMobile(
                patient.getMobile()
        );

        existingPatient.setEmail(
                patient.getEmail()
        );

        existingPatient.setDob(
                patient.getDob()
        );

        existingPatient.setGender(
                patient.getGender()
        );

        existingPatient.setBloodGroup(
                patient.getBloodGroup()
        );

        existingPatient.setMaritalStatus(
                patient.getMaritalStatus()
        );

        existingPatient.setOccupation(
                patient.getOccupation()
        );

        existingPatient.setAadhaar(
                patient.getAadhaar()
        );

        existingPatient.setPan(
                patient.getPan()
        );

        existingPatient.setEmergencyContact(
                patient.getEmergencyContact()
        );

        existingPatient.setEmergencyName(
                patient.getEmergencyName()
        );

        // ================= ADDRESS DETAILS =================

        existingPatient.setAddress1(
                patient.getAddress1()
        );

        existingPatient.setAddress2(
                patient.getAddress2()
        );

        existingPatient.setDistrict(
                patient.getDistrict()
        );

        existingPatient.setCity(
                patient.getCity()
        );

        existingPatient.setState(
                patient.getState()
        );

        existingPatient.setCountry(
                patient.getCountry()
        );

        existingPatient.setPincode(
                patient.getPincode()
        );

        // ================= MEDICAL DETAILS =================

        existingPatient.setMedicalHistory(
                patient.getMedicalHistory()
        );

        existingPatient.setCurrentMedication(
                patient.getCurrentMedication()
        );

        existingPatient.setAllergies(
                patient.getAllergies()
        );

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

        existingPatient.setStatus(
                PatientStatus.APPROVED
        );

        // IMPORTANT:
        // Keep existing company.
        existingPatient.setCompany(company);

        return patientRepository.save(
                existingPatient
        );
    }

    // =========================================================
    // DELETE PATIENT - COMPANY WISE
    // =========================================================

    @Override
    public void deletePatient(Long id) {

        Company company = getLoggedInCompany();

        Patient patient =
                patientRepository
                        .findByIdAndCompany(id, company)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found with ID: " + id
                                )
                        );

        patientRepository.delete(patient);
    }

    // =========================================================
    // SAVE DRAFT
    // =========================================================

    @Override
    public Patient saveDraft(Patient patient) {

        Company company = getLoggedInCompany();

        // Automatically assign logged-in user's company
        patient.setCompany(company);

        if (patient.getId() == null) {

            patient.setCreatedDate(
                    LocalDateTime.now()
            );
        }

        patient.setStatus(PatientStatus.DRAFT);

        return patientRepository.save(patient);
    }

    // =========================================================
    // GET ALL DRAFT PATIENTS - COMPANY WISE
    // =========================================================

    @Override
    public List<Patient> getDraftPatients() {

        Company company = getLoggedInCompany();

        return patientRepository
                .findByCompanyAndStatusOrderByCreatedDateDesc(
                        company,
                        PatientStatus.DRAFT
                );
    }

    // =========================================================
    // APPROVE DRAFT - COMPANY WISE
    // =========================================================

    @Override
    @Transactional
    public Patient approvePatient(Long id) {

        Company company = getLoggedInCompany();

        Patient patient =
                patientRepository
                        .findByIdAndCompany(id, company)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found with ID: " + id
                                )
                        );

        patient.setStatus(
                PatientStatus.APPROVED
        );

        return patientRepository.save(patient);
    }

    // =========================================================
    // DELETE DRAFT - COMPANY WISE
    // =========================================================

    @Override
    public void deleteDraft(Long id) {

        Company company = getLoggedInCompany();

        Patient patient =
                patientRepository
                        .findByIdAndCompany(id, company)
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

        patientRepository.delete(patient);
    }
}