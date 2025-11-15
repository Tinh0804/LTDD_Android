package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;
import com.google.android.material.button.MaterialButton;

import java.time.Instant;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etPhone, etPassword, etConfirmPassword;
    private ImageButton btnTogglePassword, btnToggleConfirmPassword;
    private MaterialButton btnRegister;

    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnTogglePassword = findViewById(R.id.btnTogglePassword);
        btnToggleConfirmPassword = findViewById(R.id.btnToggleConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
    }

    private void setListeners() {
        btnTogglePassword.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            togglePasswordVisibility(etPassword, passwordVisible);
        });

        btnToggleConfirmPassword.setOnClickListener(v -> {
            confirmPasswordVisible = !confirmPasswordVisible;
            togglePasswordVisibility(etConfirmPassword, confirmPasswordVisible);
        });

        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (validateInput(name, phone, password, confirmPassword)) {


                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putString("phone", phone);
                bundle.putString("password", password);



                Intent intent1 = new Intent(RegisterActivity.this, StudyTimeActivity.class);
                intent1.putExtra("data",bundle);

                startActivity(intent1);
            }
        });
    }

    private void togglePasswordVisibility(EditText editText, boolean visible) {
        if (visible) {
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        // Giữ con trỏ ở cuối text
        editText.setSelection(editText.getText().length());
    }

    private boolean validateInput(String name, String phone, String password, String confirmPassword) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            etName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            etPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            etConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void registerUser(String name, String phone, String password) {
        // TODO: Gọi API backend ở đây
        // Ví dụ giả lập:
        Toast.makeText(this, "Register successful!\nName: " + name + "\nPhone: " + phone, Toast.LENGTH_LONG).show();

        // Sau khi register xong -> chuyển sang LoginActivity hoặc MainActivity
        // startActivity(new Intent(this, MainActivity.class));
        // finish();
    }
}
