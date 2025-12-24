package com.example.learninglanguageapp.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Entities.TopUserEntity;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.LeaderboardResponse;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.List;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardRepository {

    private final ApiService apiService;

    public LeaderboardRepository(Context context) {
        apiService = ApiClient.getApiService(context);
    }

    public void loadLeaderboard(
            String authHeader,
            Consumer<List<TopUserEntity>> onSuccess,
            MutableLiveData<Boolean> isLoading,
            MutableLiveData<String> error
    ) {
        isLoading.setValue(true);

        apiService.getLeaderboard(authHeader)
                .enqueue(new Callback<ApiResponse<LeaderboardResponse>>() {

                    @Override
                    public void onResponse(
                            Call<ApiResponse<LeaderboardResponse>> call,
                            Response<ApiResponse<LeaderboardResponse>> response) {

                        isLoading.setValue(false);

                        if (response.isSuccessful()
                                && response.body() != null
                                && response.body().getData() != null
                                && response.body().getData().getTopUsers() != null) {

                            onSuccess.accept(
                                    response.body().getData().getTopUsers()
                            );

                        } else {
                            error.setValue("Không tải được leaderboard");
                        }
                    }
                    @Override
                    public void onFailure(
                            Call<ApiResponse<LeaderboardResponse>> call,
                            Throwable t) {

                        isLoading.setValue(false);
                        error.setValue(t.getMessage());
                    }
                });
    }

}
