package com.medbill.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.medbill.entity.Rack;
import com.medbill.repository.RackRepository;

@Service
@Transactional
public class RackService {

    private final RackRepository rackRepository;

    public RackService(RackRepository rackRepository) {
        this.rackRepository = rackRepository;
    }

    public List<Rack> getAllRacks(Long companyId) {
        if (companyId != null) {
            List<Rack> list = rackRepository.findByCompanyIdOrderByNameAsc(companyId);
            if (!list.isEmpty()) {
                return list;
            }
        }
        return rackRepository.findAllByOrderByNameAsc();
    }

    public List<Rack> getRacksByWarehouse(Long warehouseId) {
        return rackRepository.findByWarehouseIdOrderByNameAsc(warehouseId);
    }

    public Rack getRackById(Long id) {
        return rackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rack not found with id: " + id));
    }

    public Rack createRack(Rack rack, Long companyId) {
        if (companyId != null) {
            rack.setCompanyId(companyId);
        }
        return rackRepository.save(rack);
    }

    public Rack updateRack(Long id, Rack details) {
        Rack existing = getRackById(id);
        existing.setName(details.getName());
        existing.setDescription(details.getDescription());
        existing.setWarehouseId(details.getWarehouseId());
        existing.setStatus(details.getStatus());
        return rackRepository.save(existing);
    }

    public void deleteRack(Long id) {
        rackRepository.deleteById(id);
    }
}
