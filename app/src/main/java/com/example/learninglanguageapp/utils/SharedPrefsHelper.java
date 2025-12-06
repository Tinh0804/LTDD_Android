package com.example.learninglanguageapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.models.User;
import com.google.gson.Gson;

public class SharedPrefsHelper {
    private static final String PREF_NAME = "app_prefs";

    // Key cho auth
    public static final String KEY_TOKEN = "jwt_token";
    public static final String KEY_REFRESH_TOKEN = "refresh_token";
    public static final String KEY_IS_NEW_USER = "is_new_user";

    // Key cho user info (nếu muốn lưu luôn cả User)
    public static final String KEY_USER_JSON = "user_json";

    private final SharedPreferences prefs;

    public SharedPrefsHelper(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Các method cũ của con
    public void saveString(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    public void saveLong(String key, long value) {
        prefs.edit().putLong(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return prefs.getLong(key, defaultValue);
    }

    public void clearAll() {
        prefs.edit().clear().apply();
    }

    // Thêm vài method tiện ích cho auth
    public void saveToken(String token, String refreshToken, boolean isNewUser) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_REFRESH_TOKEN, refreshToken)
                .putBoolean(KEY_IS_NEW_USER, isNewUser)
                .apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public void saveUser(User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        saveString(KEY_USER_JSON, json);
    }

    public User getUser() {
        String json = getString(KEY_USER_JSON, null);
        if (json == null) return null;
        Gson gson = new Gson();
        return gson.fromJson(json, User.class);
    }

    public void logout() {
        prefs.edit().remove(KEY_TOKEN).remove(KEY_REFRESH_TOKEN).remove(KEY_USER_JSON).apply();
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }
    public void saveUserFromResponse(UserResponse userResponse) {
        Gson gson = new Gson();
        saveString("current_user_response", gson.toJson(userResponse));
    }

    public UserResponse getCurrentUserResponse() {
        String json = getString("current_user_response", null);
        if (json == null) return null;
        return new Gson().fromJson(json, UserResponse.class);
    }
}