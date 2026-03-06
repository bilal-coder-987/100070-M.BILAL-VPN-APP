package com.example.bilal.ui.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.mlkit.vision.MlKitAnalyzer;
import androidx.camera.view.CameraController;
import androidx.camera.view.LifecycleCameraController;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.bilal.data.remote.NutritionApiService;
import com.example.bilal.data.remote.model.FoodResponse;
import com.example.bilal.databinding.ActivityScannerBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScannerActivity extends AppCompatActivity {

    private ActivityScannerBinding binding;
    private static final int PERMISSION_CAMERA_REQUEST = 1;
    private ExecutorService cameraExecutor;
    private boolean isScanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScannerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        cameraExecutor = Executors.newSingleThreadExecutor();

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA_REQUEST);
        }
    }

    private void startCamera() {
        LifecycleCameraController cameraController = new LifecycleCameraController(this);
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();
        BarcodeScanner barcodeScanner = BarcodeScanning.getClient(options);

        cameraController.setImageAnalysisAnalyzer(
                ContextCompat.getMainExecutor(this),
                new MlKitAnalyzer(
                        java.util.Collections.singletonList(barcodeScanner),
                        CameraController.COORDINATE_SYSTEM_VIEW_REFERENCED,
                        ContextCompat.getMainExecutor(this),
                        result -> {
                            List<Barcode> barcodes = result.getValue(barcodeScanner);
                            if (barcodes != null && !barcodes.isEmpty() && !isScanning) {
                                isScanning = true;
                                String barcodeValue = barcodes.get(0).getRawValue();
                                fetchFoodInfo(barcodeValue);
                            }
                        }
                )
        );

        cameraController.bindToLifecycle(this);
        binding.previewView.setController(cameraController);
    }

    private void fetchFoodInfo(String barcode) {
        binding.progressBar.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://world.openfoodfacts.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NutritionApiService service = retrofit.create(NutritionApiService.class);
        service.getProductInfo(barcode).enqueue(new Callback<FoodResponse>() {
            @Override
            public void onResponse(Call<FoodResponse> call, Response<FoodResponse> response) {
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null && response.body().status == 1) {
                    FoodResponse.Product product = response.body().product;
                    String message = String.format("Product: %s\nKcal: %.1f\nProtein: %.1fg\nCarbs: %.1fg\nFat: %.1fg",
                            product.productName,
                            product.nutriments.calories,
                            product.nutriments.proteins,
                            product.nutriments.carbohydrates,
                            product.nutriments.fat);
                    
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ScannerActivity.this);
                    builder.setTitle("Food Details")
                            .setMessage(message)
                            .setPositiveButton("OK", (dialog, which) -> {
                                isScanning = false;
                                finish();
                            })
                            .show();
                } else {
                    Toast.makeText(ScannerActivity.this, "Product not found", Toast.LENGTH_SHORT).show();
                    isScanning = false;
                }
            }

            @Override
            public void onFailure(Call<FoodResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ScannerActivity.this, "API Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                isScanning = false;
            }
        });
    }

    private boolean allPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CAMERA_REQUEST) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera permission required", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }
}
