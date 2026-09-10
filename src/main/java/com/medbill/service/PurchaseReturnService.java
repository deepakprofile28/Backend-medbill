package com.medbill.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.medbill.entity.Medicine;
import com.medbill.entity.PurchaseReturn;
import com.medbill.entity.PurchaseReturnItem;
import com.medbill.repository.MedicineRepository;
import com.medbill.repository.PurchaseReturnRepository;

@Service
@Transactional
public class PurchaseReturnService {

    private final PurchaseReturnRepository purchaseReturnRepository;
    private final MedicineRepository medicineRepository;

    public PurchaseReturnService(PurchaseReturnRepository purchaseReturnRepository, MedicineRepository medicineRepository) {
        this.purchaseReturnRepository = purchaseReturnRepository;
        this.medicineRepository = medicineRepository;
    }

    public List<PurchaseReturn> getAllReturns(Long companyId) {
        if (companyId != null) {
            List<PurchaseReturn> list = purchaseReturnRepository.findByCompanyIdOrderByIdDesc(companyId);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return purchaseReturnRepository.findAllByOrderByIdDesc();
    }

    public PurchaseReturn getReturnById(Long id) {
        return purchaseReturnRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase Return not found with id: " + id));
    }

    public PurchaseReturn createPurchaseReturn(PurchaseReturn ret) {
        if (ret.getItems() != null) {
            for (PurchaseReturnItem item : ret.getItems()) {
                item.setPurchaseReturn(ret);
                if (item.getSubtotal() == null || item.getSubtotal() == 0.0) {
                    double uPrice = item.getUnitPrice() != null ? item.getUnitPrice() : 0.0;
                    int qty = item.getQuantity() != null ? item.getQuantity() : 0;
                    item.setSubtotal(uPrice * qty);
                }
            }
        }

        PurchaseReturn saved = purchaseReturnRepository.save(ret);

        // Deduct returned stock from medicine inventory
        if (ret.getItems() != null) {
            for (PurchaseReturnItem item : ret.getItems()) {
                if (item.getProductId() != null && item.getProductId() > 0) {
                    medicineRepository.findById(item.getProductId()).ifPresent(med -> {
                        int current = med.getStockQty() != null ? med.getStockQty() : 0;
                        int deductQty = item.getQuantity() != null ? item.getQuantity() : 0;
                        med.setStockQty(Math.max(0, current - deductQty));
                        medicineRepository.save(med);
                    });
                }
            }
        }

        return saved;
    }
}
