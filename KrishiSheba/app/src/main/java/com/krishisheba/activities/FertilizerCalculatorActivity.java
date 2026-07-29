package com.krishisheba.activities;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.krishisheba.R;

import java.util.Locale;

public class FertilizerCalculatorActivity extends AppCompatActivity {

    private static final double ACRES_PER_HECTARE = 2.47105d;
    private static final double UREA_N_PERCENT = 0.46d;
    private static final double TSP_P_PERCENT = 0.46d;
    private static final double MOP_K_PERCENT = 0.60d;

    private TextView tvSelectedCrop;
    private TextView tvNValue;
    private TextView tvPValue;
    private TextView tvKValue;
    private TextView tvFieldSize;
    private TextView tvResult;
    private TextView tvEditNutrients;
    private RadioGroup rgUnit;

    private double fieldSizeAcre = 1.5d;
    private boolean isAcre = true;
    private CropPreset selectedCrop = CropPreset.RICE;
    private double editableNPerAcre = selectedCrop.nitrogenPerAcre;
    private double editablePPerAcre = selectedCrop.phosphorusPerAcre;
    private double editableKPerAcre = selectedCrop.potassiumPerAcre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_calculator);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnCalculate = findViewById(R.id.btnCalculate);
        View btnCropSelector = findViewById(R.id.btnCropSelector);
        View btnMinus = findViewById(R.id.btnMinus);
        View btnPlus = findViewById(R.id.btnPlus);

        tvSelectedCrop = findViewById(R.id.tvSelectedCrop);
        tvNValue = findViewById(R.id.tvNValue);
        tvPValue = findViewById(R.id.tvPValue);
        tvKValue = findViewById(R.id.tvKValue);
        tvFieldSize = findViewById(R.id.tvFieldSize);
        tvResult = findViewById(R.id.tvResult);
        tvEditNutrients = findViewById(R.id.tvEditNutrients);
        rgUnit = findViewById(R.id.rgUnit);

        btnBack.setOnClickListener(v -> finish());
        btnCalculate.setOnClickListener(v -> calculateRecommendations());
        btnCropSelector.setOnClickListener(v -> showCropSelector());
        tvEditNutrients.setOnClickListener(v -> showNutrientEditor());
        btnMinus.setOnClickListener(v -> adjustFieldSize(-0.5d));
        btnPlus.setOnClickListener(v -> adjustFieldSize(0.5d));

        rgUnit.setOnCheckedChangeListener((group, checkedId) -> {
            boolean newIsAcre = checkedId == R.id.rbAcre;
            if (newIsAcre != isAcre) {
                isAcre = newIsAcre;
                updateFieldSizeDisplay();
            }
        });

        updateCropDisplay(selectedCrop);
        updateFieldSizeDisplay();
        renderResultPlaceholder();
    }

    private void showCropSelector() {
        CropPreset[] crops = CropPreset.values();
        String[] labels = new String[crops.length];

        for (int i = 0; i < crops.length; i++) {
            labels[i] = crops[i].displayName;
        }

        new AlertDialog.Builder(this)
                .setTitle("Select crop")
                .setItems(labels, (dialog, which) -> {
                    selectedCrop = crops[which];
                    editableNPerAcre = selectedCrop.nitrogenPerAcre;
                    editablePPerAcre = selectedCrop.phosphorusPerAcre;
                    editableKPerAcre = selectedCrop.potassiumPerAcre;
                    updateCropDisplay(selectedCrop);
                    renderResultPlaceholder();
                })
                .show();
    }

    private void showNutrientEditor() {
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(40, 24, 40, 0);

        EditText etN = createNumericField("Nitrogen (N)", editableNPerAcre);
        EditText etP = createNumericField("Phosphorus (P)", editablePPerAcre);
        EditText etK = createNumericField("Potassium (K)", editableKPerAcre);

        container.addView(etN);
        container.addView(etP);
        container.addView(etK);

        new AlertDialog.Builder(this)
                .setTitle("Edit nutrient values")
                .setView(container)
                .setPositiveButton("Save", (dialog, which) -> {
                    editableNPerAcre = parseDouble(etN.getText().toString(), editableNPerAcre);
                    editablePPerAcre = parseDouble(etP.getText().toString(), editablePPerAcre);
                    editableKPerAcre = parseDouble(etK.getText().toString(), editableKPerAcre);
                    updateCropDisplay(selectedCrop);
                    renderResultPlaceholder();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void adjustFieldSize(double deltaDisplayUnits) {
        double deltaAcre = isAcre
                ? deltaDisplayUnits
                : deltaDisplayUnits * ACRES_PER_HECTARE;

        fieldSizeAcre = Math.max(0.2d, fieldSizeAcre + deltaAcre);

        updateFieldSizeDisplay();
    }

    private void updateCropDisplay(CropPreset crop) {
        tvSelectedCrop.setText(crop.displayName);
        tvNValue.setText(formatNutrient("N", editableNPerAcre));
        tvPValue.setText(formatNutrient("P", editablePPerAcre));
        tvKValue.setText(formatNutrient("K", editableKPerAcre));
    }

    private void updateFieldSizeDisplay() {
        double displayValue = getDisplayFieldSize();
        String unitLabel = isAcre ? "Acre" : "Hectare";

        tvFieldSize.setText(
                String.format(
                        Locale.getDefault(),
                        "%.1f%n%s",
                        displayValue,
                        unitLabel
                )
        );
    }

    private double getDisplayFieldSize() {
        return isAcre ? fieldSizeAcre : fieldSizeAcre / ACRES_PER_HECTARE;
    }

    private void calculateRecommendations() {
        double fieldAcre = fieldSizeAcre;
        double nNeeded = editableNPerAcre * fieldAcre;
        double pNeeded = editablePPerAcre * fieldAcre;
        double kNeeded = editableKPerAcre * fieldAcre;

        double ureaKg = nNeeded / UREA_N_PERCENT;
        double tspKg = pNeeded / TSP_P_PERCENT;
        double mopKg = kNeeded / MOP_K_PERCENT;

        String result = String.format(
                Locale.getDefault(),
                "%s fertilizer recommendation%n%n" +
                        "Field: %.1f %s%n" +
                        "Total area: %.2f acre%n%n" +
                        "Nutrient targets%n" +
                        "N: %.1f kg%n" +
                        "P: %.1f kg%n" +
                        "K: %.1f kg%n%n" +
                        "Estimated fertilizer mix%n" +
                        "Urea: %.1f kg%n" +
                        "TSP: %.1f kg%n" +
                        "MOP: %.1f kg%n%n" +
                        "Note: This is an estimate based on crop presets.",
                selectedCrop.displayName,
                getDisplayFieldSize(),
                isAcre ? "Acre" : "Hectare",
                fieldAcre,
                nNeeded,
                pNeeded,
                kNeeded,
                ureaKg,
                tspKg,
                mopKg
        );

        tvResult.setText(result);
    }

    private void renderResultPlaceholder() {
        tvResult.setText(
                String.format(
                        Locale.getDefault(),
                        "Tap Calculate to see fertilizer recommendation for %s.",
                        selectedCrop.displayName
                )
        );
    }

    private String formatNutrient(String label, double value) {
        return String.format(Locale.getDefault(), "%s  %.1f", label, value);
    }

    private EditText createNumericField(String hint, double value) {
        EditText editText = new EditText(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 18;
        editText.setLayoutParams(params);
        editText.setHint(hint);
        editText.setText(String.format(Locale.getDefault(), "%.1f", value));
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setGravity(Gravity.START);
        return editText;
    }

    private double parseDouble(String value, double fallback) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private enum CropPreset {
        RICE("Rice", 75.0d, 25.0d, 34.5d),
        MAIZE("Maize", 120.0d, 50.0d, 40.0d),
        POTATO("Potato", 110.0d, 55.0d, 120.0d),
        WHEAT("Wheat", 100.0d, 45.0d, 30.0d);

        final String displayName;
        final double nitrogenPerAcre;
        final double phosphorusPerAcre;
        final double potassiumPerAcre;

        CropPreset(String displayName,
                   double nitrogenPerAcre,
                   double phosphorusPerAcre,
                   double potassiumPerAcre) {
            this.displayName = displayName;
            this.nitrogenPerAcre = nitrogenPerAcre;
            this.phosphorusPerAcre = phosphorusPerAcre;
            this.potassiumPerAcre = potassiumPerAcre;
        }
    }
}
