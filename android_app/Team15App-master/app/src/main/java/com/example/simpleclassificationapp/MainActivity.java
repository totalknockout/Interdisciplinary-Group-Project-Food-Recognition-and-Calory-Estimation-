package com.example.simpleclassificationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE_PERMISSIONS = 1001;
    private final String[] REQUIRED_PERMISSIONS = new String[]{Manifest.permission.CAMERA};
    private String result;
    private boolean next_page_opened = false;
    PreviewView mPreviewView;
    TextView tvResults;
    Classifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPreviewView = findViewById(R.id.viewFinder);
        tvResults = findViewById(R.id.tvResults);

        classifier = new Classifier(this);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS);
        }
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider>
                cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                            bindPreview(cameraProvider);
                        } catch (ExecutionException | InterruptedException e) {
                        }
                    }
                },
                ActivityCompat.getMainExecutor(this)
        );
    }

    void bindPreview(ProcessCameraProvider cameraProvider) {
        ImageCapture.Builder builder = new ImageCapture.Builder();
        ImageCapture imageCapture = builder.build();

        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(mPreviewView.createSurfaceProvider());

        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(
                CameraSelector.LENS_FACING_BACK).build();
        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ActivityCompat.getMainExecutor(this),
                new ImageAnalysis.Analyzer() {
                    @Override
                    public void analyze(@NonNull ImageProxy image) {
                        result = classifier.classify(image);
                        tvResults.setText(result);
                        image.close();
                    }
                });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageAnalysis.setAnalyzer(ActivityCompat.getMainExecutor(MainActivity.this),
                        new ImageAnalysis.Analyzer() {
                            @Override
                            public void analyze(@NonNull ImageProxy image) {
                                String result2;
                                result2 = classifier.classify(image);
                                if (result.split(" ")[0].equals(result2.split(" ")[0])) {

                                        HashMap<String, Integer> lookupTable = Utils.initLookupTable();
                                        if(lookupTable.containsKey(result2.split(" ")[0])) {

                                            if(!next_page_opened) {
                                                next_page_opened = true;
                                                Log.d("INTENT", "I OPEN X MANY TIMES");
                                                Intent myIntent = new Intent(MainActivity.this, CalorieActivity.class);
                                                myIntent.putExtra("classificationResult", result2.split(" ")[0]);
                                                myIntent.putExtra("caloriesResult", lookupTable.get(result2.split(" ")[0]).toString());
                                                startActivity(myIntent);
                                            }
                                    } else {
                                        Log.d("KEY_NOT_IN_DICT", "We don't have that dish in our lookup Table");
                                    }
                                }
                                image.close();
                            }
                        });
            }
        }, 3000);


        Camera camera = cameraProvider.bindToLifecycle(this, cameraSelector,
                preview, imageAnalysis, imageCapture);
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Please, give access to camera",
                        Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
    }

    @Override
    public void onResume() {
        tvResults.setText("");
        super.onResume();
    }
}