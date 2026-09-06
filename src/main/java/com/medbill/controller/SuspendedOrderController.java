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

import com.medbill.entity.SuspendedOrder;
import com.medbill.service.SalesService;

@RestController
@RequestMapping("/api/sales/suspended")
@CrossOrigin(origins = "http://localhost:4200")
public class SuspendedOrderController {

    private final SalesService salesService;

    public SuspendedOrderController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<SuspendedOrder>> getAllSuspended(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.getAllSuspended(companyId));
    }

    @PostMapping
    public ResponseEntity<SuspendedOrder> createSuspended(
            @RequestBody SuspendedOrder entity,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.createSuspended(entity, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SuspendedOrder> updateSuspended(
            @PathVariable Long id,
            @RequestBody SuspendedOrder entity) {
        return ResponseEntity.ok(salesService.updateSuspended(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuspended(@PathVariable Long id) {
        salesService.deleteSuspended(id);
        return ResponseEntity.noContent().build();
    }
}
