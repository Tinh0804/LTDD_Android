package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;

public class FriendFragment extends Fragment {

    private ImageView btnSettings;
    private ImageView imgAvatar;
    private TextView tvName;
    private TextView tvUserInfo;
    private TextView tvFollowing;
    private TextView tvFollowers;
    private Button btnAddFriends;
    private ImageButton btnShare;
    private Button btnCompleteProfile;

    // Request code
    private static final int PICK_IMAGE = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile_friend, container, false);

        initViews(view);
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        btnSettings = view.findViewById(R.id.btnSettings);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tvName = view.findViewById(R.id.tvName);
        tvUserInfo = view.findViewById(R.id.tvUserInfo);
        tvFollowing = view.findViewById(R.id.tvFollowing);
        tvFollowers = view.findViewById(R.id.tvFollowers);
        btnAddFriends = view.findViewById(R.id.btnAddFriends);
        btnShare = view.findViewById(R.id.btnShare);
        btnCompleteProfile = view.findViewById(R.id.btnCompleteProfile);
    }

    private void setupClickListeners() {

        btnSettings.setOnClickListener(v -> {
            // TODO: Màn hình settings
        });

        imgAvatar.setOnClickListener(v -> openImagePicker());

        btnAddFriends.setOnClickListener(v -> {
            // TODO: Add Friends
        });

        btnShare.setOnClickListener(v -> shareProfile());

        btnCompleteProfile.setOnClickListener(v -> {
            // TODO: Complete Profile
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void shareProfile() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out my profile: @Sam315105");
        startActivity(Intent.createChooser(shareIntent, "Share Profile"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imgAvatar.setImageURI(selectedImage);

            // TODO: Upload image to server
        }
    }
}
