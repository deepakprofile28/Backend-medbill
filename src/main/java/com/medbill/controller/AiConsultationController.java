package com.medbill.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.medbill.entity.AiConsultation;
import com.medbill.service.AiConsultationService;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "http://localhost:4200")
public class AiConsultationController {

    private final AiConsultationService aiService;

    public AiConsultationController(AiConsultationService aiService) {
        this.aiService = aiService;
    }

    // =========================================================
    // ANALYZE DRUG / SYMPTOM & SAVE TO SEPARATE TABLE
    // =========================================================
    @PostMapping("/analyze")
    public ResponseEntity<AiConsultation> analyzeAndSave(@RequestBody Map<String, Object> request) {
        String prompt = (String) request.get("prompt");
        Long companyId = null;
        if (request.get("companyId") != null) {
            try {
                companyId = Long.valueOf(request.get("companyId").toString());
            } catch (Exception ignored) {}
        }
        AiConsultation saved = aiService.analyzeAndSave(prompt, companyId);
        return ResponseEntity.ok(saved);
    }

    // =========================================================
    // GET CONSULTATION HISTORY FROM SEPARATE TABLE
    // =========================================================
    @GetMapping("/history")
    public ResponseEntity<List<AiConsultation>> getHistory(@RequestParam(required = false) Long companyId) {
        return ResponseEntity.ok(aiService.getConsultationHistory(companyId));
    }

    // =========================================================
    // GET BY ID
    // =========================================================
    @GetMapping("/{id}")
    public ResponseEntity<AiConsultation> getById(@PathVariable Long id) {
        return ResponseEntity.ok(aiService.getConsultationById(id));
    }

    // =========================================================
    // DELETE CONSULTATION ENTRY
    // =========================================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        aiService.deleteConsultation(id);
        return ResponseEntity.noContent().build();
    }
}

