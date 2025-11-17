package com.example.learninglanguageapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.adapters.WordAdapter;
import com.example.learninglanguageapp.viewmodels.LessonViewModel;

public class LessonActivity extends AppCompatActivity {

    private LessonViewModel viewModel;
    private ViewPager2 viewPager;
    private WordAdapter adapter;
    private TextView tvCategory, tvPosition;
    private ImageButton btnPrevious, btnNext, btnClose;
    private ProgressBar progressBar;

    private int lessonId;
    private int userId;
    private String categoryName = "Animal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        // Get Intent data
        lessonId = getIntent().getIntExtra("LESSON_ID", 1);
        userId = getIntent().getIntExtra("USER_ID", 1);
        categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        if (categoryName == null) categoryName = "Vocabulary";

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

        // Observe words data
        viewModel.getWordsLiveData().observe(this, words -> {
            if (words != null && !words.isEmpty()) {
                adapter.setWordList(words);
                updatePositionText();
                updateNavigationButtons();
            } else {
                Toast.makeText(this, "Không có từ vựng nào", Toast.LENGTH_SHORT).show();
            }
        });

        // Observe loading state
        viewModel.getIsLoadingLiveData().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            viewPager.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        });

        // Observe errors
        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupViewPager() {
        adapter = new WordAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);

        // Page change callback
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updatePositionText();
                updateNavigationButtons();
            }
        });
    }

    private void setupListeners() {
        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < adapter.getItemCount() - 1) {
                viewPager.setCurrentItem(currentItem + 1, true);
            }
        });

        btnPrevious.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true);
            }
        });

        btnClose.setOnClickListener(v -> finish());
    }

    private void loadWords() {
        viewModel.loadWords(lessonId, userId);
    }

    private void updatePositionText() {
        int current = viewPager.getCurrentItem() + 1;
        int total = adapter.getItemCount();
        tvPosition.setText(current + " / " + total);
    }

    private void updateNavigationButtons() {
        int currentItem = viewPager.getCurrentItem();
        int totalItems = adapter.getItemCount();

        btnPrevious.setEnabled(currentItem > 0);
        btnNext.setEnabled(currentItem < totalItems - 1);

        btnPrevious.setAlpha(currentItem > 0 ? 1.0f : 0.3f);
        btnNext.setAlpha(currentItem < totalItems - 1 ? 1.0f : 0.3f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.releaseMediaPlayer();
        }
    }
}