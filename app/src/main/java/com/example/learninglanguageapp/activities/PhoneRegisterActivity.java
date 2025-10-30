package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class PhoneRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_register);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);

        // 🔙 Quay lại NameRegisterActivity
        btnBack.setOnClickListener(v -> finish());

        // ⏩ Sang màn hình nhập mật khẩu
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(PhoneRegisterActivity.this, PassRegisterActivity.class);
            startActivity(intent);
        });
    }
}
