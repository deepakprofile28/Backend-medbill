package com.medbill.dto;

public class TaxCalculationRequest {

    private Double amount;
    private Integer quantity = 1;
    private Double discountPercent = 0.0;
    private Double gstRate;
    private String taxType = "EXCLUSIVE"; // "EXCLUSIVE" or "INCLUSIVE"
    private Long taxId;

    public TaxCalculationRequest() {}

    public TaxCalculationRequest(Double amount, Integer quantity, Double discountPercent, Double gstRate, String taxType) {
        this.amount = amount;
        this.quantity = quantity;
        this.discountPercent = discountPercent;
        this.gstRate = gstRate;
        this.taxType = taxType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getQuantity() {
        return quantity != null && quantity > 0 ? quantity : 1;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getDiscountPercent() {
        return discountPercent != null && discountPercent >= 0 ? discountPercent : 0.0;
    }

    public void setDiscountPercent(Double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Double getGstRate() {
        return gstRate != null && gstRate >= 0 ? gstRate : 0.0;
    }

    public void setGstRate(Double gstRate) {
        this.gstRate = gstRate;
    }

    public String getTaxType() {
        return taxType != null ? taxType.toUpperCase().trim() : "EXCLUSIVE";
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }
}

