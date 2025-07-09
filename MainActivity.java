package com.anhar.catering.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anhar.catering.R;
import com.anhar.catering.history.HistoryOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 100;

    List<ModelCategories> modelCategoriesList = new ArrayList<>();
    List<ModelTrending> modelTrendingList = new ArrayList<>();
    CategoriesAdapter categoriesAdapter;
    TrendingAdapter trendingAdapter;

    RecyclerView rvCategories, rvTrending;
    CardView cvHistory, cvLocation;
    TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStatusbar();
        initLayout();
        setCategories();
        setTrending();

        // Inisialisasi lokasi
        cvLocation = findViewById(R.id.cvLocation);
        tvLocation = findViewById(R.id.tvLocation);

        if (cvLocation != null && tvLocation != null) {
            cvLocation.setOnClickListener(v -> {
                // Pastikan izin lokasi sudah diberikan
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_REQUEST_CODE);
                } else {
                    // Buka activity pilih lokasi
                    Intent intent = new Intent(MainActivity.this, PickLocationActivity.class);
                    startActivityForResult(intent, LOCATION_REQUEST_CODE);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("latitude", 0);
            double lon = data.getDoubleExtra("longitude", 0);

            if (tvLocation != null) {
                String latitude = String.format("%.6f", lat);
                String longitude = String.format("%.6f", lon);
                tvLocation.setText("Lat: " + latitude + ", Lng: " + longitude);
                Toast.makeText(this, "Lokasi berhasil dipilih", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initLayout() {
        cvHistory = findViewById(R.id.cvHistory);
        cvHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryOrderActivity.class);
            startActivity(intent);
        });

        rvCategories = findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 3));
        rvCategories.setHasFixedSize(true);

        rvTrending = findViewById(R.id.rvTrending);
        rvTrending.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvTrending.setHasFixedSize(true);
    }

    private void setCategories() {
        modelCategoriesList.add(new ModelCategories(R.drawable.ic_complete, "Complete Package"));
        modelCategoriesList.add(new ModelCategories(R.drawable.ic_saving, "Saving Package"));
        modelCategoriesList.add(new ModelCategories(R.drawable.ic_healthy, "Healthy Package"));
        modelCategoriesList.add(new ModelCategories(R.drawable.ic_fast, "FastFood"));
        modelCategoriesList.add(new ModelCategories(R.drawable.ic_event, "Event Packages"));
        modelCategoriesList.add(new ModelCategories(R.drawable.ic_more_food, "Others"));

        categoriesAdapter = new CategoriesAdapter(this, modelCategoriesList);
        rvCategories.setAdapter(categoriesAdapter);
    }

    private void setTrending() {
        modelTrendingList.add(new ModelTrending(R.drawable.complete_1, "Menu 1", "2.200 disukai"));
        modelTrendingList.add(new ModelTrending(R.drawable.complete_2, "Menu 2", "1.220 disukai"));
        modelTrendingList.add(new ModelTrending(R.drawable.complete_3, "Menu 3", "345 disukai"));
        modelTrendingList.add(new ModelTrending(R.drawable.complete_4, "Menu 4", "590 disukai"));

        trendingAdapter = new TrendingAdapter(this, modelTrendingList);
        rvTrending.setAdapter(trendingAdapter);
    }

    public void setStatusbar() {
        if (Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(@NonNull Activity activity, final int bits, boolean on) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        if (on) {
            layoutParams.flags |= bits;
        } else {
            layoutParams.flags &= ~bits;
        }
        window.setAttributes(layoutParams);
    }
}
