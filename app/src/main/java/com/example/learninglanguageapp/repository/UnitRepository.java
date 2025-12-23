package com.example.learninglanguageapp.repository;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.models.UnitWithLessons;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnitRepository {
    private final ApiService apiService;

    public UnitRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
    }

    public void loadUnitsWithLessons(
            String authHeader,
            MutableLiveData<List<UnitWithLessons>> unitsLiveData,
            MutableLiveData<Boolean> isLoading,
            MutableLiveData<String> errorLiveData) {

        isLoading.setValue(true);
        apiService.getMyUnitsWithDetails(authHeader).enqueue(new Callback<ApiResponse<List<Unit>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Unit>>> call, Response<ApiResponse<List<Unit>>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<Unit> units = response.body().getData();
                    List<UnitWithLessons> results = new ArrayList<>();

                    for (Unit unit : units)
                        results.add(new UnitWithLessons(unit, unit.getLessons()));
                    unitsLiveData.setValue(results);
                } else
                    errorLiveData.setValue("Lỗi tải dữ liệu: " + response.code());
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Unit>>> call, Throwable t) {
                isLoading.setValue(false);
                errorLiveData.setValue("Lỗi mạng: " + t.getMessage());
            }
        });
    }
}
