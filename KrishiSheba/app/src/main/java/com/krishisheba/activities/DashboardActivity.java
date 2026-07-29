package com.krishisheba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.button.MaterialButton;
import com.krishisheba.R;
import com.krishisheba.models.WeatherResponse;
import com.krishisheba.network.RetrofitClient;
import com.krishisheba.network.WeatherApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private CardView cardDiseaseScan;
    private MaterialButton btnTakePicture;
    private CardView cardWeatherSummary;
    private TextView tvTemperature;
    private CardView cardFertilizerCalculator;
    private CardView cardPesticideCalculator;
    private CardView cardFarmingCalculator;
    private CardView cardLibraryCrops;
    private CardView cardCultivationTips;
    private CardView cardPestsDiseases;
    private CardView cardPestsDiseasesAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupListeners();
        loadWeather();
    }

    private void initViews() {

        cardDiseaseScan = findViewById(R.id.cardDiseaseScan);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        cardWeatherSummary = findViewById(R.id.cardWeatherSummary);
        tvTemperature = findViewById(R.id.tvTemperature);
        cardFertilizerCalculator = findViewById(R.id.cardFertilizerCalculator);
        cardPesticideCalculator = findViewById(R.id.cardPesticideCalculator);
        cardFarmingCalculator = findViewById(R.id.cardFarmingCalculator);
        cardLibraryCrops = findViewById(R.id.cardLibraryCrops);
        cardCultivationTips = findViewById(R.id.cardCultivationTips);
        cardPestsDiseases = findViewById(R.id.cardPestsDiseases);
        cardPestsDiseasesAlert = findViewById(R.id.cardAlert);

    }

    private void setupListeners() {

        cardDiseaseScan.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    DiseaseScanActivity.class
            );
            startActivity(intent);
        });

        btnTakePicture.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    DiseaseScanActivity.class
            );
            startActivity(intent);
        });

        cardWeatherSummary.setOnClickListener(v -> {
            Intent intent = new Intent(
                    DashboardActivity.this,
                    WeatherActivity.class
            );
            startActivity(intent);
        });

        cardFertilizerCalculator.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                DashboardActivity.this,
                                FertilizerCalculatorActivity.class
                        )
                ));

        cardPesticideCalculator.setOnClickListener(v ->
                showComingSoon());

        cardFarmingCalculator.setOnClickListener(v ->
                showComingSoon());

        cardLibraryCrops.setOnClickListener(v ->
                startActivity(
                        new Intent(
                                DashboardActivity.this,
                                CropActivity.class
                        )
                ));

        cardCultivationTips.setOnClickListener(v ->
                showComingSoon());

        cardPestsDiseases.setOnClickListener(v ->
                showComingSoon());

        cardPestsDiseasesAlert.setOnClickListener(v ->
                showComingSoon());
    }

    private void loadWeather() {

        WeatherApi api =
                RetrofitClient.getClient()
                        .create(WeatherApi.class);

        api.getWeather(
                23.8103,
                90.4125,
                true,
                "temperature_2m_max,temperature_2m_min",
                "auto"
        ).enqueue(new Callback<WeatherResponse>() {

            @Override
            public void onResponse(Call<WeatherResponse> call,
                                   Response<WeatherResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    double temp =
                            response.body()
                                    .getCurrent_weather()
                                    .getTemperature();

                    tvTemperature.setText(
                            String.format("%.1f°C", temp)
                    );

                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call,
                                  Throwable t) {

                tvTemperature.setText("--°C");
            }
        });
    }

    private void showComingSoon() {
        Toast.makeText(this, "Coming Soon...", Toast.LENGTH_SHORT).show();
    }
}