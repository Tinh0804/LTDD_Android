package com.example.learninglanguageapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.LeaderboardAdapter;
import com.example.learninglanguageapp.models.Response.Result;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.viewmodels.AuthViewModel;

import java.util.List;

public class LeaderBoardFragment extends Fragment {

    private AuthViewModel viewModel;
    private LeaderboardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_leaderboard, container, false);

        // RecyclerView + Adapter
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new LeaderboardAdapter();
        recyclerView.setAdapter(adapter);

        // Dùng lại AuthViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Gọi API lấy bảng xếp hạng thật từ /api/Profile
        viewModel.loadRanking();

        // Observe – ĐÃ DÙNG ĐÚNG getValue() và getException() CỦA BẠN
        viewModel.getRankingLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccess()) {
                List<UserResponse> list = result.getValue();
                adapter.submitList(list);
            }
        });

        // 3 nút Weekly/Monthly/AllTime → để đẹp, click reload lại bảng
        view.findViewById(R.id.btnWeekly).setOnClickListener(v -> viewModel.loadRanking());
        view.findViewById(R.id.btnMonthly).setOnClickListener(v -> viewModel.loadRanking());
        view.findViewById(R.id.btnAllTime).setOnClickListener(v -> viewModel.loadRanking());

        // Nút back
        view.findViewById(R.id.btnback).setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return view;
    }
}