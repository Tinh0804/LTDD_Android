// network/ApiService.java
package com.example.learninglanguageapp.network;

import android.annotation.SuppressLint;

import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.LoginResponse;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.models.Word;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;

// Đây chỉ là ví dụ, bạn thay đổi theo API thật của bạn
public interface ApiService {

    //Bé Bin
    @POST("api/Auth/login")
    Call<ApiResponse<UserResponse>> login(@Body LoginRequest request);

    @POST("api/Auth/register")
    Call<ApiResponse<UserResponse>> register(@Body RegisterRequest request);

    @POST("api/Auth/external-login")
    Call<ApiResponse<UserResponse>> socialLogin(@Body SocialLoginRequest request);

    //VY HẬU
    @GET("units")
    Call<List<Unit>> getAllUnits();

    @GET("units/{unitId}/lessons")
    Call<List<Lesson>> getLessonsByUnit(@Path("unitId") int unitId);

    @GET("lessons/{lessonId}/words")
    Call<List<Word>> getWordsByLesson(@Path("lessonId") int lessonId);
}
