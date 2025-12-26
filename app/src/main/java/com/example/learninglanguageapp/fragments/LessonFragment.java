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
import com.example.learninglanguageapp.adapters.LessonAdapter;
import com.example.learninglanguageapp.viewmodels.LessonViewModel;

public class LessonFragment extends Fragment {
    private LessonViewModel viewModel;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson, container, false);
        recyclerView = view.findViewById(R.id.rvLessons);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);

        int unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getLessons().observe(getViewLifecycleOwner(), lessons -> {
            recyclerView.setAdapter(new LessonAdapter(lessons, lesson -> {
                PronunciationFragment pronunciationFragment = new PronunciationFragment();
                Bundle args = new Bundle();
                args.putInt("lessonId", lesson.getLessonId());
                pronunciationFragment.setArguments(args);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, pronunciationFragment)
                        .addToBackStack(null)
                        .commit();
            }));
        });

        viewModel.getError().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.fetchLessons(unitId);
    }
}