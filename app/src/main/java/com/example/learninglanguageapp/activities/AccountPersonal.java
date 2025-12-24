package com.example.learninglanguageapp.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.viewmodels.LanguageViewModel;

public class AccountPersonal extends AppCompatActivity {

    private ImageView btnBack;
    private EditText etName, etEmail, etPhoneNumber;
    private TextView tvCountry, tvGender, tvDateOfBirth;

    private SharedPrefsHelper sharedPrefsHelper;
    private UserResponse userResponse;

    private LanguageViewModel languageViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_person);

        initViews();
        setupListeners();

        sharedPrefsHelper = new SharedPrefsHelper(this);
        userResponse = sharedPrefsHelper.getCurrentUserResponse();

        if (userResponse == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            return;
        }

        bindUserDataToViews();

        // ===== Language ViewModel =====
        languageViewModel = new ViewModelProvider(this).get(LanguageViewModel.class);

        observeLanguage();
        languageViewModel.loadLanguage(userResponse.getNativeLanguageId());
    }

    private void initViews() {
        btnBack        = findViewById(R.id.btnBack);
        etName         = findViewById(R.id.etName);
        etEmail        = findViewById(R.id.etEmail);
        etPhoneNumber  = findViewById(R.id.etPhoneNumber);
        tvCountry      = findViewById(R.id.tvCountry);
        tvGender       = findViewById(R.id.tvGender);
        tvDateOfBirth  = findViewById(R.id.tvDateOfBirth);

        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etPhoneNumber.setEnabled(false);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void bindUserDataToViews() {
        etName.setText(userResponse.getFullName());
        etPhoneNumber.setText(userResponse.getPhoneNumber());
        tvDateOfBirth.setText(userResponse.getDateOfBirth());
        etEmail.setText("*******@gmail.com");
    }

    private void observeLanguage() {
        languageViewModel.getLanguageName().observe(this, name ->
                tvCountry.setText(name)
        );

        languageViewModel.getError().observe(this, err ->
                tvCountry.setText("Unknown")
        );
    }
}
