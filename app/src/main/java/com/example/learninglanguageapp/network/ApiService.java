// network/ApiService.java
package com.example.learninglanguageapp.network;

import androidx.room.Query;

import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Request.PaymentRequest;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Request.SocialLoginRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.BalanceResponse;
import com.example.learninglanguageapp.models.Response.LoginResponse;
import com.example.learninglanguageapp.models.Response.PaymentResponse;
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

    @GET("api/Words/user/lesson/{lessonId}")
    Call<ApiResponse<List<Word>>> getWordsByLessonOfUser(
            @Path("lessonId") int lessonId
    );

    @GET("shop/balance/{userId}")
    Call<ApiResponse<BalanceResponse>> getBalance(@Path("userId") int userId);

    /**
     * Tạo payment request và nhận payment URL
     */
    @POST("shop/payment/create")
    Call<ApiResponse<PaymentResponse>> createPayment(@Body PaymentRequest request);

    /**
     * Verify payment status sau khi callback
     */
    @GET("shop/payment/verify/{transactionId}")
    Call<ApiResponse<PaymentResponse>> verifyPayment(@Path("transactionId") String transactionId);

}