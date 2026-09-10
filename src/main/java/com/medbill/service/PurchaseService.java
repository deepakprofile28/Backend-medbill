package com.medbill.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.medbill.entity.Medicine;
import com.medbill.entity.Purchase;
import com.medbill.entity.PurchaseItem;
import com.medbill.repository.MedicineRepository;
import com.medbill.repository.PurchaseRepository;

@Service
@Transactional
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final MedicineRepository medicineRepository;

    public PurchaseService(PurchaseRepository purchaseRepository, MedicineRepository medicineRepository) {
        this.purchaseRepository = purchaseRepository;
        this.medicineRepository = medicineRepository;
    }

    public List<Purchase> getAllPurchases(Long companyId) {
        if (companyId != null) {
            List<Purchase> list = purchaseRepository.findByCompanyIdOrderByIdDesc(companyId);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return purchaseRepository.findAllByOrderByIdDesc();
    }

    public Purchase getPurchaseById(Long id) {
        return purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found with id: " + id));
    }

    public Purchase createPurchase(Purchase purchase) {
        if (purchase.getItems() != null) {
            for (PurchaseItem item : purchase.getItems()) {
                item.setPurchase(purchase);
                if (item.getSubtotal() == null || item.getSubtotal() == 0.0) {
                    double uPrice = item.getUnitPrice() != null ? item.getUnitPrice() : 0.0;
                    int qty = item.getQuantity() != null ? item.getQuantity() : 0;
                    item.setSubtotal(uPrice * qty);
                }
            }
        }

        Purchase saved = purchaseRepository.save(purchase);

        // Update live medicine stock
        if (purchase.getItems() != null) {
            for (PurchaseItem item : purchase.getItems()) {
                if (item.getProductId() != null && item.getProductId() > 0) {
                    medicineRepository.findById(item.getProductId()).ifPresent(med -> {
                        int current = med.getStockQty() != null ? med.getStockQty() : 0;
                        int addQty = item.getQuantity() != null ? item.getQuantity() : 0;
                        med.setStockQty(current + addQty);
                        medicineRepository.save(med);
                    });
                }
            }
        }

        return saved;
    }
}
