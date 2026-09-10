package com.medbill.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medbill.entity.Supplier;
import com.medbill.service.SupplierService;

@RestController
@RequestMapping("/api/suppliers")
@CrossOrigin(origins = "http://localhost:4200")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping
    public ResponseEntity<List<Supplier>> getAllSuppliers(
            @RequestParam(name = "companyId", required = false) Long companyId,
            @RequestParam(name = "activeOnly", required = false, defaultValue = "false") boolean activeOnly) {
        List<Supplier> list = activeOnly
                ? supplierService.getActiveSuppliers(companyId)
                : supplierService.getAllSuppliers(companyId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supplier> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @PostMapping
    public ResponseEntity<Supplier> createSupplier(
            @RequestBody Supplier supplier,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(supplierService.createSupplier(supplier, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplier));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
