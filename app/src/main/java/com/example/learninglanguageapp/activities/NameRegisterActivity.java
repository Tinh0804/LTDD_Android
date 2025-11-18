package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class NameRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_register);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);
        EditText etName = findViewById(R.id.etName);

        // 🔙 Quay lại màn hình trước (Welcome)
        btnBack.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();

            // Lưu dữ liệu vào SharedPreferences
            getSharedPreferences("USER_DATA", MODE_PRIVATE)
                    .edit()
                    .putString("name", name)
                    .apply();

            // Chuyển sang PhoneRegister
            Intent intent = new Intent(NameRegisterActivity.this, PhoneRegisterActivity.class);
            startActivity(intent);
        });

    }
}
