package com.example.learninglanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;

public class FriendFragment extends Fragment {

    private LinearLayout friendsContainer;  // Container chứa danh sách bạn bè
    private TextView tvTotalFriends; // Số lượng bạn bè
    private TextView tvOnlineFriends; // Số lượng bạn bè online

    private int totalFriends = 25; // Số lượng bạn bè (có thể lấy từ API hoặc database)
    private int onlineFriends = 16; // Số lượng bạn bè online

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate layout cho Fragment
        View view = inflater.inflate(R.layout.activity_list_friend, container, false);

        // Khởi tạo view
        friendsContainer = view.findViewById(R.id.friendsContainer);
        tvTotalFriends = view.findViewById(R.id.tvTotalFriends); // Lấy TextView hiển thị số bạn bè
        tvOnlineFriends = view.findViewById(R.id.tvOnlineFriends); // Lấy TextView hiển thị số bạn bè online

        // Cập nhật số liệu bạn bè
        tvTotalFriends.setText(String.valueOf(totalFriends));
        tvOnlineFriends.setText(String.valueOf(onlineFriends));

        // Thêm bạn bè vào layout (bạn có thể lấy dữ liệu động từ cơ sở dữ liệu hoặc API)
        addFriend("Nguyễn Thị Phương Anh", "⚡️ 1500 XP", "Đang hoạt động", R.drawable.avt_fr2);
        addFriend("Trần Hoàng Hiếu", "⚡️ 500 XP", "Online 2 giờ trước", R.drawable.avt_fr1);
        addFriend("Lê Văn Công", "⚡️ 600 XP", "Đang hoạt động", R.drawable.avt_fr5);
        addFriend("Phạm Thị Ánh Nguyên", "⚡️ 970 XP", "Online 1 ngày trước", R.drawable.avt_fr3);
        addFriend("Hoàng Thị Mai Phương", "⚡️ 310 XP", "Đang hoạt động", R.drawable.avt_fr4);
        addFriend("Vũ Thị Minh Anh", "⚡️ 4300 XP", "Online 3 giờ trước", R.drawable.avt_fr6);

        return view;
    }

    private void addFriend(String name, String experience, String onlineStatus, int avatarResId) {
        // Inflate một item bạn bè
        View friendView = getLayoutInflater().inflate(R.layout.item_friend, null);

        // Khởi tạo các view trong item bạn bè
        TextView tvName = friendView.findViewById(R.id.tvName);
        TextView tvExperience = friendView.findViewById(R.id.tvExperiencePoints);
        TextView tvStatus = friendView.findViewById(R.id.tvOnlineStatus);
        ImageView ivAvatar = friendView.findViewById(R.id.ivAvatar);

        // Gán giá trị cho các view
        tvName.setText(name);
        tvExperience.setText(experience);
        tvStatus.setText(onlineStatus);
        ivAvatar.setImageResource(avatarResId);

        // Thêm view bạn bè vào container
        friendsContainer.addView(friendView);
    }
}
