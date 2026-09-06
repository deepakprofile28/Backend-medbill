package com.medbill.service.ai;

import java.util.List;

public class ClinicalDrugProfile {
    private String medicineName;
    private String genericComposition;
    private String category;
    private String dosageInstructions;
    private String sideEffects;
    private String genericSubstitutes;
    private String clinicalAssessment;
    private List<String> keywords;

    public ClinicalDrugProfile(String medicineName, String genericComposition, String category,
                               String dosageInstructions, String sideEffects, String genericSubstitutes,
                               String clinicalAssessment, List<String> keywords) {
        this.medicineName = medicineName;
        this.genericComposition = genericComposition;
        this.category = category;
        this.dosageInstructions = dosageInstructions;
        this.sideEffects = sideEffects;
        this.genericSubstitutes = genericSubstitutes;
        this.clinicalAssessment = clinicalAssessment;
        this.keywords = keywords;
    }

    public String getMedicineName() { return medicineName; }
    public String getGenericComposition() { return genericComposition; }
    public String getCategory() { return category; }
    public String getDosageInstructions() { return dosageInstructions; }
    public String getSideEffects() { return sideEffects; }
    public String getGenericSubstitutes() { return genericSubstitutes; }
    public String getClinicalAssessment() { return clinicalAssessment; }
    public List<String> getKeywords() { return keywords; }
}

