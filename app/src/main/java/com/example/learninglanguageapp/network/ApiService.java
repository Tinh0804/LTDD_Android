// network/ApiService.java
package com.example.learninglanguageapp.network;

import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.LoginResponse;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.models.Word;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // 1. LOGIN THƯỜNG – TRẢ VỀ ApiResponse<LoginResponse>
    @POST("api/Auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest request);

    // 2. ĐĂNG KÝ – TRẢ VỀ ApiResponse<UserResponse>
    @POST("api/Auth/register")
    Call<ApiResponse<UserResponse>> register(@Body RegisterRequest request);

    // 3. SOCIAL LOGIN – BACKEND TRẢ VỀ LoginResponse TRỰC TIẾP (không bọc ApiResponse)
    @POST("api/Auth/external-login")
    Call<ApiResponse<LoginResponse>> socialLogin(@Body SocialLoginRequest request);

    // 4. LẤY PROFILE – TRẢ VỀ ApiResponse<UserResponse>
    @GET("api/Profile/myInfo")
    Call<ApiResponse<UserResponse>> getMyProfile(@Header("Authorization") String authHeader);

    @GET("api/Words/lesson/{lessonId}/user/{userId}")
    Call<ApiResponse<List<Word>>> getWordsByLessonOfUser(
            @Path("lessonId") int lessonId,
            @Path("userId") int userId
    );
}