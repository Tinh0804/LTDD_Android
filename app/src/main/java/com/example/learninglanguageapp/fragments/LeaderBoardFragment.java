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
import com.example.learninglanguageapp.adapters.LeaderboardAdapter;
import com.example.learninglanguageapp.viewmodels.LeaderboardViewModel;

public class LeaderBoardFragment extends Fragment {

    private LeaderboardViewModel viewModel;
    private LeaderboardAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_leaderboard, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewLeaderboard);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new LeaderboardAdapter(requireContext());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this)
                .get(LeaderboardViewModel.class);

        viewModel.getLeaderboard().observe(getViewLifecycleOwner(),
                adapter::submitList);

        viewModel.getError().observe(getViewLifecycleOwner(),
                msg -> Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show());

        view.findViewById(R.id.btnWeekly).setOnClickListener(v -> viewModel.loadLeaderboard());
        view.findViewById(R.id.btnMonthly).setOnClickListener(v -> viewModel.loadLeaderboard());
        view.findViewById(R.id.btnAllTime).setOnClickListener(v -> viewModel.loadLeaderboard());

        view.findViewById(R.id.btnback).setOnClickListener(v ->
                requireActivity().getOnBackPressedDispatcher().onBackPressed());

        return view;
    }
}
