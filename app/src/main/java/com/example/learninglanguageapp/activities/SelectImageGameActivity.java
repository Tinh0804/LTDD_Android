package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.viewmodels.LessonViewModel;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SelectImageGameActivity extends AppCompatActivity {

    private static final String TAG = "SelectImageGame";
    private static final int MAX_HEARTS = 5;
    private static final int MAX_OPTIONS = 4;
    private static final int CORRECT_ANSWER_DELAY = 2000; // 2 seconds

    private LessonViewModel viewModel;
    private MediaPlayer mediaPlayer;

    // UI Components
    private ImageButton btnSettings, btnAudio;
    private TextView tvHearts, tvInstruction, tvWord, tvFeedback;
    private ProgressBar progressBar;
    private View progressIndicator;
    private MaterialCardView[] optionCards = new MaterialCardView[MAX_OPTIONS];
    private ImageView[] optionImages = new ImageView[MAX_OPTIONS];
    private TextView[] optionTexts = new TextView[MAX_OPTIONS];
    private View feedbackContainer;

    // Game Data
    private List<Word> allWords;
    private int currentWordIndex = 0;
    private int hearts = MAX_HEARTS;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private long startTime;

    private int lessonId;
    private int userId;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_game);

        lessonId = getIntent().getIntExtra("LESSON_ID", 1);
        userId = getIntent().getIntExtra("USER_ID", 1);

        startTime = System.currentTimeMillis();

        initViews();
        setupViewModel();
        loadWords();
    }

    private void initViews() {
        btnSettings = findViewById(R.id.btnSettings);
        btnAudio = findViewById(R.id.btnAudio);
        tvHearts = findViewById(R.id.tvHearts);
        tvInstruction = findViewById(R.id.tvInstruction);
        tvWord = findViewById(R.id.tvWord);
        progressBar = findViewById(R.id.progressBar);
        progressIndicator = findViewById(R.id.progressIndicator);
        feedbackContainer = findViewById(R.id.feedbackContainer);
        tvFeedback = findViewById(R.id.tvFeedback);

        // Option cards
        optionCards[0] = findViewById(R.id.optionCard1);
        optionCards[1] = findViewById(R.id.optionCard2);
        optionCards[2] = findViewById(R.id.optionCard3);
        optionCards[3] = findViewById(R.id.optionCard4);

        optionImages[0] = findViewById(R.id.optionImage1);
        optionImages[1] = findViewById(R.id.optionImage2);
        optionImages[2] = findViewById(R.id.optionImage3);
        optionImages[3] = findViewById(R.id.optionImage4);

        optionTexts[0] = findViewById(R.id.optionText1);
        optionTexts[1] = findViewById(R.id.optionText2);
        optionTexts[2] = findViewById(R.id.optionText3);
        optionTexts[3] = findViewById(R.id.optionText4);

        btnSettings.setOnClickListener(v -> showSettingsDialog());
        btnAudio.setOnClickListener(v -> playCurrentWordAudio());
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(LessonViewModel.class);

        viewModel.getWordsLiveData().observe(this, words -> {
            if (words != null && !words.isEmpty()) {
                allWords = new ArrayList<>(words);
                Collections.shuffle(allWords); // Shuffle for random order
                showCurrentQuestion();
            } else {
                Toast.makeText(this, "Không có từ vựng nào để chơi", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getIsLoadingLiveData().observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) {
                disableAllOptions();
            }
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void loadWords() {
        viewModel.loadWords(lessonId, userId);
    }

    private void showCurrentQuestion() {
        if (currentWordIndex >= allWords.size()) {
            // Game completed
            showGameCompleted();
            return;
        }

        isAnswered = false;
        feedbackContainer.setVisibility(View.GONE);

        Word currentWord = allWords.get(currentWordIndex);
        tvWord.setText(currentWord.getWord());

        // Update progress
        float progress = (float) (currentWordIndex + 1) / allWords.size();
        progressIndicator.setLayoutParams(getProgressParams(progress));

        // Generate options
        generateOptions(currentWord);

        // Play audio automatically
        playAudio(currentWord.getAudioFile());

        // Enable all options
        enableAllOptions();
        resetOptionStyles();
    }

    private void generateOptions(Word correctWord) {
        List<Word> options = new ArrayList<>();
        options.add(correctWord);

        // Add random wrong options
        List<Word> otherWords = new ArrayList<>(allWords);
        otherWords.remove(correctWord);
        Collections.shuffle(otherWords);

        int optionsToAdd = Math.min(MAX_OPTIONS - 1, otherWords.size());
        for (int i = 0; i < optionsToAdd; i++) {
            options.add(otherWords.get(i));
        }

        // Shuffle options
        Collections.shuffle(options);

        // Display options
        for (int i = 0; i < MAX_OPTIONS; i++) {
            if (i < options.size()) {
                Word word = options.get(i);
                optionCards[i].setVisibility(View.VISIBLE);

                // Load image
                Glide.with(this)
                        .load(word.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.image_error)
                        .into(optionImages[i]);

                optionTexts[i].setText(word.getTranslation());

                // Set click listener
                final int index = i;
                optionCards[i].setOnClickListener(v -> {
                    if (!isAnswered) {
                        checkAnswer(word, index);
                    }
                });
            } else {
                optionCards[i].setVisibility(View.GONE);
            }
        }
    }
    private void playSfx(int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
    }

    private void checkAnswer(Word selectedWord, int selectedIndex) {
        isAnswered = true;
        disableAllOptions();

        Word correctWord = allWords.get(currentWordIndex);
        boolean isCorrect = selectedWord.getWordId() == correctWord.getWordId();

        if (isCorrect) {
            // Correct answer
            playSfx(R.raw.correct);
            correctAnswers++;
            showCorrectFeedback(selectedIndex);

            // Move to next question after delay
            handler.postDelayed(() -> {
                currentWordIndex++;
                showCurrentQuestion();
            }, CORRECT_ANSWER_DELAY);

        } else {
            // Wrong answer
            playSfx(R.raw.wrong);
            wrongAnswers++;
            hearts--;
            updateHearts();
            showWrongFeedback(selectedIndex, correctWord);

            if (hearts <= 0) {
                // Game over
                handler.postDelayed(this::showGameOver, 1500);
            } else {
                // Allow retry
                handler.postDelayed(() -> {
                    isAnswered = false;
                    enableAllOptions();
                    resetOptionStyles();
                    feedbackContainer.setVisibility(View.GONE);
                }, 1500);
            }
        }
    }

    private void showCorrectFeedback(int correctIndex) {
        // Highlight correct answer
        optionCards[correctIndex].setCardBackgroundColor(getColor(R.color.success_green));
        optionCards[correctIndex].setStrokeColor(getColor(R.color.success_green_dark));
        optionCards[correctIndex].setStrokeWidth(8);

        // Show feedback
        feedbackContainer.setVisibility(View.VISIBLE);
        feedbackContainer.setBackgroundColor(getColor(R.color.success_green));
        tvFeedback.setText("✓ Bạn giỏi lắm! Tiếp tục phát huy");
        tvFeedback.setTextColor(Color.WHITE);
    }

    private void showWrongFeedback(int wrongIndex, Word correctWord) {
        // Highlight wrong answer
        optionCards[wrongIndex].setCardBackgroundColor(getColor(R.color.error_red));
        optionCards[wrongIndex].setStrokeColor(getColor(R.color.error_red_dark));
        optionCards[wrongIndex].setStrokeWidth(8);

        // Highlight correct answer
        for (int i = 0; i < MAX_OPTIONS; i++) {
            if (optionTexts[i].getText().toString().equals(correctWord.getTranslation())) {
                optionCards[i].setCardBackgroundColor(getColor(R.color.success_green_light));
                optionCards[i].setStrokeColor(getColor(R.color.success_green));
                optionCards[i].setStrokeWidth(8);
                break;
            }
        }

        // Show feedback
        feedbackContainer.setVisibility(View.VISIBLE);
        feedbackContainer.setBackgroundColor(getColor(R.color.error_red));
        tvFeedback.setText("✗ Sai đáp án. Vui lòng chọn lại");
        tvFeedback.setTextColor(Color.WHITE);
    }

    private void resetOptionStyles() {
        for (MaterialCardView card : optionCards) {
            card.setCardBackgroundColor(Color.WHITE);
            card.setStrokeColor(getColor(R.color.gray_light));
            card.setStrokeWidth(4);
        }
    }

    private void updateHearts() {
        tvHearts.setText(String.valueOf(hearts));

        // Animate hearts decrease
        tvHearts.animate()
                .scaleX(1.5f)
                .scaleY(1.5f)
                .setDuration(200)
                .withEndAction(() -> {
                    tvHearts.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start();
                })
                .start();
    }

    private void playCurrentWordAudio() {
        if (currentWordIndex < allWords.size()) {
            Word currentWord = allWords.get(currentWordIndex);
            playAudio(currentWord.getAudioFile());
        }
    }

    private void playAudio(String audioUrl) {
        if (audioUrl == null || audioUrl.isEmpty()) return;

        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            } else {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
            }

            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                return true;
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            Log.e(TAG, "Error playing audio: " + e.getMessage());
        }
    }


    private void enableAllOptions() {
        for (CardView card : optionCards) {
            card.setEnabled(true);
            card.setAlpha(1f);
        }
    }

    private void disableAllOptions() {
        for (CardView card : optionCards) {
            card.setEnabled(false);
        }
    }

    private android.widget.LinearLayout.LayoutParams getProgressParams(float progress) {
        android.widget.LinearLayout.LayoutParams params =
                (android.widget.LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
        params.weight = progress;
        return params;
    }

    private void showGameOver() {
        playSfx(R.raw.game_over);
        new AlertDialog.Builder(this)
                .setTitle("Hết tim rồi!")
                .setMessage("Bạn đã hết 5 tim. Hãy thử lại nhé!")
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Chơi lại", (dialog, which) -> {
                    restartGame();
                })
                .show();
    }

    private void showGameCompleted() {
        long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
        playSfx(R.raw.complete);
        String message = String.format(
                "Chúc mừng! Bạn đã hoàn thành!\n\n" +
                        "Tổng số từ: %d\n" +
                        "Đúng: %d\n" +
                        "Sai: %d\n" +
                        "Tim còn lại: %d\n" +
                        "Thời gian: %d giây",
                allWords.size(),
                correctAnswers,
                wrongAnswers,
                hearts,
                timeTaken
        );

        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành!")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Chơi lại", (dialog, which) -> {
                    restartGame();
                })
                .show();
    }

    private void restartGame() {
        currentWordIndex = 0;
        hearts = MAX_HEARTS;
        correctAnswers = 0;
        wrongAnswers = 0;
        startTime = System.currentTimeMillis();
        updateHearts();
        Collections.shuffle(allWords);
        showCurrentQuestion();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Cài đặt")
                .setItems(new String[]{"Về trang chủ", "Chơi lại", "Hủy"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            finish();
                            break;
                        case 1:
                            restartGame();
                            break;
                    }
                })
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát game?")
                .setMessage("Bạn có chắc muốn thoát? Tiến trình sẽ không được lưu.")
                .setPositiveButton("Có", (dialog, which) -> {
                    super.onBackPressed();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}