package com.example.learninglanguageapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.storage.AppDatabase;
import com.example.learninglanguageapp.storage.DAOs.UserDAO;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus, SPLASH_TIME);
    }

    private void checkLoginStatus() {
        new Thread(() -> {
            UserDAO userDao = AppDatabase.getInstance(this).userDAO();
            boolean hasToken = new SharedPrefsHelper(getApplicationContext()).isLoggedIn();
            boolean hasLocalUser = userDao.hasUser();
            boolean isLoggedIn = hasLocalUser && hasToken;
            runOnUiThread(() -> {
                Intent intent;
                if (isLoggedIn) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                } else
                    intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            });
        }).start();
    }
}
