package com.medbill.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.medbill.entity.Purchase;
import com.medbill.service.PurchaseService;

@RestController
@RequestMapping("/api/purchases")
@CrossOrigin(origins = "http://localhost:4200")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping
    public ResponseEntity<List<Purchase>> getAllPurchases(
            @RequestParam(name = "companyId", required = false) Long companyId) {
        return ResponseEntity.ok(purchaseService.getAllPurchases(companyId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Purchase> getPurchaseById(@PathVariable Long id) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(id));
    }

    @PostMapping
    public ResponseEntity<Purchase> createPurchase(@RequestBody Purchase purchase) {
        return ResponseEntity.ok(purchaseService.createPurchase(purchase));
    }
}
