package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.UIModel.PackagePayment;
import com.example.learninglanguageapp.viewmodels.ShopViewModel;

public class ShopActivity extends AppCompatActivity {

    private ShopViewModel viewModel;

    // Views
    private TextView tvDiamondCount;
    private CardView cardDiamond1000, cardDiamond2000, cardDiamond3000;
    private CardView cardHeart;
    private ImageView btnClose;
    private TextView btnRedeemHeart;
    private ProgressBar progressBar;

    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        // Get userId từ SharedPreferences hoặc Intent
        userId = getUserId();

        // Init ViewModel
        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        viewModel.setUserId(userId);

        initViews();
        setupObservers();
        setupClickListeners();

        // Load balance
        viewModel.loadBalance();
    }

    private void initViews() {
        tvDiamondCount = findViewById(R.id.tvDiamondCount);
        cardDiamond1000 = findViewById(R.id.cardDiamond1000);
        cardDiamond2000 = findViewById(R.id.cardDiamond2000);
        cardDiamond3000 = findViewById(R.id.cardDiamond3000);
        cardHeart = findViewById(R.id.cardHeart);
        btnClose = findViewById(R.id.btnClose);
        btnRedeemHeart = findViewById(R.id.btnRedeemHeart);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupObservers() {
        // Observe balance
        viewModel.getBalanceLiveData().observe(this, balance -> {
            if (balance != null) {
                tvDiamondCount.setText(balance + " diamonds");
            }
        });

        // Observe loading
        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });

        // Observe error
        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> finish());

        cardDiamond1000.setOnClickListener(v -> {
            PackagePayment pkg = new PackagePayment("diamond_1000", 1000, 2.00, "diamond");
            viewModel.setSelectedPackage(pkg);
            navigateToPayment();
        });

        cardDiamond2000.setOnClickListener(v -> {
            PackagePayment pkg = new PackagePayment("diamond_2000", 2000, 4.00, "diamond");
            viewModel.setSelectedPackage(pkg);
            navigateToPayment();
        });

        cardDiamond3000.setOnClickListener(v -> {
            PackagePayment pkg = new PackagePayment("diamond_3000", 3000, 6.00, "diamond");
            viewModel.setSelectedPackage(pkg);
            navigateToPayment();
        });

        btnRedeemHeart.setOnClickListener(v -> {
            // Show dialog to enter heart code
            showRedeemHeartDialog();
        });
    }

    private void navigateToPayment() {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("userId", userId);
        startActivity(intent);
    }

    private void showRedeemHeartDialog() {
        // TODO: Implement dialog to enter heart code
        Toast.makeText(this, "Redeem heart code feature", Toast.LENGTH_SHORT).show();
    }

    private int getUserId() {
        // Get from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return prefs.getInt("user_id", 1); // Default userId = 1
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload balance khi quay lại màn hình
        viewModel.loadBalance();
    }
}