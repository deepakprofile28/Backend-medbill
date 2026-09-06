package com.medbill.dto;

import java.util.List;

public class InvoiceTaxCalculationRequest {

    private List<TaxCalculationRequest> items;
    private Double overallDiscountPercent = 0.0;
    private Boolean enableRoundOff = true;

    public InvoiceTaxCalculationRequest() {}

    public List<TaxCalculationRequest> getItems() {
        return items;
    }

    public void setItems(List<TaxCalculationRequest> items) {
        this.items = items;
    }

    public Double getOverallDiscountPercent() {
        return overallDiscountPercent != null ? overallDiscountPercent : 0.0;
    }

    public void setOverallDiscountPercent(Double overallDiscountPercent) {
        this.overallDiscountPercent = overallDiscountPercent;
    }

    public Boolean getEnableRoundOff() {
        return enableRoundOff != null ? enableRoundOff : true;
    }

    public void setEnableRoundOff(Boolean enableRoundOff) {
        this.enableRoundOff = enableRoundOff;
    }
}

