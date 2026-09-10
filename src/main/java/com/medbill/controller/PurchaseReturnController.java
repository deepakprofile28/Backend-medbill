package com.medbill.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medbill.entity.PurchaseReturn;
import com.medbill.service.PurchaseReturnService;

@RestController
@RequestMapping("/api/purchase-returns")
@CrossOrigin(origins = "http://localhost:4200")
public class PurchaseReturnController {

    private final PurchaseReturnService purchaseReturnService;

    public PurchaseReturnController(PurchaseReturnService purchaseReturnService) {
        this.purchaseReturnService = purchaseReturnService;
    }

    @GetMapping
    public ResponseEntity<List<PurchaseReturn>> getAllReturns(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(purchaseReturnService.getAllReturns(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseReturn> getReturnById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseReturnService.getReturnById(id));
    }

    @PostMapping
    public ResponseEntity<PurchaseReturn> createPurchaseReturn(@RequestBody PurchaseReturn purchaseReturn) {
        return ResponseEntity.ok(purchaseReturnService.createPurchaseReturn(purchaseReturn));
    }
}
