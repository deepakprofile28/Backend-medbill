package com.medbill.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.medbill.entity.Company;
import com.medbill.entity.StoreConfiguration;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.StoreConfigurationRepository;
import com.medbill.service.StoreConfigurationService;

@Service
public class StoreConfigurationServiceImpl implements StoreConfigurationService {

    private final StoreConfigurationRepository configRepository;
    private final CompanyRepository companyRepository;

    public StoreConfigurationServiceImpl(StoreConfigurationRepository configRepository, CompanyRepository companyRepository) {
        this.configRepository = configRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public StoreConfiguration getConfiguration(Long companyId) {
        if (companyId != null) {
            Optional<StoreConfiguration> opt = configRepository.findFirstByCompanyId(companyId);
            if (opt.isPresent()) {
                return opt.get();
            }
        }

        // Fallback to first configuration
        Optional<StoreConfiguration> first = configRepository.findFirstByOrderByIdAsc();
        if (first.isPresent()) {
            return first.get();
        }

        // Initialize default configuration linked to primary company
        StoreConfiguration defaultConfig = new StoreConfiguration();
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
        if (company != null) {
            defaultConfig.setCompany(company);
            defaultConfig.setStoreName(company.getName() != null ? company.getName() : "Apollo Pharmacy");
            if (company.getAddress() != null && !company.getAddress().trim().isEmpty()) {
                defaultConfig.setAddress(company.getAddress());
            }
            if (company.getMobile() != null && !company.getMobile().trim().isEmpty()) {
                defaultConfig.setPhone(company.getMobile());
            }
            if (company.getEmail() != null && !company.getEmail().trim().isEmpty()) {
                defaultConfig.setEmail(company.getEmail());
            }
        }

        return configRepository.save(defaultConfig);
    }

    @Override
    public StoreConfiguration saveOrUpdateConfiguration(StoreConfiguration newConfig, Long companyId) {
        StoreConfiguration existing = null;
        if (companyId != null) {
            existing = configRepository.findFirstByCompanyId(companyId).orElse(null);
        }
        if (existing == null) {
            existing = configRepository.findFirstByOrderByIdAsc().orElse(null);
        }

        if (existing == null) {
            existing = new StoreConfiguration();
        }

        // Update all configuration fields
        if (newConfig.getStoreName() != null && !newConfig.getStoreName().trim().isEmpty()) {
            existing.setStoreName(newConfig.getStoreName().trim());
        }
        if (newConfig.getStoreSubtitle() != null) {
            existing.setStoreSubtitle(newConfig.getStoreSubtitle().trim());
        }
        if (newConfig.getLogoUrl() != null) {
            existing.setLogoUrl(newConfig.getLogoUrl().trim());
        }
        if (newConfig.getCurrencySymbol() != null && !newConfig.getCurrencySymbol().trim().isEmpty()) {
            existing.setCurrencySymbol(newConfig.getCurrencySymbol().trim());
        }
        if (newConfig.getCurrencyCode() != null && !newConfig.getCurrencyCode().trim().isEmpty()) {
            existing.setCurrencyCode(newConfig.getCurrencyCode().trim());
        }
        if (newConfig.getPhone() != null) {
            existing.setPhone(newConfig.getPhone().trim());
        }
        if (newConfig.getEmail() != null) {
            existing.setEmail(newConfig.getEmail().trim());
        }
        if (newConfig.getAddress() != null) {
            existing.setAddress(newConfig.getAddress().trim());
        }
        if (newConfig.getCity() != null) {
            existing.setCity(newConfig.getCity().trim());
        }
        if (newConfig.getState() != null) {
            existing.setState(newConfig.getState().trim());
        }
        if (newConfig.getPincode() != null) {
            existing.setPincode(newConfig.getPincode().trim());
        }
        if (newConfig.getGstin() != null) {
            existing.setGstin(newConfig.getGstin().trim());
        }
        if (newConfig.getDrugLicenseNo() != null) {
            existing.setDrugLicenseNo(newConfig.getDrugLicenseNo().trim());
        }
        if (newConfig.getInvoicePrefix() != null) {
            existing.setInvoicePrefix(newConfig.getInvoicePrefix().trim());
        }
        if (newConfig.getDefaultReceiptType() != null) {
            existing.setDefaultReceiptType(newConfig.getDefaultReceiptType().trim());
        }
        if (newConfig.getDefaultGstRate() != null) {
            existing.setDefaultGstRate(newConfig.getDefaultGstRate());
        }
        if (newConfig.getBillGreeting() != null) {
            existing.setBillGreeting(newConfig.getBillGreeting().trim());
        }
        if (newConfig.getReturnPolicyTerms() != null) {
            existing.setReturnPolicyTerms(newConfig.getReturnPolicyTerms().trim());
        }
        if (newConfig.getEnableRoundOff() != null) {
            existing.setEnableRoundOff(newConfig.getEnableRoundOff());
        }

        // Link company
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
        if (company != null) {
            existing.setCompany(company);
        }

        return configRepository.save(existing);
    }
}

