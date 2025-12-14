package com.example.learninglanguageapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;

public class Profile_Friend extends AppCompatActivity {

    private ImageView btnSettings;
    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvUserInfo;
    private TextView tvFollowing;
    private TextView tvFollowers;
    private Button btnAddFriends;
    private ImageButton btnShare;
    private Button btnCompleteProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_fr);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnSettings = findViewById(R.id.btnSettings);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvName = findViewById(R.id.tvName);
        tvUserInfo = findViewById(R.id.tvUserInfo);
        tvFollowing = findViewById(R.id.tvFollowing);
        tvFollowers = findViewById(R.id.tvFollowers);
        btnAddFriends = findViewById(R.id.btnAddFriends);
        btnShare = findViewById(R.id.btnShare);
        btnCompleteProfile = findViewById(R.id.btnCompleteProfile);
    }


    private void setupClickListeners() {
        // Settings
        btnSettings.setOnClickListener(v -> {
            // TODO: Mở màn hình settings
        });

        // Avatar - Upload/Change avatar
        imgAvatar.setOnClickListener(v -> {
            // TODO: Mở gallery để chọn ảnh
            openImagePicker();
        });

        // Add Friends
        btnAddFriends.setOnClickListener(v -> {
            // TODO: Mở màn hình add friends
        });

        // Share Profile
        btnShare.setOnClickListener(v -> {
            shareProfile();
        });

        // Complete Profile
        btnCompleteProfile.setOnClickListener(v -> {
            // TODO: Mở màn hình complete profile
        });

        // Bottom Navigation
//        findViewById(R.id.navHome).setOnClickListener(v -> {
//            // TODO: Navigate to Home
//        });
//
//        findViewById(R.id.navPractice).setOnClickListener(v -> {
//            // TODO: Navigate to Practice
//        });
//
//        findViewById(R.id.navLearn).setOnClickListener(v -> {
//            // TODO: Navigate to Learn
//        });
//
//        findViewById(R.id.navShop).setOnClickListener(v -> {
//            // TODO: Navigate to Shop
//        });
//
//        findViewById(R.id.navLeaderboard).setOnClickListener(v -> {
//            // TODO: Navigate to Leaderboard
//        });
//
//        findViewById(R.id.navMore).setOnClickListener(v -> {
//            // TODO: Navigate to More
//        });
    }

    private void openImagePicker() {
        // Mở gallery để chọn ảnh avatar
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 100);
    }

    private void shareProfile() {
        // Chia sẻ profile
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my profile: @Sam315105");
        startActivity(Intent.createChooser(shareIntent, "Share Profile"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Load ảnh đã chọn vào ImageView
            imgAvatar.setImageURI(data.getData());

            // TODO: Upload ảnh lên server
        }
    }
}