// network/ApiService.java
package com.example.learninglanguageapp.network;

import android.annotation.SuppressLint;

import com.example.learninglanguageapp.models.Request.LoginRequest;
import com.example.learninglanguageapp.models.Response.LoginResponse;
import com.google.firebase.firestore.auth.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.Path;

// Đây chỉ là ví dụ, bạn thay đổi theo API thật của bạn
public interface ApiService {

    // Ví dụ API login
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // Ví dụ API lấy danh sách users
    @SuppressLint("RestrictedApi")
    @GET("users")
    Call<List<User>> getUsers();

    // Ví dụ API lấy user theo id
    @SuppressLint("RestrictedApi")
    @GET("users/{id}")
    Call<User> getUser(@Path("id") String id);
}
