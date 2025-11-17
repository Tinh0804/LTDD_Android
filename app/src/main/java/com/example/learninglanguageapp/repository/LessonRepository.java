package com.example.learninglanguageapp.repository;

import android.util.Log;

import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonRepository {
    private static final String TAG = "WordRepository";
    private final ApiService apiService;

    public LessonRepository() {
        apiService = ApiClient.getApiService();
    }

    public interface WordCallback {
        void onSuccess(List<Word> words);
        void onError(String error);
    }

    public void getWordsByLesson(int lessonId, int userId, WordCallback callback) {
        apiService.getWordsByLesson(lessonId, userId)
                .enqueue(new Callback<ApiResponse<List<Word>>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<List<Word>>> call, Response<ApiResponse<List<Word>>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body().getData());
                        } else {
                            String error = "Error: " + response.code() + " - " + response.message();
                            Log.e(TAG, error);
                            callback.onError(error);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<List<Word>>> call, Throwable t) {
                        String error = "Network error: " + t.getMessage();
                        Log.e(TAG, error, t);
                        callback.onError(error);
                    }
                });

    }
}