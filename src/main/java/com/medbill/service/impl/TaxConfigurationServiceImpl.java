package com.medbill.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.medbill.entity.Company;
import com.medbill.entity.TaxConfiguration;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.TaxConfigurationRepository;
import com.medbill.service.TaxConfigurationService;

@Service
public class TaxConfigurationServiceImpl implements TaxConfigurationService {

    private final TaxConfigurationRepository taxRepository;
    private final CompanyRepository companyRepository;

    public TaxConfigurationServiceImpl(TaxConfigurationRepository taxRepository, CompanyRepository companyRepository) {
        this.taxRepository = taxRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<TaxConfiguration> getAllTaxes(Long companyId) {
        List<TaxConfiguration> list = new ArrayList<>();
        if (companyId != null) {
            list = taxRepository.findByCompanyIdOrderByIdAsc(companyId);
        }
        if (list.isEmpty()) {
            list = taxRepository.findAllByOrderByIdAsc();
        }

        // Auto-seed standard Indian Pharmacy GST brackets if empty
        if (list.isEmpty()) {
            seedDefaultTaxes(companyId);
            list = taxRepository.findAllByOrderByIdAsc();
        }

        return list;
    }

    private void seedDefaultTaxes(Long companyId) {
        Company company = null;
        if (companyId != null) {
            company = companyRepository.findById(companyId).orElse(null);
        }
        if (company == null) {
            List<Company> all = companyRepository.findAll();
            if (!all.isEmpty()) {
                company = all.get(0);
            }
        }

        List<TaxConfiguration> defaults = List.of(
            new TaxConfiguration("GST 0% - Nil / Exempted", 0.0, "EXCLUSIVE", "ACTIVE", "Life-saving medicines & vaccines exempted from GST", false),
            new TaxConfiguration("GST 5% - Essential Drugs", 5.0, "EXCLUSIVE", "ACTIVE", "Oral rehydration, insulin, essential diagnostic kits", false),
            new TaxConfiguration("GST 12% - Standard Pharma", 12.0, "EXCLUSIVE", "ACTIVE", "Standard allopathic medicines, antibiotics, & syrups", true),
            new TaxConfiguration("GST 18% - Healthcare & Wellness", 18.0, "EXCLUSIVE", "ACTIVE", "Nutritional supplements, skin care, & surgical disposables", false),
            new TaxConfiguration("GST 12% - Inclusive (MRP)", 12.0, "INCLUSIVE", "ACTIVE", "Retail medicines where GST is pre-included in product MRP", false)
        );

        for (TaxConfiguration tc : defaults) {
            tc.setCompany(company);
            taxRepository.save(tc);
        }
    }

    @Override
    public TaxConfiguration getTaxById(Long id) {
        return taxRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tax configuration not found with ID: " + id));
    }

    @Override
    public TaxConfiguration createTax(TaxConfiguration tax, Long companyId) {
        if (companyId != null) {
            companyRepository.findById(companyId).ifPresent(tax::setCompany);
        }
        if (tax.getCompany() == null) {
            List<Company> all = companyRepository.findAll();
            if (!all.isEmpty()) {
                tax.setCompany(all.get(0));
            }
        }

        if (tax.getTaxName() == null || tax.getTaxName().trim().isEmpty()) {
            tax.setTaxName("GST " + (tax.getGstRate() != null ? tax.getGstRate() : 12) + "%");
        }

        tax.calculateComponentRates();
        return taxRepository.save(tax);
    }

    @Override
    public TaxConfiguration updateTax(Long id, TaxConfiguration updated) {
        TaxConfiguration existing = getTaxById(id);

        if (updated.getTaxName() != null && !updated.getTaxName().trim().isEmpty()) {
            existing.setTaxName(updated.getTaxName().trim());
        }
        if (updated.getGstRate() != null) {
            existing.setGstRate(updated.getGstRate());
        }
        if (updated.getTaxType() != null && !updated.getTaxType().trim().isEmpty()) {
            existing.setTaxType(updated.getTaxType().trim().toUpperCase());
        }
        if (updated.getStatus() != null && !updated.getStatus().trim().isEmpty()) {
            existing.setStatus(updated.getStatus().trim().toUpperCase());
        }
        if (updated.getDescription() != null) {
            existing.setDescription(updated.getDescription().trim());
        }
        if (updated.getIsDefault() != null) {
            existing.setIsDefault(updated.getIsDefault());
        }

        existing.calculateComponentRates();
        return taxRepository.save(existing);
    }

    @Override
    public void deleteTax(Long id) {
        taxRepository.deleteById(id);
    }

    @Override
    public com.medbill.dto.TaxCalculationResult calculateItemTax(com.medbill.dto.TaxCalculationRequest request) {
        if (request == null) {
            request = new com.medbill.dto.TaxCalculationRequest();
        }

        Double amount = request.getAmount() != null ? request.getAmount() : 0.0;
        int qty = request.getQuantity() != null && request.getQuantity() > 0 ? request.getQuantity() : 1;
        Double discountPct = request.getDiscountPercent() != null ? request.getDiscountPercent() : 0.0;

        Double gstRate = request.getGstRate();
        String taxType = request.getTaxType();

        // If taxId was provided, lookup from DB
        if (request.getTaxId() != null) {
            try {
                TaxConfiguration tc = getTaxById(request.getTaxId());
                if (tc != null) {
                    if (gstRate == null) gstRate = tc.getGstRate();
                    if (taxType == null) taxType = tc.getTaxType();
                }
            } catch (Exception ignored) {}
        }

        if (gstRate == null) gstRate = 12.0;
        if (taxType == null) taxType = "EXCLUSIVE";
        taxType = taxType.trim().toUpperCase();

        double gross = round2(amount * qty);
        double discount = round2(gross * (discountPct / 100.0));
        double effective = round2(gross - discount);

        double taxable;
        double totalTax;
        double netTotal;
        String formulaDesc;

        if ("INCLUSIVE".equalsIgnoreCase(taxType)) {
            // INCLUSIVE FORMULA:
            // The price already contains GST.
            // Taxable Base = Total / (1 + Rate / 100)
            // GST Amount = Total - Taxable Base
            taxable = round2((effective * 100.0) / (100.0 + gstRate));
            totalTax = round2(effective - taxable);
            netTotal = effective;
            formulaDesc = String.format("Inclusive: Taxable Base = (%.2f * 100) / (100 + %.1f) = %.2f, GST Included = %.2f, Net = %.2f",
                    effective, gstRate, taxable, totalTax, netTotal);
        } else {
            // EXCLUSIVE FORMULA:
            // GST is added ON TOP of the price.
            // Taxable Base = Effective Price
            // GST Amount = Taxable Base * (Rate / 100)
            // Net Total = Taxable Base + GST Amount
            taxable = effective;
            totalTax = round2(taxable * (gstRate / 100.0));
            netTotal = round2(taxable + totalTax);
            formulaDesc = String.format("Exclusive: Taxable Base = %.2f, GST (%.1f%%) = %.2f, Net Total = %.2f",
                    taxable, gstRate, totalTax, netTotal);
        }

        double cgst = round2(totalTax / 2.0);
        double sgst = round2(totalTax - cgst);

        com.medbill.dto.TaxCalculationResult result = new com.medbill.dto.TaxCalculationResult();
        result.setGrossAmount(gross);
        result.setDiscountAmount(discount);
        result.setTaxableAmount(taxable);
        result.setGstRate(gstRate);
        result.setCgstRate(round2(gstRate / 2.0));
        result.setSgstRate(round2(gstRate / 2.0));
        result.setCgstAmount(cgst);
        result.setSgstAmount(sgst);
        result.setTotalTaxAmount(totalTax);
        result.setNetTotal(netTotal);
        result.setTaxType(taxType);
        result.setFormulaDescription(formulaDesc);

        return result;
    }

    @Override
    public com.medbill.dto.InvoiceTaxCalculationResult calculateInvoiceTax(com.medbill.dto.InvoiceTaxCalculationRequest request) {
        if (request == null) {
            request = new com.medbill.dto.InvoiceTaxCalculationRequest();
        }

        List<com.medbill.dto.TaxCalculationRequest> items = request.getItems();
        List<com.medbill.dto.TaxCalculationResult> calculatedItems = new java.util.ArrayList<>();
        java.util.Map<String, Double> slabMap = new java.util.LinkedHashMap<>();

        double grossSubtotal = 0.0;
        double totalDiscount = 0.0;
        double totalTaxable = 0.0;
        double totalCgst = 0.0;
        double totalSgst = 0.0;
        double totalTax = 0.0;
        double totalNet = 0.0;

        if (items != null) {
            for (com.medbill.dto.TaxCalculationRequest itemReq : items) {
                com.medbill.dto.TaxCalculationResult itemRes = calculateItemTax(itemReq);
                calculatedItems.add(itemRes);

                grossSubtotal = round2(grossSubtotal + itemRes.getGrossAmount());
                totalDiscount = round2(totalDiscount + itemRes.getDiscountAmount());
                totalTaxable = round2(totalTaxable + itemRes.getTaxableAmount());
                totalCgst = round2(totalCgst + itemRes.getCgstAmount());
                totalSgst = round2(totalSgst + itemRes.getSgstAmount());
                totalTax = round2(totalTax + itemRes.getTotalTaxAmount());
                totalNet = round2(totalNet + itemRes.getNetTotal());

                String slabKey = "GST " + itemRes.getGstRate() + "% (" + itemRes.getTaxType() + ")";
                slabMap.put(slabKey, round2(slabMap.getOrDefault(slabKey, 0.0) + itemRes.getTotalTaxAmount()));
            }
        }

        // Apply overall bill discount if any
        if (request.getOverallDiscountPercent() != null && request.getOverallDiscountPercent() > 0) {
            double overallDisc = round2(totalNet * (request.getOverallDiscountPercent() / 100.0));
            totalDiscount = round2(totalDiscount + overallDisc);
            totalNet = round2(totalNet - overallDisc);
        }

        double grandTotal = totalNet;
        double roundDiff = 0.0;

        if (Boolean.TRUE.equals(request.getEnableRoundOff())) {
            double rounded = Math.round(grandTotal);
            roundDiff = round2(rounded - grandTotal);
            grandTotal = rounded;
        }

        com.medbill.dto.InvoiceTaxCalculationResult invoiceResult = new com.medbill.dto.InvoiceTaxCalculationResult();
        invoiceResult.setGrossSubtotal(grossSubtotal);
        invoiceResult.setTotalDiscount(totalDiscount);
        invoiceResult.setTotalTaxableAmount(totalTaxable);
        invoiceResult.setTotalCgst(totalCgst);
        invoiceResult.setTotalSgst(totalSgst);
        invoiceResult.setTotalTax(totalTax);
        invoiceResult.setRoundOffDifference(roundDiff);
        invoiceResult.setGrandTotal(grandTotal);
        invoiceResult.setCalculatedItems(calculatedItems);
        invoiceResult.setTaxSlabBreakdown(slabMap);

        return invoiceResult;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

