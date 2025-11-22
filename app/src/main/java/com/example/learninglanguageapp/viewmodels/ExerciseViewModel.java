package com.example.learninglanguageapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.repository.ExerciseRepository;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {

    private ExerciseRepository repository;
    public MutableLiveData<List<Exercise>> exercisesLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    public MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
    }

    public void fetchExercises(int unitId) {
        repository.getExercisesByUnit(unitId, exercisesLiveData, loadingLiveData, errorLiveData);
    }
    public void fetchExercisesByType(int unitId, String type) {
        repository.getExercisesByUnitAndType(unitId, type, exercisesLiveData, loadingLiveData, errorLiveData);
    }
    // Trong ExerciseViewModel.java
    public void saveUnitExercises(List<ExerciseEntity> exercises) {
        new Thread(() -> {
            if (exercises != null && !exercises.isEmpty()) {
                repository.saveExercises(exercises); // gọi Repository để lưu
            }
        }).start();
    }



}
