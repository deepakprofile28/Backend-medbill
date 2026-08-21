package com.medbill.controller;

import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medbill.entity.Patient;
import com.medbill.service.PatientService;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    // ================= CREATE PATIENT =================

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        Patient savedPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(savedPatient);
    }

    // ================= GET ALL PATIENTS =================

    @GetMapping
    public ResponseEntity<List<Patient>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    // ================= GET PATIENT BY ID =================

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    // ================= UPDATE PATIENT =================

    @PutMapping("/{id}")
    public ResponseEntity<Patient> updatePatient(
            @PathVariable Long id,
            @RequestBody Patient patient) {

        return ResponseEntity.ok(
                patientService.updatePatient(id, patient)
        );
    }

    // ================= DELETE PATIENT =================

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePatient(@PathVariable Long id) {

        patientService.deletePatient(id);

        return ResponseEntity.ok("Patient deleted successfully");
    }
    
 // ================= SEARCH PATIENT BY NAME =================

    @GetMapping("/search/name")
    public ResponseEntity<List<Patient>> searchPatientsByName(
            @RequestParam String name) {

        return ResponseEntity.ok(
                patientService.searchPatientsByName(name)
        );
    }

    // ================= SEARCH PATIENT BY MOBILE =================

    @GetMapping("/search/mobile")
    public ResponseEntity<List<Patient>> searchPatientsByMobile(
            @RequestParam String mobile) {

        return ResponseEntity.ok(
                patientService.searchPatientsByMobile(mobile)
        );
    }
}