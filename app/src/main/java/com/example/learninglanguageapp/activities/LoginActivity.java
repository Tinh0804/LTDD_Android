package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.databinding.ActivityLoginBinding;
import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.viewmodels.AuthViewModel;
import com.facebook.appevents.suggestedevents.ViewOnClickListener;


public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;
    private Button btnSignIn;
    private TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        btnSignIn = findViewById(R.id.btnSignIn);
        tvSignUp = findViewById(R.id.tvSignUp);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        setupClick();
        observeViewModel();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                startActivity(intent);
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupClick() {
        binding.btnSignIn.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();
            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this, "Email và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            LoginRequest request = new LoginRequest(email, password);
            authViewModel.login(request);
        });
    }

    private void observeViewModel() {
        authViewModel.getLoginResult().observe(this, result -> {
            if (result.isSuccess()) {
                UserResponse user = result.getValue();
                Toast.makeText(this, "Login thành công: " + user.getFullName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, result.getException().getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
