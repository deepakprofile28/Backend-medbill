package com.medbill.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medbill.entity.Company;
import com.medbill.entity.Warehouse;
import com.medbill.repository.CompanyRepository;
import com.medbill.repository.WarehouseRepository;

@Service
@Transactional
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final CompanyRepository companyRepository;

    public WarehouseService(WarehouseRepository warehouseRepository, CompanyRepository companyRepository) {
        this.warehouseRepository = warehouseRepository;
        this.companyRepository = companyRepository;
    }

    public List<Warehouse> getAllWarehouses(Long companyId) {
        if (companyId != null) {
            List<Warehouse> list = warehouseRepository.findByCompanyIdOrderByNameAsc(companyId);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return warehouseRepository.findAll();
    }

    public List<Warehouse> getActiveWarehouses(Long companyId) {
        if (companyId != null) {
            return warehouseRepository.findByCompanyIdAndStatus(companyId, "ACTIVE");
        }
        return warehouseRepository.findByStatus("ACTIVE");
    }

    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found with ID: " + id));
    }

    public Warehouse createWarehouse(Warehouse warehouse, Long companyId) {
        if (warehouse.getName() == null || warehouse.getName().trim().isEmpty()) {
            throw new RuntimeException("Warehouse name is required");
        }

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            if (company != null) {
                warehouse.setCompany(company);
            }
        }

        return warehouseRepository.save(warehouse);
    }

    public Warehouse updateWarehouse(Long id, Warehouse details) {
        Warehouse existing = getWarehouseById(id);
        existing.setName(details.getName());
        existing.setCode(details.getCode());
        existing.setAddress(details.getAddress());
        existing.setCity(details.getCity());
        existing.setState(details.getState());
        existing.setPincode(details.getPincode());
        existing.setContactPerson(details.getContactPerson());
        existing.setPhone(details.getPhone());
        existing.setEmail(details.getEmail());
        existing.setCapacity(details.getCapacity());
        existing.setStatus(details.getStatus());

        return warehouseRepository.save(existing);
    }

    public void deleteWarehouse(Long id) {
        Warehouse existing = getWarehouseById(id);
        warehouseRepository.delete(existing);
    }
}

