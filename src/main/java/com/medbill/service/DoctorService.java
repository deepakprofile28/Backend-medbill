package com.medbill.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Company;
import com.medbill.entity.Doctor;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.DoctorRepository;

@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final CompanyRepository companyRepository;

    public DoctorService(DoctorRepository doctorRepository, CompanyRepository companyRepository) {
        this.doctorRepository = doctorRepository;
        this.companyRepository = companyRepository;
    }

    public List<Doctor> getAllDoctors(Long companyId) {
        if (companyId != null) {
            List<Doctor> doctors = doctorRepository.findByCompanyIdOrderByNameAsc(companyId);
            if (!doctors.isEmpty()) {
                return doctors;
            }
        }
        return doctorRepository.findAll();
    }

    public List<Doctor> getActiveDoctors(Long companyId) {
        if (companyId != null) {
            return doctorRepository.findByCompanyIdAndStatus(companyId, "ACTIVE");
        }
        return doctorRepository.findByStatus("ACTIVE");
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Doctor not found with ID: " + id));
    }

    public Doctor createDoctor(Doctor doctor, Long companyId) {
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new RuntimeException("Doctor name is required");
        }
        if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
            throw new RuntimeException("Specialization is required");
        }
        if (doctor.getPhone() == null || doctor.getPhone().trim().isEmpty()) {
            throw new RuntimeException("Phone number is required");
        }

        if (companyId != null) {
            Company company = companyRepository.findById(companyId)
                    .orElse(null);
            doctor.setCompany(company);
        } else if (doctor.getCompany() == null) {
            companyRepository.findAll().stream().findFirst().ifPresent(doctor::setCompany);
        }

        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existing = getDoctorById(id);

        if (updatedDoctor.getName() != null && !updatedDoctor.getName().trim().isEmpty()) {
            existing.setName(updatedDoctor.getName().trim());
        }
        if (updatedDoctor.getSpecialization() != null && !updatedDoctor.getSpecialization().trim().isEmpty()) {
            existing.setSpecialization(updatedDoctor.getSpecialization().trim());
        }
        if (updatedDoctor.getQualification() != null) {
            existing.setQualification(updatedDoctor.getQualification().trim());
        }
        if (updatedDoctor.getRegistrationNumber() != null) {
            existing.setRegistrationNumber(updatedDoctor.getRegistrationNumber().trim());
        }
        if (updatedDoctor.getPhone() != null && !updatedDoctor.getPhone().trim().isEmpty()) {
            existing.setPhone(updatedDoctor.getPhone().trim());
        }
        if (updatedDoctor.getEmail() != null) {
            existing.setEmail(updatedDoctor.getEmail().trim().toLowerCase());
        }
        if (updatedDoctor.getDepartment() != null) {
            existing.setDepartment(updatedDoctor.getDepartment().trim());
        }
        if (updatedDoctor.getRoomNumber() != null) {
            existing.setRoomNumber(updatedDoctor.getRoomNumber().trim());
        }
        if (updatedDoctor.getConsultationFee() != null) {
            existing.setConsultationFee(updatedDoctor.getConsultationFee());
        }
        if (updatedDoctor.getExperienceYears() != null) {
            existing.setExperienceYears(updatedDoctor.getExperienceYears());
        }
        if (updatedDoctor.getAvailableDays() != null) {
            existing.setAvailableDays(updatedDoctor.getAvailableDays().trim());
        }
        if (updatedDoctor.getAvailableTimeStart() != null) {
            existing.setAvailableTimeStart(updatedDoctor.getAvailableTimeStart().trim());
        }
        if (updatedDoctor.getAvailableTimeEnd() != null) {
            existing.setAvailableTimeEnd(updatedDoctor.getAvailableTimeEnd().trim());
        }
        if (updatedDoctor.getStatus() != null && !updatedDoctor.getStatus().trim().isEmpty()) {
            existing.setStatus(updatedDoctor.getStatus().trim().toUpperCase());
        }
        if (updatedDoctor.getNotes() != null) {
            existing.setNotes(updatedDoctor.getNotes().trim());
        }

        return doctorRepository.save(existing);
    }

    public void deleteDoctor(Long id) {
        Doctor doctor = getDoctorById(id);
        doctorRepository.delete(doctor);
    }
}

