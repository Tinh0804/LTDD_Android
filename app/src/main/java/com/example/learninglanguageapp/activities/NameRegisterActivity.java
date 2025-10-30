package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class NameRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_register);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);

        // 🔙 Quay lại màn hình trước (Welcome)
        btnBack.setOnClickListener(v -> finish());

        // ⏩ Sang màn hình nhập số điện thoại
        btnContinue.setOnClickListener(v -> {
            Intent intent = new Intent(NameRegisterActivity.this, PhoneRegisterActivity.class);
            startActivity(intent);
        });
    }
}
