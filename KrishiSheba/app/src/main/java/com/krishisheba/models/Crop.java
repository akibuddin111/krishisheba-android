package com.krishisheba.models;

public class Crop {

    private String name;
    private String season;
    private String soil;
    private String description;

    public Crop(String name,
                String season,
                String soil,
                String description) {

        this.name = name;
        this.season = season;
        this.soil = soil;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getSeason() {
        return season;
    }

    public String getSoil() {
        return soil;
    }

    public String getDescription() {
        return description;
    }
}
