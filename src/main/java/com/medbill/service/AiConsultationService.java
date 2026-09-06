package com.medbill.service;

import java.util.List;

import com.medbill.entity.AiConsultation;

public interface AiConsultationService {

    AiConsultation analyzeAndSave(String queryPrompt, Long companyId);

    List<AiConsultation> getConsultationHistory(Long companyId);

    AiConsultation getConsultationById(Long id);

    void deleteConsultation(Long id);
}

