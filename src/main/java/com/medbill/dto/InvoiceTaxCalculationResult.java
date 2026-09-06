package com.medbill.dto;

import java.util.List;
import java.util.Map;

public class InvoiceTaxCalculationResult {

    private Double grossSubtotal;
    private Double totalDiscount;
    private Double totalTaxableAmount;
    private Double totalCgst;
    private Double totalSgst;
    private Double totalTax;
    private Double roundOffDifference;
    private Double grandTotal;
    private List<TaxCalculationResult> calculatedItems;
    private Map<String, Double> taxSlabBreakdown;

    public InvoiceTaxCalculationResult() {}

    public Double getGrossSubtotal() {
        return grossSubtotal;
    }

    public void setGrossSubtotal(Double grossSubtotal) {
        this.grossSubtotal = grossSubtotal;
    }

    public Double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public Double getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public void setTotalTaxableAmount(Double totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }

    public Double getTotalCgst() {
        return totalCgst;
    }

    public void setTotalCgst(Double totalCgst) {
        this.totalCgst = totalCgst;
    }

    public Double getTotalSgst() {
        return totalSgst;
    }

    public void setTotalSgst(Double totalSgst) {
        this.totalSgst = totalSgst;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getRoundOffDifference() {
        return roundOffDifference;
    }

    public void setRoundOffDifference(Double roundOffDifference) {
        this.roundOffDifference = roundOffDifference;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public List<TaxCalculationResult> getCalculatedItems() {
        return calculatedItems;
    }

    public void setCalculatedItems(List<TaxCalculationResult> calculatedItems) {
        this.calculatedItems = calculatedItems;
    }

    public Map<String, Double> getTaxSlabBreakdown() {
        return taxSlabBreakdown;
    }

    public void setTaxSlabBreakdown(Map<String, Double> taxSlabBreakdown) {
        this.taxSlabBreakdown = taxSlabBreakdown;
    }
}

