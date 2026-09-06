package com.medbill.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_exchanges")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SalesExchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exchange_no", nullable = false, length = 50, unique = true)
    private String exchangeNo;

    @Column(name = "original_bill_no", nullable = false, length = 50)
    private String originalBillNo;

    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;

    @Column(name = "returned_item", nullable = false, length = 255)
    private String returnedItem;

    @Column(name = "new_item", nullable = false, length = 255)
    private String newItem;

    @Column(name = "price_difference")
    private Double priceDifference = 0.0;

    @Column(name = "exchange_date", nullable = false)
    private LocalDateTime exchangeDate;

    @Column(name = "exchange_status", length = 50)
    private String exchangeStatus = "COMPLETED";

    @Column(name = "status", length = 20)
    private String status = "ACTIVE";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.exchangeDate == null) {
            this.exchangeDate = LocalDateTime.now();
        }
        if (this.status == null || this.status.trim().isEmpty()) {
            this.status = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public SalesExchange() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExchangeNo() { return exchangeNo; }
    public void setExchangeNo(String exchangeNo) { this.exchangeNo = exchangeNo; }

    public String getOriginalBillNo() { return originalBillNo; }
    public void setOriginalBillNo(String originalBillNo) { this.originalBillNo = originalBillNo; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getReturnedItem() { return returnedItem; }
    public void setReturnedItem(String returnedItem) { this.returnedItem = returnedItem; }

    public String getNewItem() { return newItem; }
    public void setNewItem(String newItem) { this.newItem = newItem; }

    public Double getPriceDifference() { return priceDifference; }
    public void setPriceDifference(Double priceDifference) { this.priceDifference = priceDifference; }

    public LocalDateTime getExchangeDate() { return exchangeDate; }
    public void setExchangeDate(LocalDateTime exchangeDate) { this.exchangeDate = exchangeDate; }

    public String getExchangeStatus() { return exchangeStatus; }
    public void setExchangeStatus(String exchangeStatus) { this.exchangeStatus = exchangeStatus; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Company getCompany() { return company; }
    public void setCompany(Company company) { this.company = company; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
