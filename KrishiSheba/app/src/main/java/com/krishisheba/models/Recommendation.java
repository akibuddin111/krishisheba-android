package com.krishisheba.models;

import java.util.List;

public class Recommendation {

    private String cause;
    private List<String> treatment;
    private List<String> prevention;

    public String getCause() {
        return cause;
    }

    public List<String> getTreatment() {
        return treatment;
    }

    public List<String> getPrevention() {
        return prevention;
    }
}