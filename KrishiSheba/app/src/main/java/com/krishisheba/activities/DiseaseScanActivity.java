package com.krishisheba.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.krishisheba.R;
import com.krishisheba.models.DiseaseResponse;
import com.krishisheba.models.Recommendation;
import com.krishisheba.network.DiseaseApiService;
import com.krishisheba.network.MLRetrofitClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiseaseScanActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private ImageView imagePreview;
    private Button btnSelectImage;
    private Button btnAnalyze;
    private TextView tvResult;

    private Bitmap selectedBitmap;
    private Uri imageUri;

    private DiseaseApiService apiService;
    private LinearProgressIndicator progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_scan);

        imagePreview = findViewById(R.id.imagePreview);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAnalyze = findViewById(R.id.btnAnalyze);
        tvResult = findViewById(R.id.tvResult);
        progressBar = findViewById(R.id.progressBar);

        apiService = MLRetrofitClient
                .getClient()
                .create(DiseaseApiService.class);

        btnSelectImage.setOnClickListener(v -> openGallery());
        btnAnalyze.setOnClickListener(v -> analyzeImage());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null) {

            imageUri = data.getData();

            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(
                        this.getContentResolver(),
                        imageUri
                );

                imagePreview.setImageBitmap(selectedBitmap);
                tvResult.setText("Image selected. Ready to analyze.");

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this,
                        "Failed to load image",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void analyzeImage() {

        if (imageUri == null) {
            Toast.makeText(this,
                    "Please select an image first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        tvResult.setText("Analyzing...");

        progressBar.setVisibility(View.VISIBLE);
        btnAnalyze.setEnabled(false);

        try {

            File file = createFileFromUri(imageUri);

            RequestBody requestFile =
                    RequestBody.create(
                            MediaType.parse("image/*"),
                            file
                    );

            MultipartBody.Part body =
                    MultipartBody.Part.createFormData(
                            "file",
                            file.getName(),
                            requestFile
                    );

            Call<DiseaseResponse> call =
                    apiService.predictDisease(body);

            call.enqueue(new Callback<DiseaseResponse>() {

                @Override
                public void onResponse(Call<DiseaseResponse> call,
                                       Response<DiseaseResponse> response) {

                    progressBar.setVisibility(View.GONE);
                    btnAnalyze.setEnabled(true);

                    if (response.isSuccessful()
                            && response.body() != null) {

                        String label = response.body().getDisplayLabel();
                        float confidence = response.body().getConfidence();
                        Recommendation recommendation = response.body().getRecommendation();
                        String resultText =
                                "Disease: " + label + "\n\n" +
                                        "Confidence: " + confidence + "\n\n" +
                                        "Cause: " + recommendation.getCause() + "\n\n" +
                                        "Treatment: " + recommendation.getTreatment() + "\n\n" +
                                        "Prevention: " + recommendation.getPrevention();
                        tvResult.setText(resultText);

                    } else {
                        tvResult.setText("Server error!");
                    }
                }

                @Override
                public void onFailure(Call<DiseaseResponse> call,
                                      Throwable t) {

                    progressBar.setVisibility(View.GONE);
                    btnAnalyze.setEnabled(true);

                    tvResult.setText("Error: " + t.getMessage());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            tvResult.setText("Failed to process image");
        }
    }

    private File createFileFromUri(Uri uri) throws IOException {

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                this.getContentResolver(),
                uri
        );

        File file = new File(getCacheDir(), "upload.jpg");

        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();

        return file;
    }
}
