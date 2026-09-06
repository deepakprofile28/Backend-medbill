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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medbill.entity.Medicine;
import com.medbill.service.MedicineService;

@RestController
@RequestMapping("/api/medicines")
@CrossOrigin(origins = "http://localhost:4200")
public class MedicineController {

    private final MedicineService medicineService;

    public MedicineController(MedicineService medicineService) {
        this.medicineService = medicineService;
    }

    // =========================================================
    // GET ALL MEDICINES (Optionally Filtered by Company)
    // =========================================================
    @GetMapping
    public ResponseEntity<List<Medicine>> getAllMedicines(
            @RequestParam(required = false) Long companyId) {
        return ResponseEntity.ok(medicineService.getAllMedicines(companyId));
    }

    // =========================================================
    // GET MEDICINE BY ID
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<Medicine> getMedicineById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(medicineService.getMedicineById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // ADD NEW MEDICINE
    // =========================================================
    @PostMapping
    public ResponseEntity<Medicine> addMedicine(
            @RequestBody Medicine medicine,
            @RequestParam(required = false) Long companyId) {
        
        Long targetCompanyId = companyId;
        if (targetCompanyId == null && medicine.getCompany() != null) {
            targetCompanyId = medicine.getCompany().getId();
        }
        Medicine saved = medicineService.saveMedicine(medicine, targetCompanyId);
        return ResponseEntity.ok(saved);
    }

    // =========================================================
    // UPDATE MEDICINE
    // =========================================================
    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(
            @PathVariable Long id,
            @RequestBody Medicine medicine) {
        try {
            return ResponseEntity.ok(medicineService.updateMedicine(id, medicine));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // =========================================================
    // DELETE MEDICINE
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicine(@PathVariable Long id) {
        try {
            medicineService.deleteMedicine(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

