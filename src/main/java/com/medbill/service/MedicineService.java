package com.medbill.service;

import java.util.List;

import com.medbill.entity.Medicine;

public interface MedicineService {

    List<Medicine> getAllMedicines(Long companyId);

    Medicine getMedicineById(Long id);

    Medicine saveMedicine(Medicine medicine, Long companyId);

    Medicine updateMedicine(Long id, Medicine medicine);

    void deleteMedicine(Long id);
}

