package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.viewmodels.ShopViewModel;

public class PaymentActivity extends AppCompatActivity {

    private ShopViewModel viewModel;

    // Views
    private CardView cardVNPay, cardMomo;
    private ImageView btnBack, iconCheckVNPay, iconCheckMomo;
    private Button btnContinue;
    private ProgressBar progressBar;

    private String selectedPaymentMethod = "";
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Get userId from Intent
        userId = getIntent().getIntExtra("userId", 1);

        // Init ViewModel (share với ShopActivity)
        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);
        viewModel.setUserId(userId);

        initViews();
        setupObservers();
        setupClickListeners();
    }

    private void initViews() {
        cardVNPay = findViewById(R.id.cardVNPay);
        cardMomo = findViewById(R.id.cardMomo);
        btnBack = findViewById(R.id.btnBack);
        iconCheckVNPay = findViewById(R.id.iconCheckVNPay);
        iconCheckMomo = findViewById(R.id.iconCheckMomo);
        btnContinue = findViewById(R.id.btnContinue);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupObservers() {
        // Observe payment response
        viewModel.getPaymentLiveData().observe(this, response -> {
            if (response != null && response.isSuccess()) {
                // Navigate to WebView
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("payment_url", response.getPaymentUrl());
                intent.putExtra("payment_method", selectedPaymentMethod);
                intent.putExtra("transaction_id", response.getTransactionId());
                startActivityForResult(intent, 100);
            }
        });

        // Observe loading
        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            if (isLoading != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                btnContinue.setEnabled(!isLoading);
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
        btnBack.setOnClickListener(v -> finish());

        cardVNPay.setOnClickListener(v -> {
            selectPaymentMethod("vnpay");
            iconCheckVNPay.setVisibility(View.VISIBLE);
            iconCheckMomo.setVisibility(View.INVISIBLE);
        });

        cardMomo.setOnClickListener(v -> {
            selectPaymentMethod("momo");
            iconCheckMomo.setVisibility(View.VISIBLE);
            iconCheckVNPay.setVisibility(View.INVISIBLE);
        });

        btnContinue.setOnClickListener(v -> {
            if (selectedPaymentMethod.isEmpty()) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            processPayment();
        });
    }

    private void selectPaymentMethod(String method) {
        selectedPaymentMethod = method;
    }

    private void processPayment() {
        viewModel.createPayment(selectedPaymentMethod);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                // Payment success
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show();

                // Quay về ShopActivity
                finish();
            } else {
                // Payment failed or cancelled
                Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}