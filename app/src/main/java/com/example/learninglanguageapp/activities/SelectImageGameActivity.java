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
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.utils.GameResultHelper;
import com.example.learninglanguageapp.utils.HelperFunction;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectImageGameActivity extends AppCompatActivity {

    private static final String TAG = "SelectImageGame";
    private static final int MAX_OPTIONS = 4;
    private static final int CORRECT_ANSWER_DELAY = 2000;

    private ExerciseViewModel exerciseViewModel;
    private SharedPrefsHelper sharedPrefsHelper;
    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler(Looper.getMainLooper());

    // UI Components
    private ImageButton btnSettings, btnAudio;
    private TextView tvHearts, tvInstruction, tvWord, tvFeedback;
    private ProgressBar progressBar;
    private View progressIndicator, feedbackContainer;
    private final MaterialCardView[] optionCards = new MaterialCardView[MAX_OPTIONS];
    private final ImageView[] optionImages = new ImageView[MAX_OPTIONS];
    private final TextView[] optionTexts = new TextView[MAX_OPTIONS];

    // Game Data
    private List<Exercise> allExercises;
    private int currentWordIndex = 0;
    private int hearts = 5;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private long startTime;
    private int unitId;
    private boolean isAnswered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_game);

        initData();
        initViews();
        initViewModel();
    }

    private void initData() {
        HelperFunction.init(getApplicationContext());
        sharedPrefsHelper = new SharedPrefsHelper(this);
        unitId = getIntent().getIntExtra("unitId", 1);
        hearts = HelperFunction.getInstance().loadUserHearts();
        startTime = System.currentTimeMillis();
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

        for (int i = 0; i < MAX_OPTIONS; i++) {
            int cardId = getResources().getIdentifier("optionCard" + (i + 1), "id", getPackageName());
            int imgId = getResources().getIdentifier("optionImage" + (i + 1), "id", getPackageName());
            int txtId = getResources().getIdentifier("optionText" + (i + 1), "id", getPackageName());
            optionCards[i] = findViewById(cardId);
            optionImages[i] = findViewById(imgId);
            optionTexts[i] = findViewById(txtId);
        }

        btnSettings.setOnClickListener(v -> showSettingsDialog());
        btnAudio.setOnClickListener(v -> {
            if (allExercises != null && currentWordIndex < allExercises.size()) {
                playAudio(allExercises.get(currentWordIndex).getAudioFile());
            }
        });
        tvHearts.setText(String.valueOf(hearts));
        tvInstruction.setText("Nghe và chọn hình ảnh đúng");
    }

    private void initViewModel() {
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        exerciseViewModel.submitResultLiveData.observe(this, response -> {
            if (response != null)
                GameResultHelper.showResultDialog(this, response, this::finish);
        });

        exerciseViewModel.exercisesLiveData.observe(this, exercises -> {
            if (exercises != null && !exercises.isEmpty()) {
                allExercises = new ArrayList<>(exercises);
                Collections.shuffle(allExercises);
                currentWordIndex = 0;
                updateHearts();
                showCurrentQuestion();
            } else {
                Toast.makeText(this, "Không có bài tập nào", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        exerciseViewModel.loadingLiveData.observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) disableAllOptions();
        });

        exerciseViewModel.errorLiveData.observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                exerciseViewModel.errorLiveData.setValue(null);
            }
        });

        exerciseViewModel.fetchExercisesByType(unitId, "listen");
    }

    private void showCurrentQuestion() {
        if (allExercises == null || currentWordIndex >= allExercises.size()) {
            submitGameResults();
            return;
        }

        isAnswered = false;
        feedbackContainer.setVisibility(View.GONE);
        Exercise exercise = allExercises.get(currentWordIndex);
        tvWord.setText(exercise.getQuestion());

        float progress = (float)(currentWordIndex + 1)/allExercises.size();
        progressIndicator.setLayoutParams(getProgressParams(progress));

        generateOptions(exercise);
        playAudio(exercise.getAudioFile());
        enableAllOptions();
        resetOptionStyles();
    }

    private void generateOptions(Exercise exercise) {
        List<String> options = new ArrayList<>(exercise.getOptions());
        Collections.shuffle(options);

        for (int i = 0; i < MAX_OPTIONS; i++) {
            if (i < options.size()) {
                String optionText = options.get(i);
                optionCards[i].setVisibility(View.VISIBLE);
                optionTexts[i].setText(optionText);

                final int index = i;
                optionCards[i].setOnClickListener(v -> {
                    if (!isAnswered) checkAnswer(optionText, index);
                });
            } else
                optionCards[i].setVisibility(View.GONE);
        }
    }

    private void checkAnswer(String selected, int selectedIndex) {
        isAnswered = true;
        disableAllOptions();
        Exercise exercise = allExercises.get(currentWordIndex);
        boolean isCorrect = selected.equals(exercise.getCorrectAnswer());

        if (isCorrect) {
            playSfx(R.raw.correct);
            correctAnswers++;
            showCorrectFeedback(selectedIndex);
            handler.postDelayed(() -> {
                currentWordIndex++;
                showCurrentQuestion();
            }, CORRECT_ANSWER_DELAY);
        } else {
            playSfx(R.raw.wrong);
            wrongAnswers++;
            hearts--;
            updateHearts();
            showWrongFeedback(selectedIndex, exercise.getCorrectAnswer());

            if (hearts <= 0) handler.postDelayed(this::showGameOver, 1500);
            else handler.postDelayed(() -> {
                isAnswered = false;
                enableAllOptions();
                resetOptionStyles();
                feedbackContainer.setVisibility(View.GONE);
            }, 1500);
        }
    }

    private void submitGameResults() {
        playSfx(R.raw.complete);
        String token = sharedPrefsHelper.getToken();
        int lessonId = allExercises.get(0).getLessonId();
        // Tích hợp: Gửi kết quả lên Server qua ViewModel
        exerciseViewModel.submitExerciseResult(token, lessonId, allExercises.size(), correctAnswers);
    }

    private void showCorrectFeedback(int correctIndex) {
        optionCards[correctIndex].setCardBackgroundColor(getColor(R.color.success_green));
        optionCards[correctIndex].setStrokeColor(getColor(R.color.success_green_dark));
        optionCards[correctIndex].setStrokeWidth(8);
        feedbackContainer.setVisibility(View.VISIBLE);
        feedbackContainer.setBackgroundColor(getColor(R.color.success_green));
        tvFeedback.setText("✓ Bạn giỏi lắm! Tiếp tục phát huy");
        tvFeedback.setTextColor(Color.WHITE);
    }

    private void showWrongFeedback(int wrongIndex, String correctAnswer) {
        optionCards[wrongIndex].setCardBackgroundColor(getColor(R.color.error_red));
        optionCards[wrongIndex].setStrokeColor(getColor(R.color.error_red_dark));
        optionCards[wrongIndex].setStrokeWidth(8);
        for (int i = 0; i < MAX_OPTIONS; i++) {
            if (optionTexts[i].getText().toString().equals(correctAnswer)) {
                optionCards[i].setCardBackgroundColor(getColor(R.color.success_green_light));
                optionCards[i].setStrokeColor(getColor(R.color.success_green));
                optionCards[i].setStrokeWidth(8);
                break;
            }
        }
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
        HelperFunction.getInstance().saveUserHearts(hearts);
        tvHearts.setText(String.valueOf(hearts));
        tvHearts.animate().scaleX(1.5f).scaleY(1.5f).setDuration(200)
                .withEndAction(() -> tvHearts.animate().scaleX(1f).scaleY(1f).setDuration(200).start()).start();
    }

    private void playAudio(String audioUrl) {
        if (audioUrl == null || audioUrl.isEmpty()) return;
        try {
            if (mediaPlayer != null) mediaPlayer.reset();
            else {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA).build());
            }
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.prepareAsync();
        } catch (IOException e) { Log.e(TAG, "Error playing audio: " + e.getMessage()); }
    }

    private void playSfx(int resId) {
        MediaPlayer sfx = MediaPlayer.create(this, resId);
        sfx.setOnCompletionListener(MediaPlayer::release);
        sfx.start();
    }

    private void disableAllOptions() {
        for (MaterialCardView card : optionCards) { card.setEnabled(false); card.setAlpha(0.5f); }
    }

    private void enableAllOptions() {
        for (MaterialCardView card : optionCards) { card.setEnabled(true); card.setAlpha(1f); }
    }

    private android.widget.LinearLayout.LayoutParams getProgressParams(float progress) {
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
        params.weight = progress;
        return params;
    }

    private void showGameOver() {
        playSfx(R.raw.game_over);
        new AlertDialog.Builder(this).setTitle("Hết tim rồi!")
                .setMessage("Bạn đã hết tim. Hãy mua thêm hoặc thử lại sau!")
                .setCancelable(false)
                .setPositiveButton("Đi đến cửa hàng", (d, w) -> startActivity(new Intent(this, ShopActivity.class)))
                .setNeutralButton("Về trang chủ", (d, w) -> finish()).show();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this).setTitle("Cài đặt")
                .setItems(new String[]{"Về trang chủ", "Chơi lại", "Hủy"}, (d, w) -> {
                    if (w == 0) finish(); else if (w == 1) restartGame();
                }).show();
    }

    private void restartGame() {
        currentWordIndex = 0; hearts = HelperFunction.getInstance().loadUserHearts();
        correctAnswers = 0; wrongAnswers = 0; startTime = System.currentTimeMillis();
        updateHearts();
        if (allExercises != null) Collections.shuffle(allExercises);
        showCurrentQuestion();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.hearts = HelperFunction.getInstance().loadUserHearts();
        tvHearts.setText(String.valueOf(this.hearts));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Thoát game?")
                .setMessage("Tiến trình sẽ không được lưu.")
                .setPositiveButton("Có", (d, w) -> super.onBackPressed())
                .setNegativeButton("Không", null).show();
    }
}