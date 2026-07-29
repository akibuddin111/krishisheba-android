package com.krishisheba.utils;

import java.util.ArrayList;
import java.util.List;

public class CropRecommendationEngine {

    public static List<String> getRecommendedCrops(double temp, double windSpeed) {

        List<String> crops = new ArrayList<>();

        // 🌞 HOT WEATHER
        if (temp >= 30) {
            crops.add("Rice (Kharif crop)");
            crops.add("Jute");
            crops.add("Maize");
        }

        // 🌤 MODERATE WEATHER
        else if (temp >= 20 && temp < 30) {
            crops.add("Wheat");
            crops.add("Potato");
            crops.add("Vegetables (Tomato, Brinjal)");
        }

        // ❄ COLD WEATHER
        else {
            crops.add("Mustard");
            crops.add("Lentil");
            crops.add("Garlic");
        }

        // 🌬 Wind condition adjustment
        if (windSpeed > 20) {
            crops.add("⚠ Avoid delicate crops (storm risk)");
        }

        return crops;
    }
}
