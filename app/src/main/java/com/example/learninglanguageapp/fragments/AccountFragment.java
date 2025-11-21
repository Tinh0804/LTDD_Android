package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.AccountPersonal;
import com.example.learninglanguageapp.activities.LeaderboardActivity;
import com.example.learninglanguageapp.activities.LoginActivity;
import com.example.learninglanguageapp.activities.MatchGameActivity;

public class AccountFragment extends Fragment {

    // Views
    private ImageView ivLogo, ivMenu, ivAvatar;
    private TextView tvUserName, tvUserEmail;
    private CardView cvUpgrade, cvUserProfile;
    private LinearLayout layoutPreferences, layoutPersonalInfo, layoutNotification;
    private LinearLayout layoutGeneral, layoutAccessibility, layoutSecurity;
    private LinearLayout layoutFindFriends, layoutDarkMode, layoutLogout;
    private SwitchCompat switchDarkMode;

    // Fake data
    private String userName = "Lê Hoàng Q.Tỉnh";
    private String userEmail = "lhqtinh2005@gmail.com";
    private boolean isDarkMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_account, container, false);

        initViews(view);
        loadUserData();
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        // Header
        ivLogo = view.findViewById(R.id.ivLogo);
        ivMenu = view.findViewById(R.id.ivMenu);

        // User Profile
        ivAvatar = view.findViewById(R.id.ivAvatar);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        // Cards
        cvUpgrade = view.findViewById(R.id.cvUpgrade);
        cvUserProfile = view.findViewById(R.id.cvUserProfile);

        // Menu Items
        layoutPreferences = view.findViewById(R.id.layoutPreferences);
        layoutPersonalInfo = view.findViewById(R.id.layoutPersonalInfo);
        layoutNotification = view.findViewById(R.id.layoutNotification);
        layoutGeneral = view.findViewById(R.id.layoutGeneral);
        layoutAccessibility = view.findViewById(R.id.layoutAccessibility);
        layoutSecurity = view.findViewById(R.id.layoutSecurity);
        layoutFindFriends = view.findViewById(R.id.layoutFindFriends);
        layoutDarkMode = view.findViewById(R.id.layoutDarkMode);
        layoutLogout = view.findViewById(R.id.layoutLogout);

        // Switch
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
    }

    private void loadUserData() {
        tvUserName.setText(userName);
        tvUserEmail.setText(userEmail);
        switchDarkMode.setChecked(isDarkMode);
    }

    private void setupClickListeners() {

        ivMenu.setOnClickListener(v -> showToast("Menu clicked"));

        cvUpgrade.setOnClickListener(v ->
                showToast("Upgrade Plan clicked"));

        cvUserProfile.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), AccountPersonal.class);
            startActivity(i);
        });

        layoutPreferences.setOnClickListener(v ->
                showToast("Preferences clicked"));

        layoutPersonalInfo.setOnClickListener(v -> {
            showToast("Personal Info clicked");
            startActivity(AccountPersonal.class);
        });

        layoutNotification.setOnClickListener(v -> {
            showToast("Notification clicked");
            startActivity(LeaderboardActivity.class);
        });

        layoutGeneral.setOnClickListener(v -> {
            showToast("General clicked");
            startActivity(MatchGameActivity.class);
        });

        layoutAccessibility.setOnClickListener(v ->
                showToast("Accessibility clicked"));

        layoutSecurity.setOnClickListener(v ->
                showToast("Security clicked"));

        layoutFindFriends.setOnClickListener(v ->
                showToast("Find Friends clicked"));

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkMode = isChecked;
            showToast(isChecked ? "Dark Mode ON" : "Dark Mode OFF");
        });

        layoutLogout.setOnClickListener(v -> {
            Intent i = new Intent(requireContext(), LoginActivity.class);
            startActivity(i);
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(requireContext(), cls);
        startActivity(intent);
    }

    // Update name & email
    public void updateUserInfo(String name, String email) {
        this.userName = name;
        this.userEmail = email;
        tvUserName.setText(name);
        tvUserEmail.setText(email);
    }

    public boolean getDarkModeStatus() {
        return isDarkMode;
    }

    public void setDarkMode(boolean enabled) {
        this.isDarkMode = enabled;
        switchDarkMode.setChecked(enabled);
    }
}
