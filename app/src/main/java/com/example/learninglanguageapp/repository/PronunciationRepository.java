package com.example.learninglanguageapp.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.PronunciationResponse;
import com.example.learninglanguageapp.models.Sentence;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PronunciationRepository {
    private final ApiService apiService;

    public PronunciationRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
    }

    public void getSentences(int lessonId, MutableLiveData<List<Sentence>> sentencesLiveData) {
        apiService.getSentencesByLesson(lessonId).enqueue(new Callback<ApiResponse<List<Sentence>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Sentence>>> call, Response<ApiResponse<List<Sentence>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sentencesLiveData.setValue(response.body().getData());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Sentence>>> call, Throwable t) {}
        });
    }

    public void evaluate(int sentenceId, String audioBase64,
                         MutableLiveData<PronunciationResponse> resultLiveData,
                         MutableLiveData<Boolean> isProcessing) {
        isProcessing.setValue(true);
        Map<String, Object> body = new HashMap<>();
        body.put("sentenceId", sentenceId);
        body.put("audioBase64", audioBase64);

        apiService.evaluatePronunciation(body).enqueue(new Callback<ApiResponse<PronunciationResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<PronunciationResponse>> call, Response<ApiResponse<PronunciationResponse>> response) {
                isProcessing.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.setValue(response.body().getData());
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<PronunciationResponse>> call, Throwable t) {
                isProcessing.setValue(false);
            }
        });
    }
}