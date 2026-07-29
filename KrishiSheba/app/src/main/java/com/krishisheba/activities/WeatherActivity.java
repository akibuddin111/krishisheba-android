package com.krishisheba.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.krishisheba.R;
import com.krishisheba.models.WeatherResponse;
import com.krishisheba.utils.CropRecommendationEngine;
import com.krishisheba.network.RetrofitClient;
import com.krishisheba.network.WeatherApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    private TextView tvTemperature;
    private TextView tvForecast;
    private TextView tvWind;
    private TextView tvCrops;
    private TextView tvTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        tvTemperature = findViewById(R.id.tvTemperature);
        tvForecast = findViewById(R.id.tvForecast);
        tvWind = findViewById(R.id.tvWind);
        tvCrops = findViewById(R.id.tvCrops);
        tvTip = findViewById(R.id.tvTip);

        loadWeather();
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

                if(response.isSuccessful() && response.body() != null){

                    WeatherResponse data = response.body();

                    // CURRENT WEATHER
                    double temp = data.getCurrent_weather().getTemperature();
                    double wind = data.getCurrent_weather().getWindspeed();

                    // 🌾 Crop Recommendation
                    List<String> recommended =
                            CropRecommendationEngine.getRecommendedCrops(temp, wind);

                    StringBuilder cropText = new StringBuilder();

                    for (String crop : recommended) {
                        cropText.append("✔ ").append(crop).append("\n");
                    }

                    tvCrops.setText(cropText.toString());

                    tvTemperature.setText("Temperature: " + temp + "°C");
                    tvWind.setText("Wind Speed: " + wind + " km/h");

                    // AGRO TIP
                    if(temp > 35){
                        tvTip.setText("Agro Tip: Irrigate crops immediately.");
                    } else if(temp < 20){
                        tvTip.setText("Agro Tip: Protect crops from cold.");
                    } else {
                        tvTip.setText("Agro Tip: Good farming conditions.");
                    }

                    // FORECAST (simple display)
                    StringBuilder forecast = new StringBuilder();

                    if(data.getDaily() != null){

                        for(int i = 0; i < data.getDaily().getTime().size(); i++){

                            forecast.append("Day ").append(i+1)
                                    .append(": ")
                                    .append(data.getDaily().getTemperature_2m_min().get(i))
                                    .append("°C - ")
                                    .append(data.getDaily().getTemperature_2m_max().get(i))
                                    .append("°C\n");
                        }

                        tvForecast.setText(forecast.toString());
                    }

                } else {
                    tvTip.setText("API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

                tvTip.setText("ERROR: " + t.toString());

                t.printStackTrace();
            }
        });
    }
}