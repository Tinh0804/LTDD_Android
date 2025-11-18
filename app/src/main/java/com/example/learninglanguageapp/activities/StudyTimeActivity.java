package com.example.learninglanguageapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.StudyTimeAdapter;
import com.example.learninglanguageapp.fragments.HomeFragment;
import com.example.learninglanguageapp.models.Request.RegisterRequest;
import com.example.learninglanguageapp.models.Response.ApiResponse;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.models.UIModel.StudyTimeOption;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class StudyTimeActivity extends AppCompatActivity {

    private StudyTimeAdapter adapter;

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_time);

        ImageButton btnBack = findViewById(R.id.btnBack);
        Button btnContinue = findViewById(R.id.btnContinue);
        ListView lvOptions = findViewById(R.id.lvOptions);

        btnBack.setOnClickListener(v -> finish());

        List<StudyTimeOption> options = new ArrayList<>();
        options.add(new StudyTimeOption("5 minutes", "Casual (25 words per week)", R.drawable.ic_timer));
        options.add(new StudyTimeOption("10 minutes", "Normal (50 words per week)", R.drawable.ic_timer));
        options.add(new StudyTimeOption("15 minutes", "Serious (75 words per week)", R.drawable.ic_timer));
        options.add(new StudyTimeOption("30 minutes", "Intensive (150 words per week)", R.drawable.ic_timer));
        options.add(new StudyTimeOption("60 minutes", "Super (300 words per week)", R.drawable.ic_timer));

        adapter = new StudyTimeAdapter(this, options);
        lvOptions.setAdapter(adapter);

        btnContinue.setOnClickListener(v -> {
            StudyTimeOption selected = adapter.getSelectedOption();
            if(selected != null){
                // giả sử userId lấy từ SharedPreferences hoặc Intent
                String userId = "123";

                Intent intent = getIntent();
                Bundle bundle = intent.getBundleExtra("data");

                String username = bundle.getString("username");
                String password = bundle.getString("password");
                String fullName = bundle.getString("fullName");
                String phoneNumber = bundle.getString("phoneNumber");
                int nativeLanguageId = bundle.getInt("nativeLanguageId");
                int learnLanguageId = bundle.getInt("learnLanguageId");
                int courseId = bundle.getInt("courseId");



                RegisterRequest request = new RegisterRequest(username, password, fullName, phoneNumber, nativeLanguageId, learnLanguageId,courseId);
                ApiService api = ApiClient.getApiService(this);

                api.register(request).enqueue(new retrofit2.Callback<ApiResponse<UserResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserResponse>> call, retrofit2.Response<ApiResponse<UserResponse>> response) {
                        if (response.isSuccessful() && response.body() != null ) {

                            // Lấy user nếu cần
                            UserResponse user = response.body().getData();

                            // Chuyển sang HomeActivity (Fragment không được start trực tiếp)
                            Intent intent = new Intent(StudyTimeActivity.this, HomeFragment.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(StudyTimeActivity.this, response.body() != null
                                    ? response.body().getMessage()
                                    : "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserResponse>> call, Throwable t) {
                        Toast.makeText(StudyTimeActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            } else {
                Toast.makeText(this, "Please select a study time", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
