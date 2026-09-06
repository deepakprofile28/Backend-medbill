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

import com.medbill.entity.SalesOrder;
import com.medbill.service.SalesService;

@RestController
@RequestMapping("/api/sales/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class SalesOrderController {

    private final SalesService salesService;

    public SalesOrderController(SalesService salesService) {
        this.salesService = salesService;
    }

    @GetMapping
    public ResponseEntity<List<SalesOrder>> getAllOrders(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.getAllOrders(companyId));
    }

    @PostMapping
    public ResponseEntity<SalesOrder> createOrder(
            @RequestBody SalesOrder entity,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(salesService.createOrder(entity, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesOrder> updateOrder(
            @PathVariable Long id,
            @RequestBody SalesOrder entity) {
        return ResponseEntity.ok(salesService.updateOrder(id, entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        salesService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
