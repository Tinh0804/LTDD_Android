package com.example.learninglanguageapp.repository;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.mapper.ExerciseMapper;
import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.storage.AppDatabase;
import com.example.learninglanguageapp.storage.DAOs.ExerciseDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExerciseRepository {

    private ApiService api;
    private ExerciseDAO exerciseDao;

    public ExerciseRepository(Context context) {
        api = ApiClient.getApiService(context);
        exerciseDao = AppDatabase.getInstance(context).exerciseDao();
    }

    public void getExercisesByUnit(int unitId,
                                   MutableLiveData<List<Exercise>> exercisesLiveData,
                                   MutableLiveData<Boolean> loadingLiveData,
                                   MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

        new Thread(() -> {
            // 1️⃣ Lấy offline trước
            List<ExerciseEntity> cached = exerciseDao.getExercisesByLesson(List.<Integer>of(unitId)); // Room lưu lessonId
            if (!cached.isEmpty()) {
                List<Exercise> list = new ArrayList<>();
                for (ExerciseEntity e : cached) list.add(ExerciseMapper.toDomain(e));
                exercisesLiveData.postValue(list);
                loadingLiveData.postValue(false);
                return;
            }

            // 2️⃣ Gọi API nếu offline trống
            api.getExercisesByUnit(unitId)
                    .enqueue(new Callback<ApiResponse<List<Exercise>>>() {
                        @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                        @Override
                        public void onResponse(Call<ApiResponse<List<Exercise>>> call,
                                               Response<ApiResponse<List<Exercise>>> response) {
                            loadingLiveData.setValue(false);
                            if (response.isSuccessful() && response.body() != null) {
                                List<Exercise> list = response.body().getData();
                                exercisesLiveData.setValue(list);

                                // lưu xuống DB
                                new Thread(() -> exerciseDao.insertExercises(
                                        list.stream().map(ExerciseMapper::toEntity).toList()
                                )).start();
                            } else {
                                errorLiveData.setValue("API Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<List<Exercise>>> call, Throwable t) {
                            loadingLiveData.setValue(false);
                            errorLiveData.setValue(t.getMessage());
                        }
                    });

        }).start();
    }
    // Phương thức tải bài tập theo Unit và Type (Dùng khi click vào Game Card)
    public void getExercisesByUnitAndType(int unitId, String type,
                                          MutableLiveData<List<Exercise>> exercisesLiveData,
                                          MutableLiveData<Boolean> loadingLiveData,
                                          MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

        new Thread(() -> {
            // 1️⃣ Lấy offline trước (Room)
            // Kiểm tra cache TẤT CẢ các type của Unit. Nếu có, thì không cần gọi API
            List<ExerciseEntity> cached = exerciseDao.getExercisesByLessonAndType(unitId, type);
            if (!cached.isEmpty()) {
                List<Exercise> list = cached.stream().map(ExerciseMapper::toDomain).collect(Collectors.toList());
                exercisesLiveData.postValue(list);
                loadingLiveData.postValue(false);
                return;
            }

            // 2️⃣ Gọi API nếu cache trống (vì chưa từng tải Unit này)
            api.getExercisesByUnit(unitId)
                    .enqueue(new Callback<ApiResponse<List<Exercise>>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<List<Exercise>>> call,
                                               Response<ApiResponse<List<Exercise>>> response) {
                            loadingLiveData.setValue(false);

                            if (response.isSuccessful() && response.body() != null) {
                                List<Exercise> allExercisesFromApi = response.body().getData();

                                new Thread(() -> {
                                    List<ExerciseEntity> entitiesToSave = allExercisesFromApi.stream()
                                            .map(ExerciseMapper::toEntity)
                                            .collect(Collectors.toList());
                                    // ❗ Sửa lỗi: Lưu tất cả để cache integrity được đảm bảo
                                    exerciseDao.insertExercises(entitiesToSave);

                                    // B. TẢI LẠI (VÀ LỌC) TỪ ROOM
                                    List<ExerciseEntity> filteredCached = exerciseDao.getExercisesByLessonAndType(unitId, type);
                                    List<Exercise> filteredList = filteredCached.stream()
                                            .map(ExerciseMapper::toDomain)
                                            .collect(Collectors.toList());

                                    // C. POST GIÁ TRỊ CUỐI CÙNG LÊN LIVE DATA
                                    exercisesLiveData.postValue(filteredList);
                                }).start();

                            } else {
                                // Post lỗi trên Main Thread
                                errorLiveData.setValue("API Error: " + response.code());
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<List<Exercise>>> call, Throwable t) {
                            loadingLiveData.setValue(false);
                            errorLiveData.setValue(t.getMessage());
                        }
                    });
        }).start();
    }
    // Trong ExerciseRepository.java
    public void saveExercises(List<ExerciseEntity> exercises) {
        new Thread(() -> exerciseDao.insertExercises(exercises)).start();
    }



}
