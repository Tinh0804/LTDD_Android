package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.GameWordropActivity;
import com.example.learninglanguageapp.activities.MatchGameActivity;
import com.example.learninglanguageapp.activities.MatchingGameActivity;
import com.example.learninglanguageapp.activities.SelectImageGameActivity;

public class GameFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_game, container, false);
        CardView cardImageWordGame = view.findViewById(R.id.cardImageWordGame);
        CardView cardViewArrangeWord = view.findViewById(R.id.cardArrangeWordGame);
        CardView cardViewMatchingGame = view.findViewById(R.id.cardMatchingGame);
        CardView cardViewPictureVocabulary = view.findViewById(R.id.cardVocabularyGame);
        cardImageWordGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SelectImageGameActivity.class);
            int unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;
            intent.putExtra("unitId", unitId);
            startActivity(intent);
        });
        cardViewPictureVocabulary.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MatchGameActivity.class);
            int unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;
            intent.putExtra("unitId", unitId);
            startActivity(intent);
        });
        cardViewArrangeWord.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), GameWordropActivity.class);
            int unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;
            intent.putExtra("unitId", unitId);
            startActivity(intent);
        });
        cardViewMatchingGame.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MatchingGameActivity.class);
            int unitId = getArguments() != null ? getArguments().getInt("unitId", -1) : -1;
            intent.putExtra("unitId", unitId);
            startActivity(intent);
        });
        return view;
    }
}
