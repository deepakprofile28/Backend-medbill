package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByOrderByPositionAsc();

    List<Category> findByCompanyIdOrderByPositionAsc(Long companyId);

    List<Category> findByStatusOrderByPositionAsc(String status);

    List<Category> findByCompanyIdAndStatusOrderByPositionAsc(Long companyId, String status);

    boolean existsByNameIgnoreCaseAndCompanyId(String name, Long companyId);
}

