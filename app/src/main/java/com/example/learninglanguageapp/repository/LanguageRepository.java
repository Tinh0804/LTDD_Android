package com.example.learninglanguageapp.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.LanguageResponse;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageRepository {

    private final ApiService apiService;

    public LanguageRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    public void getLanguageById(
            int id,
            java.util.function.Consumer<LanguageResponse> onSuccess,
            MutableLiveData<String> error
    ) {
        apiService.getLanguageById(id).enqueue(new Callback<ApiResponse<LanguageResponse>>() {
            @Override
            public void onResponse(
                    Call<ApiResponse<LanguageResponse>> call,
                    Response<ApiResponse<LanguageResponse>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().getData() != null) {

                    onSuccess.accept(response.body().getData());
                } else {
                    error.setValue("Không tải được language");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<LanguageResponse>> call, Throwable t) {
                error.setValue(t.getMessage());
            }
        });
    }
}
