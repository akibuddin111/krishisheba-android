package com.krishisheba.ml;

import android.graphics.Bitmap;

import java.util.Random;

public class DiseaseClassifier {

    // Disease classes
    private final String[] diseases = {
            "Healthy Leaf",
            "Rice Blast",
            "Rice Brown Spot",
            "Tomato Early Blight",
            "Tomato Late Blight",
            "Maize Leaf Blight",
            "Wheat Rust"
    };

    // Simulated prediction
    public String classify(Bitmap bitmap) {

        Random random = new Random();

        int index = random.nextInt(diseases.length);

        int confidence = 75 + random.nextInt(24);   // 75-98%

        return diseases[index] + " (" + confidence + "%)";
    }

    // Treatment advice
    public String getAdvice(String disease) {

        if (disease.contains("Healthy")) {
            return "Crop is healthy. Continue regular monitoring.";
        }

        if (disease.contains("Rice Blast")) {
            return "Apply fungicide and avoid excessive irrigation.";
        }

        if (disease.contains("Brown Spot")) {
            return "Improve drainage and apply balanced fertilizer.";
        }

        if (disease.contains("Tomato Early Blight")) {
            return "Remove infected leaves and apply copper fungicide.";
        }

        if (disease.contains("Tomato Late Blight")) {
            return "Reduce leaf moisture and spray protective fungicide.";
        }

        if (disease.contains("Maize")) {
            return "Inspect leaves and use disease-resistant seed next season.";
        }

        if (disease.contains("Wheat Rust")) {
            return "Use rust-resistant variety and apply fungicide.";
        }

        return "Inspect crop manually and consult local agriculture expert.";
    }
}