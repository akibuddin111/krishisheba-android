package com.krishisheba.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.krishisheba.R;

public class CropDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_detail);

        TextView tvName =
                findViewById(R.id.tvName);

        TextView tvSeason =
                findViewById(R.id.tvSeason);

        TextView tvSoil =
                findViewById(R.id.tvSoil);

        TextView tvDescription =
                findViewById(R.id.tvDescription);

        tvName.setText(
                getIntent().getStringExtra("name"));

        tvSeason.setText(
                "Season: " +
                        getIntent().getStringExtra("season"));

        tvSoil.setText(
                "Soil: " +
                        getIntent().getStringExtra("soil"));

        tvDescription.setText(
                getIntent().getStringExtra("description"));
    }
}