package com.example.learninglanguageapp.fragments;

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
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.FriendModel;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {

    private LinearLayout friendsContainer;
    private TextView tvTotalFriends;
    private TextView tvOnlineFriends;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_list_friend, container, false);

        friendsContainer = view.findViewById(R.id.friendsContainer);
        tvTotalFriends = view.findViewById(R.id.tvTotalFriends);
        tvOnlineFriends = view.findViewById(R.id.tvOnlineFriends);

        tvTotalFriends.setText("0");
        tvOnlineFriends.setText("0");

        loadFriends();

        return view;
    }

    private void loadFriends() {
        SharedPrefsHelper prefs = new SharedPrefsHelper(requireContext());
        String token = prefs.getToken();

        Log.d("FRIEND", "TOKEN = " + token);

        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(),
                    "Chưa có token, vui lòng đăng nhập lại",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getApiService(requireContext())
                .getFriends("Bearer " + token)
                .enqueue(new Callback<ApiResponse<List<FriendModel>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<FriendModel>>> call,
                                           Response<ApiResponse<List<FriendModel>>> response) {

                        Log.d("FRIEND", "HTTP CODE = " + response.code());

                        if (!isAdded()) return;

                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<List<FriendModel>> body = response.body();

                            if (body.isStatus() && body.getData() != null) {
                                renderFriends(body.getData());
                            } else {
                                Toast.makeText(requireContext(),
                                        body.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    "API error: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<FriendModel>>> call, Throwable t) {
                        Toast.makeText(requireContext(),
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void renderFriends(List<FriendModel> friends) {
        friendsContainer.removeAllViews();

        tvTotalFriends.setText(String.valueOf(friends.size()));
        tvOnlineFriends.setText("0");

        for (FriendModel friend : friends) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_friend, friendsContainer, false);

            TextView tvName = view.findViewById(R.id.tvName);
            TextView tvExp = view.findViewById(R.id.tvExperiencePoints);
            TextView tvStatus = view.findViewById(R.id.tvOnlineStatus);
            ImageView ivAvatar = view.findViewById(R.id.ivAvatar);

            tvName.setText(friend.getDisplayName() != null
                    ? friend.getDisplayName()
                    : "Unknown");

            tvExp.setText("⚡️ 0 XP");
            tvStatus.setText("Friend");

            if (friend.getAvatarUrl() != null && !friend.getAvatarUrl().isEmpty()) {
                Glide.with(this)
                        .load(friend.getAvatarUrl())
                        .placeholder(R.drawable.avt_fr1)
                        .error(R.drawable.avt_fr1)
                        .into(ivAvatar);
            } else {
                ivAvatar.setImageResource(R.drawable.avt_fr1);
            }

            friendsContainer.addView(view);
        }
    }
}
