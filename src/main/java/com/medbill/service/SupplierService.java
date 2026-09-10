package com.medbill.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.medbill.entity.Supplier;
import com.medbill.repository.SupplierRepository;

@Service
@Transactional
public class SupplierService {

    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public List<Supplier> getAllSuppliers(Long companyId) {
        if (companyId != null) {
            List<Supplier> list = supplierRepository.findByCompanyIdOrderByNameAsc(companyId);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return supplierRepository.findAllByOrderByNameAsc();
    }

    public List<Supplier> getActiveSuppliers(Long companyId) {
        if (companyId != null) {
            return supplierRepository.findByCompanyIdAndStatusOrderByNameAsc(companyId, "ACTIVE");
        }
        return supplierRepository.findAllByOrderByNameAsc();
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found with id: " + id));
    }

    public Supplier createSupplier(Supplier supplier, Long companyId) {
        if (companyId != null) {
            supplier.setCompanyId(companyId);
        }
        return supplierRepository.save(supplier);
    }

    public Supplier updateSupplier(Long id, Supplier details) {
        Supplier existing = getSupplierById(id);
        existing.setName(details.getName());
        existing.setContactPerson(details.getContactPerson());
        existing.setPhone(details.getPhone());
        existing.setEmail(details.getEmail());
        existing.setGstin(details.getGstin());
        existing.setDrugLicenseNo(details.getDrugLicenseNo());
        existing.setPaymentTerms(details.getPaymentTerms());
        existing.setCreditLimit(details.getCreditLimit());
        existing.setBankName(details.getBankName());
        existing.setBankAccountNumber(details.getBankAccountNumber());
        existing.setIfscCode(details.getIfscCode());
        existing.setAddress(details.getAddress());
        existing.setCity(details.getCity());
        existing.setState(details.getState());
        existing.setPincode(details.getPincode());
        existing.setNotes(details.getNotes());
        existing.setStatus(details.getStatus());
        return supplierRepository.save(existing);
    }

    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
