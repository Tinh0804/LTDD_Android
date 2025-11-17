package com.example.learninglanguageapp.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;


import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.Result;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.network.ApiClient;

import com.example.learninglanguageapp.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;

    public AuthRepository(Context context) {
        apiService = ApiClient.getApiService();
    }

    public void login(LoginRequest request, MutableLiveData<Result<UserResponse>> liveData) {
        apiService.login(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    liveData.postValue(Result.success(response.body().getData()));
                } else {
                    liveData.postValue(Result.failure(new Exception(response.body() != null ? response.body().getMessage() : "Login failed")));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                liveData.postValue(Result.failure(new Exception(t)));
            }
        });
    }

    public void register(RegisterRequest request, MutableLiveData<Result<UserResponse>> liveData) {
        apiService.register(request).enqueue(new Callback<ApiResponse<UserResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserResponse>> call, Response<ApiResponse<UserResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isStatus()) {
                    liveData.postValue(Result.success(response.body().getData()));
                } else {
                    liveData.postValue(Result.failure(new Exception(response.body() != null ? response.body().getMessage() : "Register failed")));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                liveData.postValue(Result.failure(new Exception(t)));
            }
        });
    }

    // Trong AuthRepository.java
    public void socialLogin(SocialLoginRequest request, MutableLiveData<Result<UserResponse>> resultLiveData) {
        apiService.socialLogin(request).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    resultLiveData.setValue(Result.success(response.body()));
                } else {
                    resultLiveData.setValue(Result.failure(new Exception("Login failed: " + response.message())));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                resultLiveData.setValue(Result.failure(new Exception(t.getMessage())));
            }
        });
    }

}
