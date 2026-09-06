package com.medbill.service;

import java.util.List;

import com.medbill.entity.TaxConfiguration;

public interface TaxConfigurationService {

    List<TaxConfiguration> getAllTaxes(Long companyId);

    TaxConfiguration getTaxById(Long id);

    TaxConfiguration createTax(TaxConfiguration tax, Long companyId);

    TaxConfiguration updateTax(Long id, TaxConfiguration tax);

    void deleteTax(Long id);

    com.medbill.dto.TaxCalculationResult calculateItemTax(com.medbill.dto.TaxCalculationRequest request);

    com.medbill.dto.InvoiceTaxCalculationResult calculateInvoiceTax(com.medbill.dto.InvoiceTaxCalculationRequest request);
}

