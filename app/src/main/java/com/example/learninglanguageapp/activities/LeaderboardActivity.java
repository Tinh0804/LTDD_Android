package com.example.learninglanguageapp.activities;


import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.LeaderboardAdapter;
import com.example.learninglanguageapp.models.UIModel.LeaderBoardItem;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView recyclerViewLeaderboard;
    private LeaderboardAdapter adapter;
    private Button btnWeekly, btnMonthly, btnAllTime;
    private String currentFilter = "weekly";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        ImageView btnBack = findViewById(R.id.btnback);
        btnBack.setOnClickListener(v -> finish());

        initViews();
        setupRecyclerView();
        setupFilterButtons();
        loadLeaderboardData(currentFilter);
    }

    private void initViews() {
        recyclerViewLeaderboard = findViewById(R.id.recyclerViewLeaderboard);
        btnWeekly = findViewById(R.id.btnWeekly);
        btnMonthly = findViewById(R.id.btnMonthly);
        btnAllTime = findViewById(R.id.btnAllTime);
    }

    private void setupRecyclerView() {
        recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LeaderboardAdapter(this, new ArrayList<>());
        recyclerViewLeaderboard.setAdapter(adapter);
    }

    private void setupFilterButtons() {
        btnWeekly.setOnClickListener(v -> {
            currentFilter = "weekly";
            updateButtonStates(btnWeekly);
            loadLeaderboardData(currentFilter);
        });

        btnMonthly.setOnClickListener(v -> {
            currentFilter = "monthly";
            updateButtonStates(btnMonthly);
            loadLeaderboardData(currentFilter);
        });

        btnAllTime.setOnClickListener(v -> {
            currentFilter = "alltime";
            updateButtonStates(btnAllTime);
            loadLeaderboardData(currentFilter);
        });
    }

    private void updateButtonStates(Button selectedButton) {
        // Reset all buttons to unselected state
        btnWeekly.setBackgroundResource(R.drawable.bg_button_unselected);
        btnWeekly.setTextColor(ContextCompat.getColor(this, R.color.purple_200));

        btnMonthly.setBackgroundResource(R.drawable.bg_button_unselected);
        btnMonthly.setTextColor(ContextCompat.getColor(this, R.color.purple_200));

        btnAllTime.setBackgroundResource(R.drawable.bg_button_unselected);
        btnAllTime.setTextColor(ContextCompat.getColor(this, R.color.purple_200));

        // Set selected button state
        selectedButton.setBackgroundResource(R.drawable.bg_button_selected);
        selectedButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
    }

    private void loadLeaderboardData(String filter) {
        // Sample data - Avatar từ drawable
        List<LeaderBoardItem> leaderboardList = new ArrayList<>();

        // Weekly data example - Mỗi user có avatar riêng từ drawable
        leaderboardList.add(new LeaderBoardItem(1, "Nguyễn Thị Vy Hậu", 900, "avatar_1"));
        leaderboardList.add(new LeaderBoardItem(2, "Nguyễn Lương Bin", 872, "avatar_2"));
        leaderboardList.add(new LeaderBoardItem(3, "Nguyễn Thị Duy Phương", 850, "avatar_3"));
        leaderboardList.add(new LeaderBoardItem(4, "Lê Hoàng Quách Tỉnh", 830, "avatar_4"));
        leaderboardList.add(new LeaderBoardItem(5, "Nguyễn Thị Vy Hoa", 790, "avatar_5"));
        leaderboardList.add(new LeaderBoardItem(6, "Nguyễn Thị Oanh", 750, "avatar_6"));
        leaderboardList.add(new LeaderBoardItem(7, "Nguyễn Thị Duyên", 700, "avatar_7"));
        leaderboardList.add(new LeaderBoardItem(8, "Nguyễn Thị Thương", 550, "avatar_8"));

        adapter.updateList(leaderboardList);
    }

}