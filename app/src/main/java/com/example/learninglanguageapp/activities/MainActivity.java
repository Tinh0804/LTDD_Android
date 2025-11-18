package com.example.learninglanguageapp.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //lấy thông tin user
        SharedPrefsHelper prefs = new SharedPrefsHelper(this);
        UserResponse user = prefs.getCurrentUserResponse();

        if (user != null)
            new AlertDialog.Builder(this).setTitle("Chào mừng!").setMessage("Xin chào " + user.getFullName() + "!").setPositiveButton("OK", null).show();
    }
}
