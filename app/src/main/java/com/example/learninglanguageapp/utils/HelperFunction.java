package com.example.learninglanguageapp.utils;

import android.content.Context;

import com.example.learninglanguageapp.models.Response.UserResponse;

public class HelperFunction {

    private static HelperFunction INSTANCE;
    private SharedPrefsHelper sharedPrefsHelper;

    private HelperFunction(Context context) {
        sharedPrefsHelper = new SharedPrefsHelper(context.getApplicationContext());
    }

    // GỌI MỘT LẦN DUY NHẤT
    public static synchronized void init(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new HelperFunction(context);
        }
    }

    // DÙNG Ở MỌI NƠI
    public static HelperFunction getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("HelperFunction chưa được init().");
        }
        return INSTANCE;
    }

    public void saveUserHearts(int newHearts) {
        UserResponse user = sharedPrefsHelper.getCurrentUserResponse();
        if (user != null) {
            user.setHearts(newHearts);
            sharedPrefsHelper.saveUserFromResponse(user);
        }
    }

    public int loadUserHearts() {
        UserResponse user = sharedPrefsHelper.getCurrentUserResponse();
        return (user != null && user.getHearts() > 0) ? user.getHearts() : 5;
    }

    public int loadUserDiamond() {
        UserResponse user = sharedPrefsHelper.getCurrentUserResponse();
        return (user != null && user.getDiamond() > 0) ? user.getDiamond() : 0;
    }

    public void saveUserDiamond(int newDiamond) {
        UserResponse user = sharedPrefsHelper.getCurrentUserResponse();
        if (user != null) {
            user.setDiamond(newDiamond);
            sharedPrefsHelper.saveUserFromResponse(user);
        }
    }
}
