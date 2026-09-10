package com.medbill.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

@Entity
@Table(name = "purchase_returns")
@JsonIgnoreProperties(ignoreUnknown = true)
public class PurchaseReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "return_no", nullable = false, length = 50)
    private String returnNo;

    @Column(name = "purchase_no", length = 50)
    private String purchaseNo;

    @Column(name = "supplier_name", nullable = false, length = 150)
    private String supplierName;

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "warehouse_name", length = 100)
    private String warehouseName = "Main Warehouse";

    @Column(name = "total_amount")
    private Double totalAmount = 0.0;

    @Column(name = "return_date")
    private LocalDateTime returnDate;

    @Column(length = 200)
    private String reason = "Damaged / Expired";

    @Column(length = 30)
    private String status = "Completed";

    @Column(name = "company_id")
    private Long companyId = 1L;

    @OneToMany(mappedBy = "purchaseReturn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PurchaseReturnItem> items = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (this.returnDate == null) {
            this.returnDate = LocalDateTime.now();
        }
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "Completed";
        }
        if (this.companyId == null) {
            this.companyId = 1L;
        }
        if (this.returnNo == null || this.returnNo.trim().isEmpty()) {
            this.returnNo = "PRTN-" + (System.currentTimeMillis() % 1000000);
        }
    }

    public PurchaseReturn() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getReturnNo() { return returnNo; }
    public void setReturnNo(String returnNo) { this.returnNo = returnNo; }
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
    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public List<PurchaseReturnItem> getItems() { return items; }
    public void setItems(List<PurchaseReturnItem> items) {
        this.items = items != null ? items : new ArrayList<>();
        for (PurchaseReturnItem it : this.items) {
            it.setPurchaseReturn(this);
        }
    }
}
