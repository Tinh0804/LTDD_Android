package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.databinding.ActivityRegisterBinding;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.viewmodels.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private AuthViewModel authViewModel;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupClickListeners();
        observeViewModel();
    }

    private void setupClickListeners() {
        binding.btnRegister.setOnClickListener(v -> {
            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle == null) {
                Toast.makeText(this, "Missing registration data", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            int nativeLanguageId = bundle.containsKey("language_use_id") ?
                    bundle.getInt("language_use_id") : 2;

            int learnLanguageId = bundle.containsKey("language_learn_id") ?
                    bundle.getInt("language_learn_id") : 1;

            int courseId = bundle.containsKey("course_id") ?
                    bundle.getInt("course_id") : 1;


            String email = binding.etUserName.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();
            String fullName = binding.etName.getText().toString().trim();
            String phoneNumber = binding.etPhone.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                Toast.makeText(this, "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.register(new RegisterRequest(email, password, fullName,phoneNumber,courseId, nativeLanguageId, learnLanguageId ));
        });

        binding.btnTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                binding.etPassword.setInputType(
                        android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
                binding.btnTogglePassword.setImageResource(R.drawable.ic_eye_open);
            } else {
                binding.etPassword.setInputType(
                        android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
                binding.btnTogglePassword.setImageResource(R.drawable.ic_eye);
            }
            binding.etPassword.setSelection(binding.etPassword.length());
            isPasswordVisible = !isPasswordVisible;
        });
        binding.tvSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        authViewModel.getRegisterResult().observe(this, result -> {
            if (result.isSuccess())
                authViewModel.fetchUserProfile();
            else
                Toast.makeText(this, result.getException().getMessage(), Toast.LENGTH_LONG).show();
        });

        authViewModel.getRegisterResult().observe(this, result -> {
            if (result.isSuccess()) {
                Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, MainActivity.class); // trở về login
                startActivity(intent);
//                finish();
            } else {
                Toast.makeText(this, result.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }
}
