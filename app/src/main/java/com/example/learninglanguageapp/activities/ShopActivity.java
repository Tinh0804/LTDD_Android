
package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

    private static final int PAYMENT_REQUEST_CODE = 100;

    private ShopViewModel viewModel;

    // Diamond Balance
    private TextView tvDiamondCount;

    // Diamond Packages
    private CardView cardDiamond1000, cardDiamond2000, cardDiamond3000;

    // Heart Packages
    private CardView cardHeart5, cardHeart10;

    // Experience Packages
    private CardView cardXp100, cardXp250, cardXp500;

    // Streak Freeze
    private CardView cardStreakFreeze;
    private Button btnBuyStreakFreeze;

    // Unlock Course
    private CardView cardUnlockCourse;
    private Button btnUnlockCourse;

    // Close button
    private ImageView btnClose;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        viewModel = new ViewModelProvider(this).get(ShopViewModel.class);

        initViews();
        setupObservers();
        setupClickListeners();
        loadInitialBalance();
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar);
        // Diamond Balance
        tvDiamondCount = findViewById(R.id.tvDiamondCount);

        // Diamond Packages
        cardDiamond1000 = findViewById(R.id.cardDiamond1000);
        cardDiamond2000 = findViewById(R.id.cardDiamond2000);
        cardDiamond3000 = findViewById(R.id.cardDiamond3000);

        // Heart Packages
        cardHeart5 = findViewById(R.id.cardHeart5);
        cardHeart10 = findViewById(R.id.cardHeart10);

        // Experience Packages
        cardXp100 = findViewById(R.id.cardXp100);
        cardXp250 = findViewById(R.id.cardXp250);
        cardXp500 = findViewById(R.id.cardXp500);

        // Streak Freeze
        cardStreakFreeze = findViewById(R.id.cardStreakFreeze);
        btnBuyStreakFreeze = findViewById(R.id.btnBuyStreakFreeze);

        // Unlock Course
        cardUnlockCourse = findViewById(R.id.cardUnlockCourse);
        btnUnlockCourse = findViewById(R.id.btnUnlockCourse);

        // Close button
        btnClose = findViewById(R.id.btnClose);
    }

    private void loadInitialBalance() {
        // Load balance từ Intent hoặc ViewModel
        int diamond = getIntent().getIntExtra("diamond", 957);
        tvDiamondCount.setText(diamond + " kim cương");

        // Hoặc load từ ViewModel
        // viewModel.loadBalance();
    }

    private void setupObservers() {

        viewModel.getBalanceLiveData().observe(this, balance -> {
            if (balance != null) {
                tvDiamondCount.setText(balance + " kim cương");
            }
        });

        viewModel.getPurchaseSuccessLiveData().observe(this, isSuccess -> {
            if (isSuccess != null && isSuccess) {
                Toast.makeText(this, "Mua hàng thành công!", Toast.LENGTH_SHORT).show();
                viewModel.clearPurchaseSuccess();
            }
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupClickListeners() {
        // Close button
        btnClose.setOnClickListener(v -> finish());

        // Diamond Packages - Mua bằng tiền thật
        cardDiamond1000.setOnClickListener(v ->
                purchaseWithMoney(new PackagePayment("diamond_1000", 1000, 2*25000, "diamond")));

        cardDiamond2000.setOnClickListener(v ->
                purchaseWithMoney(new PackagePayment("diamond_2000", 2000, 4*25000, "diamond")));

        cardDiamond3000.setOnClickListener(v ->
                purchaseWithMoney(new PackagePayment("diamond_3000", 3000, 6*25000, "diamond")));

        // Heart Packages - Mua bằng kim cương
        cardHeart5.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("heart_5", 5, 50, "heart")));

        cardHeart10.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("heart_10", 10, 90, "heart")));

        // Experience Packages - Mua bằng kim cương
        cardXp100.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("xp_100", 100, 30, "xp")));

        cardXp250.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("xp_250", 250, 70, "xp")));

        cardXp500.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("xp_500", 500, 120, "xp")));

        // Streak Freeze - Mua bằng kim cương
        btnBuyStreakFreeze.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("streak_freeze", 1, 100, "streak_freeze")));

        cardStreakFreeze.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("streak_freeze", 1, 100, "streak_freeze")));

        // Unlock Course - Mua bằng kim cương
        btnUnlockCourse.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("unlock_course", 1, 500, "unlock_course")));

        cardUnlockCourse.setOnClickListener(v ->
                purchaseWithDiamond(new PackagePayment("unlock_course", 1, 500, "unlock_course")));
    }

    private void purchaseWithMoney(PackagePayment pkg) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra("selected_package", pkg);
        startActivityForResult(intent, PAYMENT_REQUEST_CODE);
    }

    private void purchaseWithDiamond(PackagePayment pkg) {
        // Lấy số kim cương hiện tại
        int currentDiamond = getCurrentDiamondBalance();
        int requiredDiamond = (int) pkg.getPrice();

        if (currentDiamond < requiredDiamond) {
            Toast.makeText(this,
                    "Không đủ kim cương! Cần " + requiredDiamond + " kim cương.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Hiển thị dialog xác nhận
        showPurchaseConfirmationDialog(pkg, requiredDiamond);
    }

    private void showPurchaseConfirmationDialog(PackagePayment pkg, int requiredDiamond) {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Xác nhận mua hàng")
                .setMessage("Bạn có muốn mua " + pkg.getName() +
                        " với giá " + requiredDiamond + " kim cương?")
                .setPositiveButton("Mua", (dialog, which) -> {
                    // Gọi ViewModel để xử lý giao dịch
                    viewModel.purchaseWithDiamond(pkg);
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private int getCurrentDiamondBalance() {
        // Lấy từ TextView hoặc ViewModel
        String diamondText = tvDiamondCount.getText().toString();
        try {
            return Integer.parseInt(diamondText.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYMENT_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                String transactionId = data.getStringExtra("transaction_id");
                String status = data.getStringExtra("payment_status");
                int diamondAmount = data.getIntExtra("diamond_amount", 0);

                if ("success".equals(status)) {
                    // Reload số dư kim cương sau khi thanh toán thành công
                    reloadBalance();
                    
                    Toast.makeText(this,
                            "Thanh toán thành công! Bạn nhận được " + diamondAmount + " kim cương 💎",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this,
                            "Thanh toán thất bại hoặc bị hủy",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this,
                        "Thanh toán bị hủy",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload balance khi quay lại activity
        reloadBalance();
    }

    /**
     * Reload số dư kim cương từ server hoặc local storage
     */
    private void reloadBalance() {
        // Gọi ViewModel để load lại balance từ server
        viewModel.loadBalance();
    }
}