package com.example.learninglanguageapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.WordAdapter;
import com.example.learninglanguageapp.models.Response.SubmitLessonResponse;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.viewmodels.LessonViewModel;

public class LessonActivity extends AppCompatActivity {

    private LessonViewModel viewModel;
    private ViewPager2 viewPager;
    private WordAdapter adapter;
    private TextView tvCategory, tvPosition;
    private ImageButton btnPrevious, btnNext, btnClose;
    private ProgressBar progressBar;
    private SharedPrefsHelper prefsHelper;
    private int lessonId;
    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        prefsHelper = new SharedPrefsHelper(this);
        lessonId = getIntent().getIntExtra("LESSON_ID", 1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");

        initViews();
        setupViewModel();
        setupViewPager();
        setupListeners();
        loadWords();
    }

    private void initViews() {
        viewPager = findViewById(R.id.viewPager);
        tvCategory = findViewById(R.id.tvCategory);
        tvPosition = findViewById(R.id.tvPosition);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnClose = findViewById(R.id.btnClose);
        progressBar = findViewById(R.id.progressBar);
        tvCategory.setText(categoryName);
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);

        viewModel.getWordsLiveData().observe(this, words -> {
            if (words != null && !words.isEmpty()) {
                adapter.setWordList(words);
                updatePositionText();
                updateNavigationButtons();
            }
        });

        // Lắng nghe kết quả từ Server sau khi submit thành công
        viewModel.getSubmitResultLiveData().observe(this, response -> {
            if (response != null) {
                showCompletionDialog(response);
            }
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            int totalItems = adapter.getItemCount();

            if (currentItem < totalItems - 1) {
                viewPager.setCurrentItem(currentItem + 1, true);
            } else {
                String token = prefsHelper.getToken();
                if (token != null) {
                    viewModel.submitLessonCompletion(lessonId, totalItems, token);
                } else {
                    Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnPrevious.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) viewPager.setCurrentItem(currentItem - 1, true);
        });

        btnClose.setOnClickListener(v -> finish());
    }

    private void updateNavigationButtons() {
        int currentItem = viewPager.getCurrentItem();
        int totalItems = adapter.getItemCount();

        btnPrevious.setEnabled(currentItem > 0);
        btnPrevious.setAlpha(currentItem > 0 ? 1.0f : 0.3f);

        // Nếu là trang cuối, nút Next có thể đổi màu để người dùng biết là nút "Kết thúc"
        if (currentItem == totalItems - 1) {
            btnNext.setColorFilter(getResources().getColor(R.color.purple_500)); // Ví dụ đổi màu
        } else {
            btnNext.clearColorFilter();
        }
    }

    private void showCompletionDialog(SubmitLessonResponse response) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tuyệt vời!")
                .setMessage("Bạn đã hoàn thành: " + categoryName +
                        "\nKinh nghiệm nhận được: +" + response.getLessonExperienceReward() + " XP" +
                        "\nTrạng thái: " + (response.isUnlocked() ? "Đã mở khóa bài tiếp theo!" : "Hoàn thành!"))
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    setResult(RESULT_OK);
                    finish();
                });
        builder.create().show();
    }

    private void setupViewPager() {
        adapter = new WordAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                updatePositionText();
                updateNavigationButtons();
            }
        });
    }

    private void loadWords() { viewModel.loadWords(lessonId); }

    private void updatePositionText() {
        int current = viewPager.getCurrentItem() + 1;
        int total = adapter.getItemCount();
        tvPosition.setText(current + " / " + total);
    }
}