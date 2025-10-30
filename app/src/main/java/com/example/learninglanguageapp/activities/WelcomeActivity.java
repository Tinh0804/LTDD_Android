package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.ProgressBar;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;


public class WelcomeActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private Button getStartedButton;
    private Handler handler = new Handler(Looper.getMainLooper());

    private int progressStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        progressBar = findViewById(R.id.prg);
        getStartedButton = findViewById(R.id.get_started_button);

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 1;
                handler.post(() -> progressBar.setProgress(progressStatus));
                try {
                    Thread.sleep(30); // 100 * 30ms = 3 giây
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        getStartedButton.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, NameRegisterActivity.class);
            startActivity(intent);
            finish(); // đóng WelcomeActivity
        });
    }
}
