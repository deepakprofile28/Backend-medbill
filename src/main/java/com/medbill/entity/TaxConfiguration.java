package com.medbill.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "tax_configurations")
public class TaxConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tax_name", nullable = false)
    private String taxName;

    @Column(name = "gst_rate", nullable = false)
    private Double gstRate = 12.0;

    @Column(name = "cgst_rate")
    private Double cgstRate = 6.0;

    @Column(name = "sgst_rate")
    private Double sgstRate = 6.0;

    // INCLUSIVE or EXCLUSIVE
    @Column(name = "tax_type", length = 20, nullable = false)
    private String taxType = "EXCLUSIVE";

    // ACTIVE or INACTIVE
    @Column(name = "status", length = 20, nullable = false)
    private String status = "ACTIVE";

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_default")
    private Boolean isDefault = false;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public TaxConfiguration() {}

    public TaxConfiguration(String taxName, Double gstRate, String taxType, String status, String description, Boolean isDefault) {
        this.taxName = taxName;
        this.gstRate = gstRate;
        this.cgstRate = gstRate != null ? gstRate / 2.0 : 0.0;
        this.sgstRate = gstRate != null ? gstRate / 2.0 : 0.0;
        this.taxType = taxType != null ? taxType.toUpperCase() : "EXCLUSIVE";
        this.status = status != null ? status.toUpperCase() : "ACTIVE";
        this.description = description;
        this.isDefault = isDefault != null ? isDefault : false;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateComponentRates();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateComponentRates();
    }

    public void calculateComponentRates() {
        if (this.gstRate != null) {
            this.cgstRate = Math.round((this.gstRate / 2.0) * 100.0) / 100.0;
            this.sgstRate = Math.round((this.gstRate / 2.0) * 100.0) / 100.0;
        }
    }

    // ================= GETTERS AND SETTERS =================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public Double getGstRate() {
        return gstRate;
    }

    public void setGstRate(Double gstRate) {
        this.gstRate = gstRate;
        calculateComponentRates();
    }

    public Double getCgstRate() {
        return cgstRate;
    }

    public void setCgstRate(Double cgstRate) {
        this.cgstRate = cgstRate;
    }

    public Double getSgstRate() {
        return sgstRate;
    }

    public void setSgstRate(Double sgstRate) {
        this.sgstRate = sgstRate;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType != null ? taxType.toUpperCase() : "EXCLUSIVE";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status != null ? status.toUpperCase() : "ACTIVE";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

