package com.krishisheba.activities;

import android.os.Bundle;
import android.widget.SearchView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.krishisheba.R;
import com.krishisheba.adapter.CropAdapter;
import com.krishisheba.database.DBHelper;
import com.krishisheba.models.Crop;

import java.util.ArrayList;

public class CropActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    android.widget.ImageButton btnBack;

    CropAdapter adapter;
    ArrayList<Crop> cropList;
    ArrayList<Crop> filteredList;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        recyclerView = findViewById(R.id.recyclerViewCrops);
        searchView = findViewById(R.id.searchView);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        dbHelper = new DBHelper(this);
        dbHelper.seedCropData();

        cropList = dbHelper.getAllCrops();
        filteredList = new ArrayList<>(cropList);

        adapter = new CropAdapter(this, filteredList);

        recyclerView.setLayoutManager(
                new GridLayoutManager(this, 2));

        recyclerView.setAdapter(adapter);

        setupSearch();
        applyInitialFilter();
    }

    private void setupSearch() {

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {

                        filterCrops(newText);
                        return true;
                    }
                });
    }

    private void filterCrops(String text) {

        filteredList.clear();

        if(text.isEmpty()) {
            filteredList.addAll(cropList);
        } else {

            for(Crop crop : cropList) {

                if(crop.getName()
                        .toLowerCase()
                        .contains(text.toLowerCase())) {

                    filteredList.add(crop);
                }
            }
        }

        adapter.updateList(filteredList);
    }

    private void applyInitialFilter() {
        Intent intent = getIntent();
        String initialFilter = intent.getStringExtra("initial_filter");

        if (initialFilter != null && !initialFilter.trim().isEmpty()) {
            searchView.setQuery(initialFilter, false);
            filterCrops(initialFilter);
        }
    }
}
