//package com.example.learninglanguageapp.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.cardview.widget.CardView;
//import androidx.lifecycle.ViewModelProvider;
//
//import com.example.learninglanguageapp.R;
//import com.example.learninglanguageapp.models.UIModel.PackagePayment;
//import com.example.learninglanguageapp.viewmodels.ShopViewModel;
//
//public class PaymentActivity extends AppCompatActivity {
//
//    private ShopViewModel viewModel;
//
//    private CardView cardVNPay, cardMomo;
//    private ImageView btnBack, iconCheckVNPay, iconCheckMomo;
//    private Button btnContinue;
//    private ProgressBar progressBar;
//
//    private String selectedPaymentMethod = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_payment);
//
//        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);
//
//        // Lấy gói được chọn từ ShopActivity
//        PackagePayment selectedPackage = (PackagePayment) getIntent().getSerializableExtra("selected_package");
//        if (selectedPackage != null) {
//            viewModel.setSelectedPackage(selectedPackage);
//        }
//
//        initViews();
//        setupObservers();
//        setupClickListeners();
//    }
//
//
//    private void initViews() {
//        cardVNPay = findViewById(R.id.cardVNPay);
//        cardMomo = findViewById(R.id.cardMomo);
//        btnBack = findViewById(R.id.btnBack);
//        iconCheckVNPay = findViewById(R.id.iconCheckVNPay);
//        iconCheckMomo = findViewById(R.id.iconCheckMomo);
//        btnContinue = findViewById(R.id.btnContinue);
//        progressBar = findViewById(R.id.progressBar1);
//    }
//
//    private void setupObservers() {
//        viewModel.getPaymentLiveData().observe(this, response -> {
//            if (response != null && response.isSuccess()) {
//                Intent intent = new Intent(this, WebViewActivity.class);
//                intent.putExtra("payment_url", response.getPaymentUrl());
//                intent.putExtra("payment_method", selectedPaymentMethod);
//                intent.putExtra("transaction_id", response.getTransactionId());
//                startActivityForResult(intent, 100);
//            }
//        });
//
//        viewModel.getLoadingLiveData().observe(this, isLoading -> {
//            progressBar.setVisibility(android.view.View.GONE);
//            btnContinue.setEnabled(!isLoading);
//        });
//
//        viewModel.getErrorLiveData().observe(this, error -> {
//            if (error != null && !error.isEmpty()) {
//                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void setupClickListeners() {
//        btnBack.setOnClickListener(v -> finish());
//
//        cardVNPay.setOnClickListener(v -> selectPaymentMethod("vnpay"));
//        cardMomo.setOnClickListener(v -> selectPaymentMethod("momo"));
//
//        btnContinue.setOnClickListener(v -> {
//            if (selectedPaymentMethod.isEmpty()) {
//                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
//                return;
//            }
//            viewModel.createPayment(selectedPaymentMethod);
//        });
//    }
//
//    private void selectPaymentMethod(String method) {
//        selectedPaymentMethod = method;
//        iconCheckVNPay.setVisibility(method.equals("vnpay") ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
//        iconCheckMomo.setVisibility(method.equals("momo") ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 100) {
//            setResult(resultCode, data);
//            finish();
//        }
//    }
//}
package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Import View for visibility constants
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.UIModel.PackagePayment;
import com.example.learninglanguageapp.viewmodels.ShopViewModel;

public class PaymentActivity extends AppCompatActivity {

    private ShopViewModel viewModel;

    private CardView cardVNPay, cardMomo;
    private ImageView btnBack, iconCheckVNPay, iconCheckMomo;
    private Button btnContinue;
    private ProgressBar progressBar;

    private String selectedPaymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        // Lấy gói được chọn từ ShopActivity
        PackagePayment selectedPackage = (PackagePayment) getIntent().getSerializableExtra("selected_package");
        if (selectedPackage != null) {
            viewModel.setSelectedPackage(selectedPackage);
        }

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
        // Đảm bảo R.id.progressBar tồn tại trong layout!
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupObservers() {
        // CẬP NHẬT LOGIC: Chỉ cần kiểm tra paymentUrl có hợp lệ là có thể điều hướng.
        viewModel.getPaymentLiveData().observe(this, response -> {
            if (response != null && response.getPaymentUrl() != null && !response.getPaymentUrl().isEmpty()) {

                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("payment_url", response.getPaymentUrl());
                intent.putExtra("payment_method", selectedPaymentMethod);
                // Giả sử PaymentResponse chứa TransactionId hoặc bạn đã trích xuất nó
                intent.putExtra("transaction_id", response.getTransactionId());
                
                // Lấy số kim cương từ gói đã chọn và truyền sang WebViewActivity
                PackagePayment selectedPackage = (PackagePayment) getIntent().getSerializableExtra("selected_package");
                if (selectedPackage != null && selectedPackage.getType().equals("diamond")) {
                    intent.putExtra("diamond_amount", selectedPackage.getValue());
                }

                startActivityForResult(intent, 100);
            } else if (response != null) {
                // Xử lý trường hợp 200 OK nhưng thiếu URL thanh toán
                Toast.makeText(this, "Server responded but payment URL is missing. Please contact support.", Toast.LENGTH_LONG).show();
            }
        });

        viewModel.getLoadingLiveData().observe(this, isLoading -> {
            // Đã thêm null check cho ProgressBar (khắc phục lỗi crash trước đó)
            if (progressBar != null) {
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
            btnContinue.setEnabled(!isLoading);
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        cardVNPay.setOnClickListener(v -> selectPaymentMethod("vnpay"));
        cardMomo.setOnClickListener(v -> selectPaymentMethod("momo"));

        btnContinue.setOnClickListener(v -> {
            if (selectedPaymentMethod.isEmpty()) {
                Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            // ViewModel sẽ gọi Repository tương ứng (createVnPay hoặc createMomo)
            viewModel.createPayment(selectedPaymentMethod);
        });
    }

    private void selectPaymentMethod(String method) {
        selectedPaymentMethod = method;

        if (iconCheckVNPay != null && iconCheckMomo != null) {
            iconCheckVNPay.setVisibility(method.equals("vnpay") ? View.VISIBLE : View.INVISIBLE);
            iconCheckMomo.setVisibility(method.equals("momo") ? View.VISIBLE : View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            // Xử lý kết quả trả về từ WebViewActivity
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}