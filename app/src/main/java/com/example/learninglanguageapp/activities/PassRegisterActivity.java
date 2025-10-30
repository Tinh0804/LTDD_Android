package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PassRegisterActivity extends AppCompatActivity {
    private LoadingDialog loadingDialog;
    private boolean showPassword = false;
    private boolean showConfirmPassword = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pass_register);

        // Khởi tạo Loading Dialog
        loadingDialog = new LoadingDialog(this);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);

        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        ImageButton btnTogglePassword = findViewById(R.id.btnTogglePassword);
        ImageButton btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword);

        // 🔙 Quay lại PhoneRegisterActivity
        btnBack.setOnClickListener(v -> finish());

        // ⏩ Xử lý đăng ký hoàn tất
        btnContinue.setOnClickListener(v -> {
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            // Validate: Kiểm tra rỗng
            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                etPassword.requestFocus();
                return;
            }

            if (confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                etConfirmPassword.requestFocus();
                return;
            }

            // Validate: Kiểm tra độ dài tối thiểu
            if (password.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                etPassword.requestFocus();
                return;
            }

            // Validate: Kiểm tra 2 mật khẩu có khớp không
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                etConfirmPassword.requestFocus();
                return;
            }

            // Hiển thị Loading Dialog
            loadingDialog.show();

            // Giả lập tạo tài khoản (3 giây)
            // Trong thực tế: thay bằng API call
            new Handler().postDelayed(() -> {
                // Ẩn Loading Dialog
                loadingDialog.dismiss();

                // Chuyển sang màn hình Welcome
                Intent intent = new Intent(PassRegisterActivity.this, Welcome2Activity.class);

                startActivity(intent);

                // Đóng tất cả màn hình đăng ký trước đó
                finishAffinity();

            }, 3000); // 3 giây
        });

        // 👁 Toggle hiển thị / ẩn mật khẩu chính
        btnTogglePassword.setOnClickListener(v -> {
            showPassword = !showPassword;
            if (showPassword) {
                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnTogglePassword.setImageResource(R.drawable.ic_eye_open);
            } else {
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnTogglePassword.setImageResource(R.drawable.ic_eye);
            }
            etPassword.setSelection(etPassword.getText().length());
        });

        // 👁 Toggle hiển thị / ẩn mật khẩu xác nhận
        btnToggleConfirmPassword.setOnClickListener(v -> {
            showConfirmPassword = !showConfirmPassword;
            if (showConfirmPassword) {
                etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_eye_open);
            } else {
                etConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                btnToggleConfirmPassword.setImageResource(R.drawable.ic_eye);
            }
            etConfirmPassword.setSelection(etConfirmPassword.getText().length());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Đảm bảo dismiss dialog khi activity bị destroy
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
}