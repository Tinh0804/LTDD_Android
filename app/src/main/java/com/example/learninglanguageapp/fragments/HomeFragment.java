package com.example.learninglanguageapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.activities.LessonActivity;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        View cardStarLevel = view.findViewById(R.id.cardStarLevel);

        cardStarLevel.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LessonActivity.class);

            intent.putExtra("LESSON_ID", 1);
//            intent.putExtra("USER_ID", 123);
            intent.putExtra("CATEGORY_NAME", "Animal");

            startActivity(intent);
        });

        return view;
    }
}

