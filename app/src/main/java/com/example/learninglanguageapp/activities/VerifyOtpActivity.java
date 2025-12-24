package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Request.ForgotPasswordRequest;
import com.example.learninglanguageapp.models.Request.ValidateOtpRequest;
import com.example.learninglanguageapp.models.Response.MessageResponse;
import com.example.learninglanguageapp.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText[] otp = new EditText[6];
    private TextView tvTimer, tvResend;
    private CountDownTimer timer;

    private String email;
    private boolean isVerifying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        email = getIntent().getStringExtra("email");
        if (email == null || email.trim().isEmpty()) {
            Toast.makeText(this, "Missing email", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        otp[0] = findViewById(R.id.otp1);
        otp[1] = findViewById(R.id.otp2);
        otp[2] = findViewById(R.id.otp3);
        otp[3] = findViewById(R.id.otp4);
        otp[4] = findViewById(R.id.otp5);
        otp[5] = findViewById(R.id.otp6);

        tvTimer = findViewById(R.id.tvTimer);
        tvResend = findViewById(R.id.tvResend);

        setupOtpInput();
        startTimer();

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        tvResend.setOnClickListener(v -> {
            if (tvResend.isEnabled()) resendOtp();
        });
    }

    private void setupOtpInput() {
        for (int i = 0; i < 6; i++) {
            final int pos = i;

            otp[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1) {
                        if (pos < 5) otp[pos + 1].requestFocus();
                        else verifyOtp(); // đủ 6 số
                    } else if (s.length() == 0 && pos > 0) {
                        otp[pos - 1].requestFocus();
                    }
                }
            });

            otp[i].setOnKeyListener((v, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (otp[pos].getText().toString().isEmpty() && pos > 0) {
                        otp[pos - 1].setText("");
                        otp[pos - 1].requestFocus();
                    }
                }
                return false;
            });
        }
        otp[0].requestFocus();
    }

    private void startTimer() {
        tvResend.setEnabled(false);
        tvResend.setAlpha(0.5f);
        if (timer != null) timer.cancel();

        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("You can resend the code in " + (millisUntilFinished / 1000) + " seconds");
            }

            @Override
            public void onFinish() {
                tvTimer.setText("Bạn có thể gửi lại mã ngay bây giờ");
                tvResend.setEnabled(true);
                tvResend.setAlpha(1f);
            }
        }.start();
    }

    private String getOtpCode() {
        StringBuilder code = new StringBuilder();
        for (EditText et : otp) code.append(et.getText().toString().trim());
        return code.toString();
    }

    private void clearOtp() {
        for (EditText et : otp) et.setText("");
        otp[0].requestFocus();
    }

    private void setOtpEnabled(boolean enabled) {
        for (EditText et : otp) et.setEnabled(enabled);
    }

    private void verifyOtp() {
        if (isVerifying) return;

        String resetCode = getOtpCode();
        if (resetCode.length() != 6) return;

        isVerifying = true;
        setOtpEnabled(false);

        // swagger bắt newPassword ở validate-otp => gửi tạm.
        String dummyPassword = "Temp@123";

        ApiClient.getApiService(this)
                .validateOtp(new ValidateOtpRequest(email, resetCode, dummyPassword))
                .enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        isVerifying = false;
                        setOtpEnabled(true);

                        if (response.isSuccessful() && response.body() != null && response.body()) {
                            Toast.makeText(VerifyOtpActivity.this, "OTP hợp lệ!", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(VerifyOtpActivity.this, NewPasswordActivity.class);
                            i.putExtra("email", email);
                            i.putExtra("resetCode", resetCode);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(VerifyOtpActivity.this, "OTP sai hoặc hết hạn", Toast.LENGTH_SHORT).show();
                            clearOtp();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        isVerifying = false;
                        setOtpEnabled(true);
                        Toast.makeText(VerifyOtpActivity.this, "Network: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void resendOtp() {
        tvResend.setEnabled(false);
        tvResend.setAlpha(0.5f);

        ApiClient.getApiService(this)
                .forgotPassword(new ForgotPasswordRequest(email))
                .enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        Toast.makeText(VerifyOtpActivity.this,
                                (response.isSuccessful() && response.body() != null)
                                        ? response.body().getMessage()
                                        : "Resend failed (" + response.code() + ")",
                                Toast.LENGTH_SHORT).show();
                        startTimer();
                        clearOtp();
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(VerifyOtpActivity.this,
                                "Network: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                        startTimer();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }
}
