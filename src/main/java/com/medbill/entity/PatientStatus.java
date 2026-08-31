package com.medbill.entity;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum PatientStatus {

    DRAFT,
    APPROVED;

    @JsonCreator
    public static PatientStatus fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return DRAFT;
        }
        String upper = value.trim().toUpperCase();
        if (upper.contains("DRAFT")) {
            return DRAFT;
        }
        return APPROVED;
    }
}