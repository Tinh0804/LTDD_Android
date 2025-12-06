// models/Response/LoginResponse.java
package com.example.learninglanguageapp.models.Response;

import com.example.learninglanguageapp.models.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    private boolean success;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("refreshToken")
    private String refreshToken;

    @SerializedName("isNewUser")
    private boolean isNewUser = false;

    // Backend trả về "data" -> "user" -> thông tin chi tiết
    // Nên mẹ map thẳng vào User model của con luôn cho tiện
    @SerializedName("data")
    private DataWrapper data;

    // Class con để map đúng cấu trúc JSON
    public static class DataWrapper {
        @SerializedName("user")
        public User user;
    }

    // Getter & Setter siêu gọn
    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public User getUser() {
        return data != null ? data.user : null;
    }

    // Bonus: method tiện lợi để check login thành công
    public boolean isLoginSuccessful() {
        return success && token != null;
    }
}