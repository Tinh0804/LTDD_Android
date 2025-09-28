package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.firebase.FirebaseAuthManager;
import com.example.learninglanguageapp.utils.Constants;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.utils.ValidationUtils;
import com.google.firebase.auth.FirebaseUser;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.databinding.ActivityRegisterBinding;


public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuthManager authManager;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authManager = FirebaseAuthManager.getInstance(this);
        prefsHelper = new SharedPrefsHelper(this);

        binding.btnRegister.setOnClickListener(v -> handleRegister());
        binding.tvLogin.setOnClickListener(v -> openLoginActivity());
    }

    private void handleRegister() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirmPassword = binding.etConfirmPassword.getText().toString().trim();

        if (!validateInput(email, password, confirmPassword)) {
            return;
        }

        showProgress(true);

        authManager.createUserWithEmailAndPassword(email, password, new FirebaseAuthManager.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                showProgress(false);

                prefsHelper.saveString(Constants.PREF_USER_ID, user.getUid());
                prefsHelper.saveString(Constants.PREF_USER_EMAIL, user.getEmail());

                Toast.makeText(RegisterActivity.this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onError(Exception exception) {
                showProgress(false);
                Toast.makeText(RegisterActivity.this, getString(R.string.register_failed), Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateInput(String email, String password, String confirmPassword) {
        boolean isValid = true;

        if (email.isEmpty() || !ValidationUtils.isValidEmail(email)) {
            binding.tilEmail.setError(getString(R.string.error_invalid_email));
            isValid = false;
        } else {
            binding.tilEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            binding.tilPassword.setError(getString(R.string.error_password_too_short));
            isValid = false;
        } else {
            binding.tilPassword.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            binding.tilConfirmPassword.setError(getString(R.string.error_password_not_match));
            isValid = false;
        } else {
            binding.tilConfirmPassword.setError(null);
        }

        return isValid;
    }

    private void showProgress(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!show);
        binding.btnRegister.setText(show ? "Đang đăng ký..." : getString(R.string.register));
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void openLoginActivity() {
        finish();
    }
}
