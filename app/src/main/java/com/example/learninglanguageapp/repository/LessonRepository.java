package com.example.learninglanguageapp.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.models.Response.WordResponse;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LessonRepository {

    private ApiService api;

    public LessonRepository() {
        api = ApiClient.getApiService();
    }

    public void getWords(int lessonId,
                         MutableLiveData<List<Word>> wordsLiveData,
                         MutableLiveData<Boolean> loadingLiveData,
                         MutableLiveData<String> errorLiveData) {

        loadingLiveData.setValue(true);

        api.getWordsByLesson(lessonId).enqueue(new Callback<WordResponse>() {
            @Override
            public void onResponse(Call<WordResponse> call, Response<WordResponse> response) {
                loadingLiveData.setValue(false);

                if (response.isSuccessful() && response.body() != null) {
                    WordResponse res = response.body();

                    if (res.status && res.data != null) {
                        wordsLiveData.setValue(res.data);
                    } else {
                        errorLiveData.setValue(res.message != null ? res.message : "Lỗi dữ liệu");
                    }

                } else {
                    errorLiveData.setValue("Lỗi server");
                }
            }

            @Override
            public void onFailure(Call<WordResponse> call, Throwable t) {
                loadingLiveData.setValue(false);
                errorLiveData.setValue(t.getMessage());
            }
        });
    }
}
