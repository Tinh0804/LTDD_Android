package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;

public class LanguageUse extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_use);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);

        // 🔙 Quay lại NameRegisterActivity
        btnBack.setOnClickListener(v -> finish());

        // ⏩ Sang màn hình nhập mật khẩu
//        btnContinue.setOnClickListener(v -> {
//            Intent intent = new Intent(LanguageUse.this, LanguageLearn.class);
//            startActivity(intent);
//        });
    }
}
