package com.example.learninglanguageapp.repository;


import android.content.Context;

import com.example.learninglanguageapp.models.Entities.ExerciseEntity;

import java.util.List;

public class UnitRepository {

    private final ExerciseRepository exerciseRepo;

    public UnitRepository(Context context) {
        this.exerciseRepo = new ExerciseRepository(context);
    }

    // Lưu toàn bộ exercises của Unit vào Local DB
    public void saveUnitExercises(List<ExerciseEntity> exercises) {
        exerciseRepo.saveExercises(exercises);
    }
}
