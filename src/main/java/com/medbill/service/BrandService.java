package com.medbill.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Brand;
import com.medbill.entity.Company;
import com.medbill.repository.BrandRepository;
import com.medbill.repository.CompanyRepository;

@Service
@Transactional
public class BrandService {

    private final BrandRepository brandRepository;
    private final CompanyRepository companyRepository;

    public BrandService(BrandRepository brandRepository, CompanyRepository companyRepository) {
        this.brandRepository = brandRepository;
        this.companyRepository = companyRepository;
    }

    public List<Brand> getAllBrands(Long companyId) {
        if (companyId != null) {
            List<Brand> list = brandRepository.findByCompanyIdOrderByNameAsc(companyId);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return brandRepository.findAll();
    }

    public List<Brand> getActiveBrands(Long companyId) {
        if (companyId != null) {
            return brandRepository.findByCompanyIdAndStatus(companyId, "ACTIVE");
        }
        return brandRepository.findByStatus("ACTIVE");
    }

    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found with ID: " + id));
    }

    public Brand createBrand(Brand brand, Long companyId) {
        if (brand.getName() == null || brand.getName().trim().isEmpty()) {
            throw new RuntimeException("Brand name is required");
        }

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            if (company != null) {
                brand.setCompany(company);
            }
        }

        return brandRepository.save(brand);
    }

    public Brand updateBrand(Long id, Brand details) {
        Brand existing = getBrandById(id);
        existing.setName(details.getName());
        existing.setCode(details.getCode());
        existing.setManufacturer(details.getManufacturer());
        existing.setDescription(details.getDescription());
        existing.setLogoUrl(details.getLogoUrl());
        existing.setStatus(details.getStatus());

        return brandRepository.save(existing);
    }

    public void deleteBrand(Long id) {
        Brand existing = getBrandById(id);
        brandRepository.delete(existing);
    }
}

