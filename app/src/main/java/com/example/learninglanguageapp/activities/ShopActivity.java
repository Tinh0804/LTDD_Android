package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
    private TextView tvDiamondCount;
    private CardView cardDiamond1000, cardDiamond2000, cardDiamond3000, cardHeart;
    private ImageView btnClose;
    private TextView btnRedeemHeart;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        initViews();
        setupObservers();
        setupClickListeners();
    }
    private void initViews() {
        tvDiamondCount = findViewById(R.id.tvDiamondCount);
        int diamond = getIntent().getIntExtra("diamond", 20);
        tvDiamondCount.setText(diamond + " diamonds");
        cardDiamond1000 = findViewById(R.id.cardDiamond1000);
        cardDiamond2000 = findViewById(R.id.cardDiamond2000);
        cardDiamond3000 = findViewById(R.id.cardDiamond3000);
        cardHeart = findViewById(R.id.cardHeart);
        btnClose = findViewById(R.id.btnClose);
        btnRedeemHeart = findViewById(R.id.btnRedeemHeart);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupObservers() {
        viewModel.getBalanceLiveData().observe(this, balance -> {
            if (balance != null) {
                tvDiamondCount.setText(balance + " diamonds");
            }
        });

        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? android.view.View.VISIBLE : android.view.View.GONE);
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnClose.setOnClickListener(v -> finish());

        cardDiamond1000.setOnClickListener(v -> selectPackageAndPay(new PackagePayment("diamond_1000", 1000, 25000 * 2, "diamond")));
        cardDiamond2000.setOnClickListener(v -> selectPackageAndPay(new PackagePayment("diamond_2000", 2000, 25000 * 4, "diamond")));
        cardDiamond3000.setOnClickListener(v -> selectPackageAndPay(new PackagePayment("diamond_3000", 3000, 25000 * 6, "diamond")));
        cardHeart.setOnClickListener(v -> selectPackageAndPay(new PackagePayment("refill_heart", 0, 20000, "heart")));

        btnRedeemHeart.setOnClickListener(v -> Toast.makeText(this, "Redeem heart feature", Toast.LENGTH_SHORT).show());
    }

    private void selectPackageAndPay(PackagePayment pkg) {
        // Truyền gói qua Intent
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("selected_package",  pkg); // PackagePayment phải implement Serializable
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if (resultCode == RESULT_OK && data != null) {
                String transactionId = data.getStringExtra("transaction_id");
                String status = data.getStringExtra("payment_status");

                if ("success".equals(status)) {
                    if (transactionId != null) {
                        Toast.makeText(this, "Payment successful! Transaction: " + transactionId, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Payment successful! Transaction ID missing", Toast.LENGTH_LONG).show();
                    }
                    // viewModel.loadBalance(); // load lại số dư nếu cần
                } else {
                    Toast.makeText(this, "Payment failed or cancelled", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Payment cancelled or failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
