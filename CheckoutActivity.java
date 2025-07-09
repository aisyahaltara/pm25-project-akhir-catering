package com.anhar.catering.main;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.anhar.catering.R;

public class CheckoutActivity extends AppCompatActivity {

    private TextView tvTotal, tvJumlahPorsi;
    private WebView webView;
    private Button btnBayar;

    private int totalHarga;
    private int jumlahPorsi;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Inisialisasi view
        tvTotal = findViewById(R.id.tvTotalPrice);
        tvJumlahPorsi = findViewById(R.id.tvJumlahPorsi);
        webView = findViewById(R.id.webView);
        btnBayar = findViewById(R.id.btnBayar);

        // Ambil data dari intent
        totalHarga = getIntent().getIntExtra("total_harga", 0);
        jumlahPorsi = getIntent().getIntExtra("jumlah_porsi", 0);

        // Tampilkan data
        tvJumlahPorsi.setText(jumlahPorsi + " items");
        tvTotal.setText("Rp " + totalHarga);

        // Konfigurasi WebView
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Tombol bayar
        btnBayar.setOnClickListener(v -> {
            String snapToken = "YOUR_GENERATED_SNAP_TOKEN"; // ganti dari backend jika tersedia
            String midtransUrl = "https://app.sandbox.midtrans.com/snap/v2/vtweb/" + snapToken;
            webView.loadUrl(midtransUrl);
        });
    }
}
