package com.medbill.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "purchases")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_no", nullable = false, length = 50)
    private String purchaseNo;

    @Column(name = "supplier_name", nullable = false, length = 150)
    private String supplierName;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "warehouse_name", length = 100)
    private String warehouseName = "Main Warehouse";

    @Column(name = "total_amount")
    private Double totalAmount = 0.0;

    @Column(name = "purchase_date")
    private LocalDateTime purchaseDate;

    @Column(name = "company_id")
    private Long companyId = 1L;

    @Column(name = "status", length = 30)
    private String status = "Completed";

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PurchaseItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.purchaseDate == null) {
            this.purchaseDate = LocalDateTime.now();
        }
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "Completed";
        }
        if (this.companyId == null) {
            this.companyId = 1L;
        }
        if (this.purchaseNo == null || this.purchaseNo.trim().isEmpty()) {
            this.purchaseNo = "PUR-" + (System.currentTimeMillis() % 1000000);
        }
    }

    public Purchase() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPurchaseNo() { return purchaseNo; }
    public void setPurchaseNo(String purchaseNo) { this.purchaseNo = purchaseNo; }
    public String getSupplierName() { return supplierName; }
    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getWarehouseName() { return warehouseName; }
    public void setWarehouseName(String warehouseName) { this.warehouseName = warehouseName; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDateTime purchaseDate) { this.purchaseDate = purchaseDate; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<PurchaseItem> getItems() { return items; }
    public void setItems(List<PurchaseItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        for (PurchaseItem it : this.items) {
            it.setPurchase(this);
        }
    }
}
