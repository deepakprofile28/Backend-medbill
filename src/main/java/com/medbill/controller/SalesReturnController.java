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

import com.medbill.entity.SalesReturn;
import com.medbill.service.SalesService;

@RestController
@RequestMapping("/api/sales/returns")
@CrossOrigin(origins = "http://localhost:4200")
public class SalesReturnController {

    private final SalesService salesService;

    public SalesReturnController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<SalesReturn>> getAllReturns(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.getAllReturns(companyId));
    }

    @PostMapping
    public ResponseEntity<SalesReturn> createReturn(
            @RequestBody SalesReturn entity,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.createReturn(entity, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesReturn> updateReturn(
            @PathVariable Long id,
            @RequestBody SalesReturn entity) {
        return ResponseEntity.ok(salesService.updateReturn(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReturn(@PathVariable Long id) {
        salesService.deleteReturn(id);
        return ResponseEntity.noContent().build();
    }
}

