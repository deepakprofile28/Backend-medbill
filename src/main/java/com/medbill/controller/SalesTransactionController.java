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

import com.medbill.entity.SalesTransaction;
import com.medbill.service.SalesService;

@RestController
@RequestMapping("/api/sales/transactions")
@CrossOrigin(origins = "http://localhost:4200")
public class SalesTransactionController {

    private final SalesService salesService;

    public SalesTransactionController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<SalesTransaction>> getAllTransactions(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.getAllTransactions(companyId));
    }

    @PostMapping
    public ResponseEntity<SalesTransaction> createTransaction(
            @RequestBody SalesTransaction entity,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.createTransaction(entity, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesTransaction> updateTransaction(
            @PathVariable Long id,
            @RequestBody SalesTransaction entity) {
        return ResponseEntity.ok(salesService.updateTransaction(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        salesService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}
