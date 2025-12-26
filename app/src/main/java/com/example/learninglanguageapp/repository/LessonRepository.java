package com.example.learninglanguageapp.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.mapper.WordMapper;
import com.example.learninglanguageapp.models.Entities.WordEntity;
import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import com.example.learninglanguageapp.storage.AppDatabase;
import com.example.learninglanguageapp.storage.DAOs.WordDao;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.stream.Collectors;
public class LessonRepository {

    private ApiService api;
    private WordDao wordDao;


    public LessonRepository(Context context) {
        api = ApiClient.getApiService(context);
        wordDao = AppDatabase.getInstance(context).wordDao();
    }

    public void getWords(int lessonId,
                         MutableLiveData<List<Word>> wordsLiveData,
                         MutableLiveData<Boolean> loadingLiveData,
                         MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

        // 1️⃣ Lấy offline trước
        new Thread(() -> {
            List<WordEntity> cached = wordDao.getWordsByLesson(lessonId);

            if (!cached.isEmpty()) {
                // Có dữ liệu local → dùng luôn
                List<Word> list = new ArrayList<>();
                for (WordEntity wordEntity:cached) {
                   list.add(WordMapper.toDomain(wordEntity));
                }
                wordsLiveData.postValue(list);
                loadingLiveData.postValue(false);
                return;
            }

            // 2️⃣ Không có → gọi API
            api.getWordsByLessonOfUser(lessonId)
                    .enqueue(new Callback<ApiResponse<List<Word>>>() {

                        @RequiresApi(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
                        @Override
                        public void onResponse(Call<ApiResponse<List<Word>>> call,
                                               Response<ApiResponse<List<Word>>> response) {

                            loadingLiveData.setValue(false);

                            if (response.isSuccessful() && response.body() != null) {
                                List<Word> list = response.body().getData();

                                wordsLiveData.setValue(list);

                                // 3️⃣ Lưu xuống DB
                                new Thread(() -> {
                                    wordDao.insertWords(
                                            list.stream()
                                                    .map(WordMapper::toEntity)
                                                    .collect(Collectors.toList()) // <--- SỬA DÒNG NÀY (Thay .toList() bằng .collect(...))
                                    );

                                }).start();

                            } else {
                                errorLiveData.setValue("API Error");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<List<Word>>> call, Throwable t) {
                            loadingLiveData.setValue(false);
                            errorLiveData.setValue(t.getMessage());
                        }
                    });
        }).start();
    }
    public void getWordsForMatchGame(int lessonId,
                                     Consumer<List<Word>> onSuccess,
                                     Consumer<String> onError) {

        new Thread(() -> {
            // 1. Lấy từ cache trước (offline first)
            List<WordEntity> cached = wordDao.getWordsByLesson(lessonId);
            if (!cached.isEmpty()) {
                List<Word> words = new ArrayList<>();
                for (WordEntity entity : cached) {
                    words.add(WordMapper.toDomain(entity));
                }
                onSuccess.accept(words);
                return;
            }

            // 2. Không có cache → gọi API
            api.getWordsByLessonOfUser(lessonId)
                    .enqueue(new Callback<ApiResponse<List<Word>>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<List<Word>>> call,
                                               Response<ApiResponse<List<Word>>> response) {

                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                List<Word> words = response.body().getData();

                                // Trả kết quả cho ViewModel
                                onSuccess.accept(words);

                                // Lưu vào Room (background)
                                new Thread(() -> {
                                    List<WordEntity> entities = new ArrayList<>();
                                    for (Word word : words) {
                                        entities.add(WordMapper.toEntity(word));
                                    }
                                    wordDao.insertWords(entities);
                                }).start();

                            } else {
                                onError.accept("Không tải được dữ liệu từ server");
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiResponse<List<Word>>> call, Throwable t) {
                            onError.accept(t.getMessage() != null ? t.getMessage() : "Lỗi kết nối mạng");
                        }
                    });
        }).start();
    }

    public void getLessonsByUnit(int unitId,
                                 MutableLiveData<List<Lesson>> lessonsLiveData,
                                 MutableLiveData<Boolean> isLoading,
                                 MutableLiveData<String> errorLiveData) {
        isLoading.setValue(true);
        api.getLessonsByUnit(unitId).enqueue(new Callback<ApiResponse<List<Lesson>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Lesson>>> call, Response<ApiResponse<List<Lesson>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    lessonsLiveData.setValue(response.body().getData());
                } else {
                    errorLiveData.setValue("Không thể tải danh sách bài học");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Lesson>>> call, Throwable t) {
                isLoading.setValue(false);
                errorLiveData.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
}
