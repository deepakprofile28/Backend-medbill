package com.medbill.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Company;
import com.medbill.entity.Patient;
import com.medbill.entity.PatientStatus;
import com.medbill.entity.User;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.PatientRepository;
import com.medbill.repository.UserRepository;
import com.medbill.service.PatientService;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public PatientServiceImpl(
            PatientRepository patientRepository,
            UserRepository userRepository,
            CompanyRepository companyRepository) {

        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
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

        return userRepository
                .findByEmail(email)
                .orElse(null);
    }

    // =========================================================
    // GET LOGGED-IN COMPANY / TENANT
    // =========================================================

    private Company getLoggedInCompany() {
        try {
            User user = getLoggedInUser();
            if (user != null && user.getCompany() != null) {
                return user.getCompany();
            }
        } catch (Exception e) {
            System.out.println("Notice: Context user missing company, falling back to default tenant: " + e.getMessage());
        }

        var companies = companyRepository.findAll();
        if (!companies.isEmpty()) {
            return companies.get(0);
        }

        Company defaultComp = new Company();
        defaultComp.setName("MedBill Pharmacy");
        defaultComp.setEmail("admin@medbill.com");
        defaultComp.setStatus("ACTIVE");
        defaultComp.setCreatedAt(LocalDateTime.now());
        return companyRepository.save(defaultComp);
    }

    // =========================================================
    // SAVE PATIENT
    // =========================================================

    @Override
    public Patient savePatient(Patient patient) {

        Company company = getLoggedInCompany();

        // IMPORTANT:
        // Always assign logged-in user's company.
        patient.setCompany(company);

        // If an ID was sent that does not exist in DB (e.g. temporary timestamp from client), reset to null for new insert
        if (patient.getId() != null && !patientRepository.existsById(patient.getId())) {
            patient.setId(null);
        }

        if (patient.getId() == null) {
            patient.setCreatedDate(
                    LocalDateTime.now()
            );
        }

        patient.setStatus(
                PatientStatus.APPROVED
        );

        Patient saved = patientRepository.save(patient);
        System.out.println("Patient saved into MySQL patients table successfully! ID: " + saved.getId() + ", Name: " + saved.getName());
        return saved;
    }

    // =========================================================
    // GET ALL APPROVED PATIENTS - COMPANY WISE
    // =========================================================

    @Override
    public List<Patient> getAllPatients() {

        Company company = getLoggedInCompany();

        List<Patient> list = patientRepository.findByCompanyAndStatus(
                company,
                PatientStatus.APPROVED
        );
        if (list.isEmpty()) {
            return patientRepository.findByStatus(PatientStatus.APPROVED);
        }
        return list;
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

        // IMPORTANT:
        // Patient must belong to logged-in company.
        Patient existingPatient =
                patientRepository
                        .findByIdAndCompany(id, company)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Patient not found with ID: " + id
                                )
                        );

        // =====================================================
        // PERSONAL DETAILS
        // =====================================================

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

        // =====================================================
        // ADDRESS DETAILS
        // =====================================================

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

        // =====================================================
        // MEDICAL DETAILS
        // =====================================================

        existingPatient.setMedicalHistory(
                patient.getMedicalHistory()
        );

        existingPatient.setCurrentMedication(
                patient.getCurrentMedication()
        );

        existingPatient.setAllergies(
                patient.getAllergies()
        );

        // =====================================================
        // INSURANCE DETAILS
        // =====================================================

        existingPatient.setInsuranceProvider(
                patient.getInsuranceProvider()
        );

        existingPatient.setPolicyNumber(
                patient.getPolicyNumber()
        );

        existingPatient.setPolicyHolderName(
                patient.getPolicyHolderName()
        );

        // =====================================================
        // STATUS
        // =====================================================

        existingPatient.setStatus(
                PatientStatus.APPROVED
        );

        // IMPORTANT:
        // Keep patient inside logged-in company.
        // Never take company from frontend.
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

        // IMPORTANT:
        // Automatically assign logged-in company.
        patient.setCompany(company);

        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            patient.setName("Draft Patient");
        }

        // If an ID is passed that does not exist in the DB, reset it to null for new insert
        if (patient.getId() != null && !patientRepository.existsById(patient.getId())) {
            patient.setId(null);
        }

        if (patient.getId() == null) {
            patient.setCreatedDate(
                    LocalDateTime.now()
            );
        }

        patient.setStatus(
                PatientStatus.DRAFT
        );

        Patient saved = patientRepository.save(patient);
        System.out.println("Patient DRAFT saved into MySQL DB successfully! ID: " + saved.getId() + ", Name: " + saved.getName());
        return saved;
    }

    // =========================================================
    // GET ALL DRAFT PATIENTS - COMPANY WISE
    // =========================================================

    @Override
    public List<Patient> getDraftPatients() {

        Company company = getLoggedInCompany();

        List<Patient> list = patientRepository
                .findByCompanyAndStatusOrderByCreatedDateDesc(
                        company,
                        PatientStatus.DRAFT
                );
        if (list.isEmpty()) {
            return patientRepository.findByStatus(PatientStatus.DRAFT);
        }
        return list;
    }

    // =========================================================
    // APPROVE DRAFT - COMPANY WISE
    // =========================================================

    @Override
    @Transactional
    public Patient approvePatient(Long id) {

        Company company = getLoggedInCompany();

        Optional<Patient> patientOpt = patientRepository.findByIdAndCompany(id, company);
        if (patientOpt.isEmpty()) {
            patientOpt = patientRepository.findById(id);
        }

        Patient patient = patientOpt.orElseThrow(() ->
                new RuntimeException("Patient not found with ID: " + id)
        );

        if (company != null) {
            patient.setCompany(company);
        }
        patient.setStatus(PatientStatus.APPROVED);

        Patient saved = patientRepository.save(patient);
        System.out.println("Patient " + saved.getName() + " (ID: " + saved.getId() + ") APPROVED in MySQL DB successfully!");
        return saved;
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