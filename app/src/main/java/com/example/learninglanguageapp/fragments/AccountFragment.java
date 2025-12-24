package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.AccountPersonal;
import com.example.learninglanguageapp.activities.LoginActivity;
import com.example.learninglanguageapp.activities.MatchGameActivity;
import com.example.learninglanguageapp.models.Entities.UserEntity; // Thêm import
import com.example.learninglanguageapp.storage.AppDatabase; // Thêm import
import com.example.learninglanguageapp.storage.DAOs.UserDAO; // Thêm import
import com.example.learninglanguageapp.viewmodels.AuthViewModel;

public class AccountFragment extends Fragment {

    // Views
    private ImageView ivLogo, ivMenu, ivAvatar;
    private TextView tvUserName, tvUserEmail;
    private CardView cvUpgrade, cvUserProfile;
    private LinearLayout layoutPreferences, layoutPersonalInfo, layoutNotification;
    private LinearLayout layoutGeneral, layoutAccessibility, layoutSecurity;
    private LinearLayout layoutFindFriends, layoutDarkMode, layoutLogout;
    private SwitchCompat switchDarkMode;
    private AuthViewModel authViewModel;
    private UserEntity currentUser;
    private boolean isDarkMode = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_account, container, false);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        initViews(view);
        loadUserDataFromDb(); // Tải dữ liệu từ DB
        setupClickListeners();
        observeViewModel();
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
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
    }

    private void loadUserDataFromDb() {
        new Thread(() -> {
            UserDAO userDao = AppDatabase.getInstance(requireContext().getApplicationContext()).userDAO();
            final UserEntity user = userDao.getCurrentUser();
//            if (getActivity() == null) return;
            getActivity().runOnUiThread(() -> {
                if (user != null) {
                    this.currentUser = user;
                    tvUserName.setText(user.getFullName());
                    tvUserEmail.setText(user.getDateOfBirth());
                } else {
                    tvUserName.setText("Khách");
                    tvUserEmail.setText("Chưa đăng nhập");
                }
                switchDarkMode.setChecked(isDarkMode);
            });
        }).start();
    }

    private void setupClickListeners() {
        layoutPersonalInfo.setOnClickListener(v -> {
            showToast("Personal Info clicked");
            startActivity(AccountPersonal.class);
        });

        layoutNotification.setOnClickListener(v -> {
            showToast("Notification clicked");
            startActivity(LeaderBoardFragment.class);
        });

        layoutGeneral.setOnClickListener(v -> {
            showToast("General clicked");
            startActivity(MatchGameActivity.class);
        });

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkMode = isChecked;
            showToast(isChecked ? "Dark Mode ON" : "Dark Mode OFF");
        });

        layoutLogout.setOnClickListener(v -> {
            Log.d("LOGOUT_CLICK", "Logout row clicked");
            authViewModel.logout();
        });
    }

    private void observeViewModel() {
        authViewModel.getLogoutResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                showToast("Đăng xuất thành công!");
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // Yêu cầu Activity chứa Fragment này tự đóng
                if (getActivity() != null)
                    getActivity().finish();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void startActivity(Class<?> cls) {
        Intent intent = new Intent(requireContext(), cls);
        startActivity(intent);
    }
}
