package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.*;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class GameFragment extends Fragment {

    private ExerciseViewModel viewModel;
    private Class<?> nextActivityClass;
    private int unitId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(ExerciseViewModel.class);
        unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;

        initEventListeners(view);
        observeViewModel();
    }

    private void initEventListeners(View view) {
        // Gom nhóm các View
        CardView cardImageWord = view.findViewById(R.id.cardImageWordGame);
        CardView cardArrangeWord = view.findViewById(R.id.cardArrangeWordGame);
        CardView cardMatching = view.findViewById(R.id.cardMatchingGame);
        CardView cardVocabulary = view.findViewById(R.id.cardVocabularyGame);
        CardView cardPronunciation = view.findViewById(R.id.cardPronunciation);

        // Click listeners đơn giản
        cardImageWord.setOnClickListener(v -> triggerGame("listen", SelectImageGameActivity.class));
        cardArrangeWord.setOnClickListener(v -> triggerGame("word_order", GameWordropActivity.class));
        cardMatching.setOnClickListener(v -> triggerGame("match_pairs", MatchingGameActivity.class));

        // MatchGameActivity cần logic random LessonId đặc biệt
        cardVocabulary.setOnClickListener(v -> triggerGame("match_pairs", MatchGameActivity.class));

        cardPronunciation.setOnClickListener(v -> navigateToLessonFragment());
    }

    private void observeViewModel() {
        viewModel.exercisesLiveData.observe(getViewLifecycleOwner(), exercises -> {
            if (exercises == null || exercises.isEmpty() || nextActivityClass == null) return;
            handleGameNavigation(exercises);
            // Reset trạng thái sau khi xử lý
            viewModel.exercisesLiveData.setValue(null);
            nextActivityClass = null;
        });

        viewModel.errorLiveData.observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
                viewModel.errorLiveData.setValue(null);
            }
        });
    }

    private void handleGameNavigation(List<Exercise> exercises) {
        Intent intent = new Intent(requireContext(), nextActivityClass);

        if (nextActivityClass == MatchGameActivity.class) {
            int randomLessonId = getRandomLessonId(exercises);
            if (randomLessonId != -1)
                intent.putExtra("lessonId", randomLessonId);
            else {
                Toast.makeText(requireContext(), "Không tìm thấy bài học!", Toast.LENGTH_SHORT).show();
                return;
            }
        } else
            // Các game khác dùng unitId
            intent.putExtra("unitId", unitId);

        startActivity(intent);
    }

    private void triggerGame(String type, Class<?> activityClass) {
        if (unitId == -1) {
            Toast.makeText(requireContext(), "Unit ID không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }
        this.nextActivityClass = activityClass;
        viewModel.fetchExercisesByType(unitId, type);
    }

    private int getRandomLessonId(List<Exercise> exercises) {
        List<Integer> ids = exercises.stream().map(Exercise::getLessonId).distinct().collect(Collectors.toList());

        return ids.isEmpty() ? -1 : ids.get(new Random().nextInt(ids.size()));
    }

    private void navigateToLessonFragment() {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putInt("unitId", unitId);
        fragment.setArguments(args);

        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
    }
}