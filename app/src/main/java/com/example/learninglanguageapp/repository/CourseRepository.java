package com.example.learninglanguageapp.repository;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Course;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CourseRepository {
    private ApiService apiService;

    public CourseRepository(Context context) {
        this.apiService = ApiClient.getApiService(context);
    }

    public void getCourses(int fromId, int toId,
                           MutableLiveData<List<Course>> coursesLiveData,
                           MutableLiveData<Boolean> loading,
                           MutableLiveData<String> error) {

        loading.postValue(true);

        apiService.getCourses(fromId, toId).enqueue(new Callback<ApiResponse<List<Course>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Course>>> call, Response<ApiResponse<List<Course>>> response) {
                loading.postValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    // Trả về danh sách data bên trong CourseResponse
                    coursesLiveData.postValue(response.body().getData());
                } else {
                    error.postValue("Failed to load courses");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Course>>> call, Throwable t) {
                loading.postValue(false);
                error.postValue(t.getMessage());
            }
        });
    }
}