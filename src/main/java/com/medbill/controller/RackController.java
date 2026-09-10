package com.medbill.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medbill.entity.Rack;
import com.medbill.service.RackService;

@RestController
@RequestMapping("/api/racks")
@CrossOrigin(origins = "http://localhost:4200")
public class RackController {

    private final RackService rackService;

    public RackController(RackService rackService) {
        this.rackService = rackService;
    }

    @GetMapping
    public ResponseEntity<List<Rack>> getAllRacks(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(rackService.getAllRacks(companyId));
    }

    @GetMapping("/warehouse/{warehouseId}")
    public ResponseEntity<List<Rack>> getRacksByWarehouse(@PathVariable Long warehouseId) {
        return ResponseEntity.ok(rackService.getRacksByWarehouse(warehouseId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rack> getRackById(@PathVariable Long id) {
        return ResponseEntity.ok(rackService.getRackById(id));
    }

    @PostMapping
    public ResponseEntity<Rack> createRack(
            @RequestBody Rack rack,
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(rackService.createRack(rack, companyId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rack> updateRack(@PathVariable Long id, @RequestBody Rack rack) {
        return ResponseEntity.ok(rackService.updateRack(id, rack));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRack(@PathVariable Long id) {
        rackService.deleteRack(id);
        return ResponseEntity.noContent().build();
    }
}
