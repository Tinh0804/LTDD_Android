package com.example.learninglanguageapp.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.databinding.ActivityLoginBinding;
import com.example.learninglanguageapp.firebase.AnalyticsManager;
import com.example.learninglanguageapp.firebase.FirebaseAuthManager;
import com.example.learninglanguageapp.utils.Constants;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.utils.ValidationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseAuthManager authManager;
    private SharedPrefsHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeComponents();
        setupClickListeners();
        checkExistingAuth();
    }

    private void initializeComponents() {
        authManager = FirebaseAuthManager.getInstance(this);
        prefsHelper = new SharedPrefsHelper(this);
    }

    private void setupClickListeners() {
        binding.btnSignIn.setOnClickListener(v -> handleLogin());
        binding.tvSignUp.setOnClickListener(v -> openRegisterActivity());
        binding.tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
    }

    private void checkExistingAuth() {
        if (authManager.isUserLoggedIn()) {
            navigateToMain();
        }
    }

    private void handleLogin() {
        String email = binding.edtEmail.getText().toString().trim();
        String password = binding.edtPassword.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        showProgress(true);

        authManager.signInWithEmailAndPassword(email, password, new FirebaseAuthManager.AuthResultListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                showProgress(false);

                // Save user data locally
                prefsHelper.saveString(Constants.PREF_USER_ID, user.getUid());
                prefsHelper.saveString(Constants.PREF_USER_EMAIL, user.getEmail());
                prefsHelper.saveLong(Constants.PREF_LAST_SYNC, System.currentTimeMillis());

                // Log analytics event
                AnalyticsManager.getInstance(LoginActivity.this).logEvent("user_login", null);

                Toast.makeText(LoginActivity.this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                navigateToMain();
            }

            @Override
            public void onError(Exception exception) {
                showProgress(false);
                String errorMessage = getErrorMessage(exception);
                Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });
    }


    private boolean validateInput(String email, String password) {
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

        return isValid;
    }

    private void showProgress(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnSignIn.setEnabled(!show);
        binding.btnSignIn.setText(show ? "Đang đăng nhập..." : getString(R.string.login));
    }

    private String getErrorMessage(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            switch (errorCode) {
                case "ERROR_INVALID_EMAIL":
                    return "Email không hợp lệ";
                case "ERROR_WRONG_PASSWORD":
                    return "Mật khẩu không chính xác";
                case "ERROR_USER_NOT_FOUND":
                    return "Tài khoản không tồn tại";
                case "ERROR_USER_DISABLED":
                    return "Tài khoản đã bị vô hiệu hóa";
                case "ERROR_TOO_MANY_REQUESTS":
                    return "Quá nhiều lần thử. Vui lòng thử lại sau";
                default:
                    return "Đăng nhập thất bại. Vui lòng thử lại";
            }
        }
        return getString(R.string.login_failed);
    }

    private void handleForgotPassword() {
        String email = binding.edtEmail.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Không thể gửi email đặt lại mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}