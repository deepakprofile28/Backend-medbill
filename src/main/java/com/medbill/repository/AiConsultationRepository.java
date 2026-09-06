package com.medbill.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.medbill.entity.AiConsultation;

@Repository
public interface AiConsultationRepository extends JpaRepository<AiConsultation, Long> {

    List<AiConsultation> findByCompanyIdOrderByCreatedAtDesc(Long companyId);

    List<AiConsultation> findAllByOrderByCreatedAtDesc();
}

