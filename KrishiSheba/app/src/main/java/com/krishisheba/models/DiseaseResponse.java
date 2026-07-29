package com.krishisheba.models;

public class DiseaseResponse {

    private String label;
    private String display_label;
    private float confidence;

    private Recommendation recommendation;

    public String getLabel() {
        return label;
    }

    public String getDisplayLabel() {
        return display_label != null ? display_label : label;
    }

    public float getConfidence() {
        return confidence;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }
}
