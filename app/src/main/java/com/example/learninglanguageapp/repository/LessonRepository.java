package com.example.learninglanguageapp.repository;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonRepository {

    private ApiService api;

    public LessonRepository(Context context) {
        api = ApiClient.getApiService(context);
    }

    public void getWords(int lessonId,int userId,
                         MutableLiveData<List<Word>> wordsLiveData,
                         MutableLiveData<Boolean> loadingLiveData,
                         MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

        api.getWordsByLessonOfUser(lessonId,userId).enqueue(new Callback<ApiResponse<List<Word>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<Word>>> call, Response<ApiResponse<List<Word>>> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "API Success: " + response.body().getData().size() + " words");
                    wordsLiveData.setValue(response.body().getData());
                } else {
                    String error = "Error: " + response.code() + " - " + response.message();
                    Log.e(TAG, "API Error Response: " + error);
                    errorLiveData.setValue(error);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Word>>> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(t.getMessage());
            }
        });
    }
}
