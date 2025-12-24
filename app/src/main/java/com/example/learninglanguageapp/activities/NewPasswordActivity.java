package com.example.learninglanguageapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.databinding.ActivityNewPasswordBinding;
import com.example.learninglanguageapp.models.Request.ResetPasswordRequest;
import com.example.learninglanguageapp.models.Response.MessageResponse;
import com.example.learninglanguageapp.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPasswordActivity extends AppCompatActivity {

    private ActivityNewPasswordBinding binding;

    private String email;
    private String resetCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        email = getIntent().getStringExtra("email");
        resetCode = getIntent().getStringExtra("resetCode");

        if (email == null || resetCode == null) {
            Toast.makeText(this, "Missing data", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        binding.btnBack.setOnClickListener(v -> finish());

        binding.btnSavePassword.setOnClickListener(v -> {
            String newPass = binding.edtNewPassword.getText().toString().trim();
            String confirmPass = binding.edtConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(newPass)) {
                binding.inputNewPassword.setError("Please enter new password");
                return;
            }
            if (newPass.length() < 6) {
                binding.inputNewPassword.setError("Password must be at least 6 characters");
                return;
            }
            if (!newPass.equals(confirmPass)) {
                binding.inputConfirmPassword.setError("Passwords do not match");
                return;
            }

            binding.inputNewPassword.setError(null);
            binding.inputConfirmPassword.setError(null);

            setLoading(true);

            ApiClient.getApiService(this)
                    .resetPassword(new ResetPasswordRequest(email, resetCode, newPass))
                    .enqueue(new Callback<MessageResponse>() {
                        @Override
                        public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                            setLoading(false);

                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(NewPasswordActivity.this,
                                        response.body().getMessage(),
                                        Toast.LENGTH_LONG).show();
                                finishAffinity(); // về login/main
                            } else {
                                Toast.makeText(NewPasswordActivity.this,
                                        "Reset failed (" + response.code() + ")",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageResponse> call, Throwable t) {
                            setLoading(false);
                            Toast.makeText(NewPasswordActivity.this,
                                    "Network: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    private void setLoading(boolean loading) {
        binding.btnSavePassword.setEnabled(!loading);
        binding.btnSavePassword.setText(loading ? "Saving..." : "Save Password");
    }
}
