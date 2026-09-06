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

import com.medbill.entity.SalesExchange;
import com.medbill.service.SalesService;

@RestController
@RequestMapping("/api/sales/exchanges")
@CrossOrigin(origins = "http://localhost:4200")
public class SalesExchangeController {

    private final SalesService salesService;

    public SalesExchangeController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<SalesExchange>> getAllExchanges(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.getAllExchanges(companyId));
    }

    @PostMapping
    public ResponseEntity<SalesExchange> createExchange(
            @RequestBody SalesExchange entity,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.createExchange(entity, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesExchange> updateExchange(
            @PathVariable Long id,
            @RequestBody SalesExchange entity) {
        return ResponseEntity.ok(salesService.updateExchange(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExchange(@PathVariable Long id) {
        salesService.deleteExchange(id);
        return ResponseEntity.noContent().build();
    }
}
