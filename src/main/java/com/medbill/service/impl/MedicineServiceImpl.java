package com.medbill.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.medbill.entity.Company;
import com.medbill.entity.Medicine;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.MedicineRepository;
import com.medbill.service.MedicineService;

@Service
public class MedicineServiceImpl implements MedicineService {

    private final MedicineRepository medicineRepository;
    private final CompanyRepository companyRepository;

    public MedicineServiceImpl(MedicineRepository medicineRepository, CompanyRepository companyRepository) {
        this.medicineRepository = medicineRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Medicine> getAllMedicines(Long companyId) {
        if (companyId != null) {
            List<Medicine> list = medicineRepository.findByCompanyIdOrderByNameAsc(companyId);
            if (list != null && !list.isEmpty()) {
                return list;
            }
        }
        return medicineRepository.findAllByOrderByNameAsc();
    }

    @Override
    public Medicine getMedicineById(Long id) {
        return medicineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medicine not found with id: " + id));
    }

    @Override
    public Medicine saveMedicine(Medicine medicine, Long companyId) {
        Company company = null;
        if (companyId != null) {
            company = companyRepository.findById(companyId).orElse(null);
        }
        if (company == null) {
            List<Company> allCompanies = companyRepository.findAll();
            if (!allCompanies.isEmpty()) {
                company = allCompanies.get(0);
            }
        }
        if (company != null) {
            medicine.setCompany(company);
        }
        return medicineRepository.save(medicine);
    }

    @Override
    public Medicine updateMedicine(Long id, Medicine updated) {
        Medicine existing = getMedicineById(id);
        existing.setName(updated.getName());
        existing.setGenericName(updated.getGenericName());
        existing.setCategory(updated.getCategory());
        existing.setBatchNo(updated.getBatchNo());
        existing.setExpiryDate(updated.getExpiryDate());
        existing.setUnitPrice(updated.getUnitPrice());
        existing.setMrp(updated.getMrp() != null ? updated.getMrp() : updated.getUnitPrice());
        existing.setGstRate(updated.getGstRate() != null ? updated.getGstRate() : 12);
        existing.setStockQty(updated.getStockQty() != null ? updated.getStockQty() : 0);
        existing.setManufacturer(updated.getManufacturer());
        return medicineRepository.save(existing);
    }

    @Override
    public void deleteMedicine(Long id) {
        medicineRepository.deleteById(id);
    }
}
