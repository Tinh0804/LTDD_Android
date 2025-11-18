package com.example.test;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AccountPersonal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountpersonal);

        EditText etName = findViewById(R.id.etName);
        EditText etPhone = findViewById(R.id.etPhoneNumber);
        TextView tvCountry = findViewById(R.id.tvCountry);

        // SỬA Ở ĐÂY
        ImageView btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        SharedPreferences pref = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String name = pref.getString("name", "");
        String phone = pref.getString("phone", "");
        String country = pref.getString("country", "");

        etName.setText(name);
        etPhone.setText(phone);
        tvCountry.setText(country);
    }
}


