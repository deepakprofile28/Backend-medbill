package com.medbill.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medbill.entity.Patient;
import com.medbill.service.PatientService;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // ==========================================
    // GET ALL APPROVED PATIENTS
    // ==========================================

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {

        return ResponseEntity.ok(
                patientService.getAllPatients()
        );
    }

    // ==========================================
    // GET PATIENT BY ID
    // ==========================================

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(
            @PathVariable Long id) {

        try {
            Patient patient = patientService.getPatientById(id);

            return ResponseEntity.ok(patient);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // SAVE APPROVED PATIENT
    // ==========================================

    @PostMapping
    public ResponseEntity<Patient> savePatient(
            @RequestBody Patient patient) {

        Patient savedPatient =
                patientService.savePatient(patient);

        return ResponseEntity.ok(savedPatient);
    }

    // ==========================================
    // SAVE DRAFT
    // ==========================================

    @PostMapping("/draft")
    public ResponseEntity<Patient> saveDraft(
            @RequestBody Patient patient) {

        Patient savedDraft =
                patientService.saveDraft(patient);

        return ResponseEntity.ok(savedDraft);
    }

    // ==========================================
    // GET DRAFT PATIENTS
    // ==========================================

    @GetMapping("/drafts")
    public ResponseEntity<List<Patient>> getDraftPatients() {

        return ResponseEntity.ok(
                patientService.getDraftPatients()
        );
    }

    // ==========================================
    // APPROVE DRAFT
    // ==========================================

    @PutMapping("/{id}/approve")
    public ResponseEntity<Patient> approvePatient(
            @PathVariable Long id) {

        try {
            Patient approvedPatient =
                    patientService.approvePatient(id);

            return ResponseEntity.ok(approvedPatient);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // UPDATE PATIENT
    // ==========================================

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {

        try {
            Patient updatedPatient =
                    patientService.updatePatient(id, patient);

            return ResponseEntity.ok(updatedPatient);

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // DELETE PATIENT
    // ==========================================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(
            @PathVariable Long id) {

        try {
            patientService.deletePatient(id);

            return ResponseEntity.ok(
                    "Patient deleted successfully"
            );

        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // ==========================================
    // DELETE DRAFT
    // ==========================================

    @DeleteMapping("/{id}/draft")
    public ResponseEntity<String> deleteDraft(
            @PathVariable Long id) {

        try {
            patientService.deleteDraft(id);

            return ResponseEntity.ok(
                    "Draft deleted successfully"
            );

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}