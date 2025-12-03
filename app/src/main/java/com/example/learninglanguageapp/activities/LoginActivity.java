package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.databinding.ActivityLoginBinding;
import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.viewmodels.AuthViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel authViewModel;
    private GoogleSignInClient googleSignInClient;
    private SharedPrefsHelper prefs;

    private boolean isPasswordVisible = false;


    private final ActivityResultLauncher<Intent> googleSignInLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Task<GoogleSignInAccount> task =
                            GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        GoogleSignInAccount acc = task.getResult(ApiException.class);
                        Log.d("GG", "TOKEN = " + acc.getIdToken());
                    } catch (ApiException e) {
                        Log.e("GG", "Error Code = " + e.getStatusCode());
                    }
                    handleGoogleSignInResult(task);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        prefs = new SharedPrefsHelper(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("976124393303-rlu8pmavq33q5q0781jbglma8ntglmb7.apps.googleusercontent.com")
//                .requestIdToken("74834344847-io42cjb2tqhkkquvis1jnbjvec2rl6t5.apps.googleusercontent.com")
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        setupClickListeners();
        observeViewModel();
    }

    private void setupClickListeners() {

        binding.btnSignIn.setOnClickListener(v -> {
            String email = binding.edtEmail.getText().toString().trim();
            String password = binding.edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email và mật khẩu không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            authViewModel.login(new LoginRequest(email, password));
        });

        binding.tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class))
        );

        // GOOGLE LOGIN
        binding.btnGoogle.setOnClickListener(v -> {
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                googleSignInLauncher.launch(signInIntent);

            });
        });

        binding.btnTogglePassword.setOnClickListener(v -> {
            if (isPasswordVisible) {
                // Ẩn mật khẩu
                binding.edtPassword.setInputType(
                        android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
                );
                binding.btnTogglePassword.setImageResource(R.drawable.ic_eye_open);
            } else {
                // Hiện mật khẩu
                binding.edtPassword.setInputType(
                        android.text.InputType.TYPE_CLASS_TEXT |
                                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );
                binding.btnTogglePassword.setImageResource(R.drawable.ic_eye);
            }

            // Luôn giữ con trỏ ở cuối
            binding.edtPassword.setSelection(binding.edtPassword.length());

            // Đổi trạng thái
            isPasswordVisible = !isPasswordVisible;
        });
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            String idToken = account.getIdToken();
            Log.d("GOOGLE_LOGIN", "ID Token: " + idToken.substring(0, 20) + "...");
            authViewModel.socialLogin(new SocialLoginRequest("Google", idToken));

        } catch (ApiException e) {
            Log.e("GOOGLE_LOGIN", "Google Sign-In Error: " + e.getMessage());
        }
    }

    private void observeViewModel() {
        authViewModel.getLoginResult().observe(this, result -> {
            if (result.isSuccess())
                authViewModel.fetchUserProfile();
        });

        // Khi fetch PROFILE thành công -> chuyển MainActivity
        authViewModel.getUserProfileLiveData().observe(this, result -> {
            if (result.isSuccess()) {
                UserResponse userResponse = result.getValue();
                UserEntity userEntity = UserMapper.toEntity(userResponse);
                new Thread(() -> {
                    AppDatabase.getInstance(this).userDAO().insertUser(userEntity);
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Xin chào " + userResponse.getFullName() + "!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        finish();
                    });
                }).start();
            }
        });
    }
}
