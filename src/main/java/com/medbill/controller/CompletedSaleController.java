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

import com.medbill.entity.CompletedSale;
import com.medbill.service.SalesService;

@RestController
@RequestMapping("/api/sales/completed")
@CrossOrigin(origins = "http://localhost:4200")
public class CompletedSaleController {

    private final SalesService salesService;

    public CompletedSaleController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<CompletedSale>> getAllCompleted(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.getAllCompleted(companyId));
    }

    @PostMapping
    public ResponseEntity<CompletedSale> createCompleted(
            @RequestBody CompletedSale entity,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.createCompleted(entity, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompletedSale> updateCompleted(
            @PathVariable Long id,
            @RequestBody CompletedSale entity) {
        return ResponseEntity.ok(salesService.updateCompleted(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompleted(@PathVariable Long id) {
        salesService.deleteCompleted(id);
        return ResponseEntity.noContent().build();
    }
}
