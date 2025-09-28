package com.example.learninglanguageapp.models.Response;

import android.annotation.SuppressLint;

import com.google.firebase.firestore.auth.User;

public class LoginResponse {
    private boolean success;
    private String message;
    private String token;   // token nếu đăng nhập thành công
    private User user;      // thông tin user trả về (nếu API có)

    // Getter & Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @SuppressLint("RestrictedApi")
    public User getUser() {
        return user;
    }
    @SuppressLint("RestrictedApi")
    public void setUser( User user) {
        this.user = user;
    }
}