package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.fragments.AccountFragment;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

public class AccountPersonal extends AppCompatActivity {

    // View
    private ImageView btnBack;
    private EditText etName, etEmail, etPhoneNumber;
    private TextView tvCountry, tvGender, tvDateOfBirth;

    // Prefs + user
    private SharedPrefsHelper sharedPrefsHelper;
    private UserResponse userResponse;   // data user hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_person); // layout của bạn

        initViews();
        setupListeners();

        // Khởi tạo helper
        sharedPrefsHelper = new SharedPrefsHelper(this);

        // Lấy user từ SharedPreferences
        userResponse = sharedPrefsHelper.getCurrentUserResponse();

        if (userResponse != null) {
            bindUserDataToViews();
        } else {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        btnBack        = findViewById(R.id.btnBack);
        etName         = findViewById(R.id.etName);
        etEmail        = findViewById(R.id.etEmail);
        etPhoneNumber  = findViewById(R.id.etPhoneNumber);
        tvCountry      = findViewById(R.id.tvCountry);
        tvGender       = findViewById(R.id.tvGender);
        tvDateOfBirth  = findViewById(R.id.tvDateOfBirth);

        // KHÔNG CHO CHỈNH SỬA
        etName.setEnabled(false);
        etEmail.setEnabled(false);
        etPhoneNumber.setEnabled(false);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
    }

    /**
     * Đổ dữ liệu từ userResponse lên layout
     */
    private void bindUserDataToViews() {
        // Name
        if (userResponse.getFullName() != null) {
            etName.setText(userResponse.getFullName());
        }

        // Phone
        if (userResponse.getPhoneNumber() != null) {
            etPhoneNumber.setText(userResponse.getPhoneNumber());
        }

        // DOB
        if (userResponse.getDateOfBirth() != null) {
            tvDateOfBirth.setText(userResponse.getDateOfBirth());
        }

        // Country: map theo nativeLanguageId (demo)
        tvCountry.setText(mapLanguageIdToCountry(userResponse.getNativeLanguageId()));

        etEmail.setText("*******@gmail.com");

    }

    /**
     * Convert nativeLanguageId -> country hiển thị
     */
    private String mapLanguageIdToCountry(int languageId) {
        switch (languageId) {
            case 1:
                return "Vietnam";
            case 2:
                return "United States of America";
            case 3:
                return "Japan";
            case 4:
                return "Korea";
            default:
                return "Unknown";
        }
    }
}
