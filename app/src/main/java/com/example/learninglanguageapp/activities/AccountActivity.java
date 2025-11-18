package com.example.learninglanguageapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import com.example.learninglanguageapp.R;

public class AccountActivity extends AppCompatActivity {

    // Views
    private ImageView ivLogo, ivMenu, ivAvatar;
    private TextView tvUserName, tvUserEmail;
    private CardView cvUpgrade, cvUserProfile;
    private LinearLayout layoutPreferences, layoutPersonalInfo, layoutNotification;
    private LinearLayout layoutGeneral, layoutAccessibility, layoutSecurity;
    private LinearLayout layoutFindFriends, layoutDarkMode, layoutLogout;
    private SwitchCompat switchDarkMode;

    // Dữ liệu giả lập - sẽ thay thế bằng database sau
    private String userName = "Andrew";
    private String userEmail = "Andrew@gmail.com";
    private boolean isDarkMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        initViews();
        loadUserData();
        setupClickListeners();
    }

    private void initViews() {
        // Header
        ivLogo = findViewById(R.id.ivLogo);
        ivMenu = findViewById(R.id.ivMenu);

        // User Profile
        ivAvatar = findViewById(R.id.ivAvatar);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserEmail = findViewById(R.id.tvUserEmail);

        // Cards
        cvUpgrade = findViewById(R.id.cvUpgrade);
        cvUserProfile = findViewById(R.id.cvUserProfile);

        // Menu Items
        layoutPreferences = findViewById(R.id.layoutPreferences);
        layoutPersonalInfo = findViewById(R.id.layoutPersonalInfo);
        layoutNotification = findViewById(R.id.layoutNotification);
        layoutGeneral = findViewById(R.id.layoutGeneral);
        layoutAccessibility = findViewById(R.id.layoutAccessibility);
        layoutSecurity = findViewById(R.id.layoutSecurity);
        layoutFindFriends = findViewById(R.id.layoutFindFriends);
        layoutDarkMode = findViewById(R.id.layoutDarkMode);
        layoutLogout = findViewById(R.id.layoutLogout);

        // Switch
        switchDarkMode = findViewById(R.id.switchDarkMode);
    }

    private void loadUserData() {
        // Load dữ liệu user - hiện tại dùng dữ liệu giả
        // TODO: Thay thế bằng việc load từ database
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
        switchDarkMode.setChecked(isDarkMode);
    }

    private void setupClickListeners() {
        // Menu button
        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Menu clicked");
                // TODO: Hiển thị menu options
            }
        });

        // Upgrade Plan Card
        cvUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Upgrade Plan clicked");
                // TODO: Navigate to upgrade screen
            }
        });

        // User Profile Card
        cvUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("User Profile clicked");
                // TODO: Navigate to profile edit screen
            }
        });

        // Preferences
        layoutPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Preferences clicked");
            }
        });

        // Personal Info
        layoutPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Personal Info clicked");
                Intent intentInfor = new Intent(AccountActivity.this, AccountPersonal.class);
                startActivity(intentInfor);
            }
        });

        // Notification
        layoutNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Notification clicked");
                Intent intentleader = new Intent(AccountActivity.this, LeaderboardActivity.class);
                startActivity(intentleader);
            }
        });

        // General
        layoutGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("General clicked");
                Intent intentGameMatch = new Intent(AccountActivity.this, MatchGameActivity.class);
                startActivity(intentGameMatch);
            }
        });

        // Accessibility
        layoutAccessibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Accessibility clicked");
                // TODO: Navigate to accessibility screen
            }
        });

        // Security
        layoutSecurity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Security clicked");
                // TODO: Navigate to security screen
            }
        });

        // Find Friends
        layoutFindFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Find Friends clicked");
                // TODO: Navigate to find friends screen
            }
        });

        // Dark Mode Switch
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkMode = isChecked;
            showToast(isChecked ? "Dark Mode ON" : "Dark Mode OFF");
            // TODO: Apply dark mode theme
            // TODO: Save dark mode preference to database
        });

        // Logout
        layoutLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Logout clicked");
                // TODO: Show logout confirmation dialog
                // TODO: Handle logout logic
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method để update user info từ database
    public void updateUserInfo(String name, String email) {
        this.userName = name;
        this.userEmail = email;
        tvUserName.setText(name);
        tvUserEmail.setText(email);
    }

    // Method để lấy dark mode status
    public boolean getDarkModeStatus() {
        return isDarkMode;
    }

    // Method để set dark mode
    public void setDarkMode(boolean enabled) {
        this.isDarkMode = enabled;
        switchDarkMode.setChecked(enabled);
    }
}