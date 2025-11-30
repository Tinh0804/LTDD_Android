package com.example.learninglanguageapp.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.models.UnitWithLessons;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitRepository {

    private static final String TAG = "UnitRepository";
    private final ApiService apiService;
    private final ExerciseRepository exerciseRepo;

    public UnitRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
        this.exerciseRepo = new ExerciseRepository(context);
    }

    public void saveUnitExercises(List<ExerciseEntity> exercises) {
        exerciseRepo.saveExercises(exercises);
    }
    public void loadUnitsWithLessons(
            MutableLiveData<List<UnitWithLessons>> unitsLiveData,
            MutableLiveData<Boolean> isLoading,
            MutableLiveData<String> errorLiveData) {

        isLoading.setValue(true);
        apiService.getUnits().enqueue(new Callback<ApiResponse<List<Unit>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Unit>>> call, Response<ApiResponse<List<Unit>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Unit> units = response.body().getData();
                    if (units.isEmpty()) {
                        unitsLiveData.setValue(new ArrayList<>());
                        isLoading.setValue(false);
                        return;
                    }
                    fetchLessonsForUnits(units, unitsLiveData, isLoading, errorLiveData);

                } else {
                    isLoading.setValue(false);
                    String error = "Failed to fetch units: " + (response.errorBody() != null ? response.message() : "Unknown error");
                    Log.e(TAG, error);
                    errorLiveData.setValue(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Unit>>> call, Throwable t) {
                isLoading.setValue(false);
                Log.e(TAG, "Unit API failure", t);
                errorLiveData.setValue("Network error: " + t.getMessage());
            }
        });
    }

    /*Lặp qua danh sách Units và gọi API để lấy Lessons cho từng Unit.*/
    private void fetchLessonsForUnits(List<Unit> units, MutableLiveData<List<UnitWithLessons>> unitsLiveData, MutableLiveData<Boolean> isLoading, MutableLiveData<String> errorLiveData) {
        List<UnitWithLessons> results = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(units.size());
        for (Unit unit : units) {
            apiService.getLessonsByUnit(unit.getUnitId()).enqueue(new Callback<ApiResponse<List<Lesson>>>() {
                @Override
                public void onResponse(Call<ApiResponse<List<Lesson>>> call, Response<ApiResponse<List<Lesson>>> response) {
                    List<Lesson> lessons = new ArrayList<>();
                    if (response.isSuccessful() && response.body() != null && response.body().getData() != null)
                        lessons = response.body().getData();
                    synchronized (results) {
                        results.add(new UnitWithLessons(unit, lessons));
                    }
                    if (counter.decrementAndGet() == 0) {
                        // Sắp xếp lại theo orderIndex của Unit
                        results.sort((u1, u2) -> Integer.compare(u1.getUnit().getOrderIndex(), u2.getUnit().getOrderIndex()));
                        unitsLiveData.setValue(results);
                        isLoading.setValue(false);
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<List<Lesson>>> call, Throwable t) {
                    Log.e(TAG, "Lesson API failure for Unit " + unit.getUnitId(), t);
                    synchronized (results) {
                        results.add(new UnitWithLessons(unit, new ArrayList<>()));
                    }
                    if (counter.decrementAndGet() == 0) {
                        results.sort((u1, u2) -> Integer.compare(u1.getUnit().getOrderIndex(), u2.getUnit().getOrderIndex()));
                        unitsLiveData.setValue(results);
                        isLoading.setValue(false);
                    }
                }
            });
        }
    }
}