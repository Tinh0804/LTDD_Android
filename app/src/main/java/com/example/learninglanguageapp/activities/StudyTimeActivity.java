package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learninglanguageapp.R;

public class StudyTimeActivity extends AppCompatActivity {

    private LinearLayout option5min, option10min, option15min, option30min, option60min;
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_time);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);

        // Options
        option5min = findViewById(R.id.option5min);
        option10min = findViewById(R.id.option10min);
        option15min = findViewById(R.id.option15min);
        option30min = findViewById(R.id.option30min);
        option60min = findViewById(R.id.option60min);

        btnBack.setOnClickListener(v -> finish());

        // Click listener cho từng option
        option5min.setOnClickListener(v -> selectOption("5 minutes"));
        option10min.setOnClickListener(v -> selectOption("10 minutes"));
        option15min.setOnClickListener(v -> selectOption("15 minutes"));
        option30min.setOnClickListener(v -> selectOption("30 minutes"));
        option60min.setOnClickListener(v -> selectOption("60 minutes"));
        btnContinue.setOnClickListener(v -> {
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select a study time", Toast.LENGTH_SHORT).show();
                return;
            }

            Bundle bundle = getIntent().getBundleExtra("data");
            if (bundle == null) bundle = new Bundle();

            bundle.putString("study_time", selectedTime); // ✅ lưu thời gian học

            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("data", bundle);
            startActivity(intent);
        });

    }

    // Hàm đánh dấu option được chọn
    private void selectOption(String time) {
        selectedTime = time;

        // Reset tất cả background về bình thường
        option5min.setBackgroundResource(R.drawable.option_background);
        option10min.setBackgroundResource(R.drawable.option_background);
        option15min.setBackgroundResource(R.drawable.option_background);
        option30min.setBackgroundResource(R.drawable.option_background);
        option60min.setBackgroundResource(R.drawable.option_background);

        // Highlight option được chọn
        switch (time) {
            case "5 minutes": option5min.setBackgroundResource(R.drawable.option_background_selected); break;
            case "10 minutes": option10min.setBackgroundResource(R.drawable.option_background_selected); break;
            case "15 minutes": option15min.setBackgroundResource(R.drawable.option_background_selected); break;
            case "30 minutes": option30min.setBackgroundResource(R.drawable.option_background_selected); break;
            case "60 minutes": option60min.setBackgroundResource(R.drawable.option_background_selected); break;
        }
    }
}
