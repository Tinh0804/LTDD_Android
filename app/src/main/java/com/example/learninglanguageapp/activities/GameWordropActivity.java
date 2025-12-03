package com.example.learninglanguageapp.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.utils.HelperFunction;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWordropActivity extends AppCompatActivity {

    private ExerciseViewModel exerciseViewModel;
    private HelperFunction helperFunction;

    private int unitId;
    private int hearts;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;

    private List<Exercise> wordOrderExercises = new ArrayList<>();
    private Exercise currentExercise;

    private ImageButton btnSettings;
    private TextView tvHearts;
    private View progressIndicator;
    private TextView tvQuestion;
    private FlexboxLayout selectedWordsContainer;
    private FlexboxLayout availableWordsContainer;
    private Button btnCheck;

    private List<Word> selectedWords = new ArrayList<>();
    private List<Word> availableWords = new ArrayList<>();
    private List<String> correctAnswerWords = new ArrayList<>();

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isCheckingAnswer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamewordrop);

        HelperFunction.init(getApplicationContext());
        helperFunction = HelperFunction.getInstance();

        unitId = getIntent().getIntExtra("unitId", 1);
        hearts = helperFunction.loadUserHearts();

        initViews();
        initViewModel();
        updateHeartsUI();
    }

    private void initViews() {
        btnSettings = findViewById(R.id.btnSettings);
        tvHearts = findViewById(R.id.tvHearts);
        progressIndicator = findViewById(R.id.progressIndicator);
        tvQuestion = findViewById(R.id.tvQuestion);
        selectedWordsContainer = findViewById(R.id.selectedWordsContainer);
        availableWordsContainer = findViewById(R.id.availableWordsContainer);
        btnCheck = findViewById(R.id.btnCheck);

        btnSettings.setOnClickListener(v -> showSettingsDialog());
        btnCheck.setOnClickListener(v -> checkAnswerManually());
        btnCheck.setEnabled(false);
    }

    private void initViewModel() {
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        exerciseViewModel.errorLiveData.observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
            }
        });

        exerciseViewModel.exercisesLiveData.observe(this, exercises -> {
            if (exercises != null && !exercises.isEmpty()) {
                wordOrderExercises.clear();
                for (Exercise ex : exercises) {
                    if ("word_order".equalsIgnoreCase(ex.getExerciseType())) {
                        wordOrderExercises.add(ex);
                    }
                }
                if (wordOrderExercises.isEmpty()) {
                    Toast.makeText(this, "Không có bài tập word_order!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    loadNextQuestion();
                }
            } else {
                Toast.makeText(this, "Không có dữ liệu!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        exerciseViewModel.fetchExercisesByType(unitId, "word_order");
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex >= wordOrderExercises.size()) {
            showUnitCompleted();
            return;
        }
        currentExercise = wordOrderExercises.get(currentQuestionIndex);
        setupGame(currentExercise);
        updateProgressBar();
    }

    private void setupGame(Exercise exercise) {
        tvQuestion.setText(exercise.getQuestion());

        String correct = exercise.getCorrectAnswer();
        if (correct == null || correct.trim().isEmpty()) {
            currentQuestionIndex++;
            loadNextQuestion();
            return;
        }

        String[] words = correct.trim().replaceAll("[.,?!;]", "").split("\\s+");
        correctAnswerWords.clear();
        availableWords.clear();
        selectedWords.clear();

        for (String w : words) {
            String word = w.trim();
            if (!word.isEmpty()) {
                correctAnswerWords.add(word);
                availableWords.add(new Word(word, false));
            }
        }

        // Thêm từ nhiễu nếu backend có
        if (exercise.getOptions() != null && !exercise.getOptions().isEmpty()) {
            for (String opt : exercise.getOptions()) {
                for (String extra : opt.split(",")) {
                    String e = extra.trim();
                    if (!e.isEmpty() && !correctAnswerWords.contains(e)) {
                        availableWords.add(new Word(e, false));
                    }
                }
            }
        }

        Collections.shuffle(availableWords);
        updateWordContainers();
    }

    private void updateWordContainers() {
        tvHearts.setText(String.valueOf(hearts));

        selectedWordsContainer.removeAllViews();
        for (Word w : selectedWords) selectedWordsContainer.addView(createWordChip(w, true));

        availableWordsContainer.removeAllViews();
        for (Word w : availableWords) {
            if (!w.isSelected) availableWordsContainer.addView(createWordChip(w, false));
        }

        btnCheck.setEnabled(!selectedWords.isEmpty());
    }

    // CHỈ DÙNG 2 FILE BẠN ĐÃ CÓ: word_chip.xml & word_chip_selected.xml
    private View createWordChip(Word word, boolean isSelected) {
        int layoutRes = isSelected ? R.layout.word_chip_selected : R.layout.word_chip;
        TextView chip = (TextView) getLayoutInflater().inflate(layoutRes, null, false);
        chip.setText(word.text);

        chip.setOnClickListener(v -> {
            if (isCheckingAnswer) return;

            if (isSelected) {
                word.isSelected = false;
                selectedWords.remove(word);
            } else {
                word.isSelected = true;
                selectedWords.add(word);
            }
            updateWordContainers();
        });

        return chip;
    }

    private void checkAnswerManually() {
        if (isCheckingAnswer || selectedWords.isEmpty()) return;

        isCheckingAnswer = true;
        btnCheck.setEnabled(false);

        boolean isCorrect = selectedWords.size() == correctAnswerWords.size();
        if (isCorrect) {
            for (int i = 0; i < selectedWords.size(); i++) {
                if (!selectedWords.get(i).text.equals(correctAnswerWords.get(i))) {
                    isCorrect = false;
                    break;
                }
            }
        }

        if (isCorrect) {
            playSfx(R.raw.correct);

            // HIỆU ỨNG ĐÚNG: xanh + rung nhẹ
            for (int i = 0; i < selectedWordsContainer.getChildCount(); i++) {
                View v = selectedWordsContainer.getChildAt(i);
                v.setBackgroundResource(R.drawable.bg_word_chip_correct); // bạn tạo file này 1 dòng thôi
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_up));
            }

            correctAnswersCount++;
            currentQuestionIndex++;

            handler.postDelayed(() -> {
                loadNextQuestion();
                isCheckingAnswer = false;
            }, 1200);

        } else {
            playSfx(R.raw.wrong);
            hearts--;
            helperFunction.saveUserHearts(hearts);
            tvHearts.setText(String.valueOf(hearts));

            // HIỆU ỨNG SAI: viền đỏ + rung
            for (int i = 0; i < selectedWordsContainer.getChildCount(); i++) {
                View v = selectedWordsContainer.getChildAt(i);
                v.setBackgroundResource(R.drawable.bg_word_chip_wrong); // bạn tạo file này
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
            }

            if (hearts <= 0) {
                handler.postDelayed(this::showGameOver, 1500);
            } else {
                handler.postDelayed(() -> {
                    selectedWords.clear();
                    for (Word w : availableWords) w.isSelected = false;
                    isCheckingAnswer = false;
                    updateWordContainers();
                }, 1500);
            }
        }
    }

    private void updateProgressBar() {
        if (wordOrderExercises.isEmpty()) return;
        float progress = (float) correctAnswersCount / wordOrderExercises.size() * 100f;
        android.widget.LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
        params.weight = progress / 100f;
        progressIndicator.setLayoutParams(params);
    }

    private void showUnitCompleted() {
        playSfx(R.raw.complete);
        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành Unit!")
                .setMessage("Tuyệt vời! Bạn đã làm đúng " + correctAnswersCount + "/" + wordOrderExercises.size() + " câu!")
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (d, w) -> finish())
                .setNegativeButton("Chơi lại", (d, w) -> restartUnit())
                .show();
    }

    private void showGameOver() {
        playSfx(R.raw.game_over);
        new AlertDialog.Builder(this)
                .setTitle("Hết tim!")
                .setMessage("Bạn đã làm đúng " + correctAnswersCount + " câu.")
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (d, w) -> finish())
                .setNegativeButton("Chơi lại", (d, w) -> restartUnit())
                .show();
    }

    private void restartUnit() {
        hearts = helperFunction.loadUserHearts();
        currentQuestionIndex = 0;
        correctAnswersCount = 0;
        isCheckingAnswer = false;
        updateHeartsUI();
        updateProgressBar();
        exerciseViewModel.fetchExercisesByType(unitId, "word_order");
    }

    private void updateHeartsUI() { tvHearts.setText(String.valueOf(hearts)); }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Menu")
                .setItems(new String[]{"Về trang chủ", "Chơi lại"}, (d, which) -> {
                    if (which == 0) finish();
                    else restartUnit();
                }).show();
    }

    private void playSfx(int resId) {
        try {
            if (mediaPlayer != null) mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, resId);
            if (mediaPlayer != null) mediaPlayer.start();
        } catch (Exception ignored) {}
    }

    @Override public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Thoát game?")
                .setMessage("Tiến độ sẽ mất.")
                .setPositiveButton("Thoát", (d, w) -> super.onBackPressed())
                .setNegativeButton("Hủy", null).show();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);
    }

    private static class Word {
        String text;
        boolean isSelected;
        Word(String text, boolean isSelected) {
            this.text = text;
            this.isSelected = isSelected;
        }
    }
}