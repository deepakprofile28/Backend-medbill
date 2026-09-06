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

import com.medbill.entity.TaxConfiguration;
import com.medbill.service.TaxConfigurationService;

@RestController
@RequestMapping("/api/taxes")
@CrossOrigin(origins = "http://localhost:4200")
public class TaxConfigurationController {

    private final TaxConfigurationService taxService;

    public TaxConfigurationController(TaxConfigurationService taxService) {
        this.taxService = taxService;
    }

    @GetMapping
    public ResponseEntity<List<TaxConfiguration>> getAllTaxes(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        List<TaxConfiguration> taxes = taxService.getAllTaxes(companyId);
        return ResponseEntity.ok(taxes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaxConfiguration> getTaxById(@PathVariable Long id) {
        TaxConfiguration tax = taxService.getTaxById(id);
        return ResponseEntity.ok(tax);
    }

    @PostMapping
    public ResponseEntity<TaxConfiguration> createTax(
            @RequestBody TaxConfiguration tax,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        TaxConfiguration created = taxService.createTax(tax, companyId);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaxConfiguration> updateTax(
            @PathVariable Long id,
            @RequestBody TaxConfiguration tax) {
        TaxConfiguration updated = taxService.updateTax(id, tax);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTax(@PathVariable Long id) {
        taxService.deleteTax(id);
        return ResponseEntity.noContent().build();
    }

    // ==========================================
    // BACKEND TAX CALCULATION (INCLUSIVE & EXCLUSIVE)
    // ==========================================

    @PostMapping("/calculate")
    public ResponseEntity<com.medbill.dto.TaxCalculationResult> calculateItemTax(
            @RequestBody com.medbill.dto.TaxCalculationRequest request) {
        com.medbill.dto.TaxCalculationResult result = taxService.calculateItemTax(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/calculate-invoice")
    public ResponseEntity<com.medbill.dto.InvoiceTaxCalculationResult> calculateInvoiceTax(
            @RequestBody com.medbill.dto.InvoiceTaxCalculationRequest request) {
        com.medbill.dto.InvoiceTaxCalculationResult result = taxService.calculateInvoiceTax(request);
        return ResponseEntity.ok(result);
    }
}

