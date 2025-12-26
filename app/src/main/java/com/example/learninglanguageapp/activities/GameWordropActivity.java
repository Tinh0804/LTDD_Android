package com.example.learninglanguageapp.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.utils.GameResultHelper;
import com.example.learninglanguageapp.utils.HelperFunction;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWordropActivity extends AppCompatActivity {

    private ExerciseViewModel exerciseViewModel;
    private HelperFunction helperFunction;
    private SharedPrefsHelper sharedPrefsHelper;

    private int unitId;
    private int hearts;
    private int currentQuestionIndex = 0;
    private int correctAnswersCount = 0;
    private boolean isCheckingAnswer = false;

    private List<Exercise> wordOrderExercises = new ArrayList<>();
    private List<Word> selectedWords = new ArrayList<>();
    private List<Word> availableWords = new ArrayList<>();
    private List<String> correctAnswerWords = new ArrayList<>();

    private TextView tvHearts, tvQuestion;
    private View progressIndicator;
    private FlexboxLayout selectedWordsContainer, availableWordsContainer;
    private Button btnCheck;
    private ImageButton btnSettings;

    private MediaPlayer mediaPlayer;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamewordrop);

        initData();
        initViews();
        initViewModel();
    }

    private void initData() {
        HelperFunction.init(getApplicationContext());
        helperFunction = HelperFunction.getInstance();
        sharedPrefsHelper = new SharedPrefsHelper(this);

        unitId = getIntent().getIntExtra("unitId", 1);
        hearts = helperFunction.loadUserHearts();
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
        updateHeartsUI();
    }

    private void initViewModel() {
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        exerciseViewModel.submitResultLiveData.observe(this, response -> {
            if (response != null)
                GameResultHelper.showResultDialog(this, response, this::finish);
        });

        exerciseViewModel.exercisesLiveData.observe(this, exercises -> {
            if (exercises != null && !exercises.isEmpty())
                filterExercises(exercises);
        });

        exerciseViewModel.errorLiveData.observe(this, error -> {
            if (error != null) Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        });

        exerciseViewModel.fetchExercisesByType(unitId, "word_order");
    }

    private void filterExercises(List<Exercise> exercises) {
        wordOrderExercises.clear();
        for (Exercise ex : exercises)
            if ("word_order".equalsIgnoreCase(ex.getExerciseType()))
                wordOrderExercises.add(ex);

        if (wordOrderExercises.isEmpty()) {
            Toast.makeText(this, "Không có dữ liệu bài tập!", Toast.LENGTH_SHORT).show();
            finish();
        } else
            loadNextQuestion();
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex >= wordOrderExercises.size()) {
            submitGameResults();
            return;
        }
        setupGame(wordOrderExercises.get(currentQuestionIndex));
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

        // Tách câu thành danh sách các từ
        String[] words = correct.trim().replaceAll("[.,?!;]", "").split("\\s+");
        correctAnswerWords.clear();
        availableWords.clear();
        selectedWords.clear();

        for (String w : words) {
            String cleanWord = w.trim();
            if (!cleanWord.isEmpty()) {
                correctAnswerWords.add(cleanWord);
                availableWords.add(new Word(cleanWord));
            }
        }

        // Xáo trộn vị trí các từ để người dùng sắp xếp
        Collections.shuffle(availableWords);
        updateWordContainers();
    }

    private void updateWordContainers() {
        selectedWordsContainer.removeAllViews();
        for (Word w : selectedWords)
            selectedWordsContainer.addView(createWordChip(w, true));

        availableWordsContainer.removeAllViews();
        for (Word w : availableWords)
            if (!w.isSelected)
                availableWordsContainer.addView(createWordChip(w, false));

        btnCheck.setEnabled(!selectedWords.isEmpty());
    }

    private View createWordChip(Word word, boolean isSelected) {
        int layout = isSelected ? R.layout.word_chip_selected : R.layout.word_chip;
        TextView chip = (TextView) getLayoutInflater().inflate(layout, null, false);
        chip.setText(word.text);

        chip.setOnClickListener(v -> {
            if (isCheckingAnswer) return;
            word.isSelected = !isSelected;
            if (word.isSelected) selectedWords.add(word);
            else selectedWords.remove(word);
            updateWordContainers();
        });
        return chip;
    }

    private void checkAnswerManually() {
        if (isCheckingAnswer || selectedWords.isEmpty()) return;
        isCheckingAnswer = true;
        btnCheck.setEnabled(false);

        boolean isCorrect = compareAnswers();

        if (isCorrect)
            handleCorrectAnswer();
        else
            handleWrongAnswer();
    }

    private boolean compareAnswers() {
        if (selectedWords.size() != correctAnswerWords.size()) return false;
        for (int i = 0; i < selectedWords.size(); i++)
            if (!selectedWords.get(i).text.equals(correctAnswerWords.get(i))) return false;
        return true;
    }

    private void handleCorrectAnswer() {
        playSfx(R.raw.correct);
        applyChipFeedback(R.drawable.bg_word_chip_correct, R.anim.scale_up);

        correctAnswersCount++;
        currentQuestionIndex++;
        handler.postDelayed(() -> {
            loadNextQuestion();
            isCheckingAnswer = false;
        }, 1200);
    }

    private void handleWrongAnswer() {
        playSfx(R.raw.wrong);
        applyChipFeedback(R.drawable.bg_word_chip_wrong, R.anim.shake);

        hearts--;
        helperFunction.saveUserHearts(hearts);
        updateHeartsUI();

        if (hearts <= 0)
            handler.postDelayed(this::showGameOver, 1500);
        else {
            handler.postDelayed(() -> {
                resetCurrentQuestion();
                isCheckingAnswer = false;
            }, 1500);
        }
    }

    private void applyChipFeedback(int backgroundRes, int animationRes) {
        for (int i = 0; i < selectedWordsContainer.getChildCount(); i++) {
            View v = selectedWordsContainer.getChildAt(i);
            v.setBackgroundResource(backgroundRes);
            v.startAnimation(AnimationUtils.loadAnimation(this, animationRes));
        }
    }

    private void resetCurrentQuestion() {
        selectedWords.clear();
        for (Word w : availableWords) w.isSelected = false;
        updateWordContainers();
    }

    private void submitGameResults() {
        playSfx(R.raw.complete);

        if (wordOrderExercises.isEmpty()) {
            finish();
            return;
        }

        int lessonId = wordOrderExercises.get(0).getLessonId();
        String token = sharedPrefsHelper.getToken();

        if (token != null) {
            exerciseViewModel.submitExerciseResult(token, lessonId, wordOrderExercises.size(), correctAnswersCount);
        } else {
            finish();
        }
    }

    private void updateProgressBar() {
        if (wordOrderExercises.isEmpty()) return;
        float progress = (float) currentQuestionIndex / wordOrderExercises.size();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
        params.weight = progress;
        progressIndicator.setLayoutParams(params);
    }

    private void updateHeartsUI() {
        tvHearts.setText(String.valueOf(hearts));
    }

    private void showGameOver() {
        playSfx(R.raw.game_over);
        new AlertDialog.Builder(this)
                .setTitle("Hết lượt!")
                .setMessage("Bạn đã hết tim. Hãy thử lại sau.")
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (d, w) -> finish())
                .setNegativeButton("Chơi lại", (d, w) -> restartUnit())
                .show();
    }

    private void restartUnit() {
        currentQuestionIndex = 0;
        correctAnswersCount = 0;
        hearts = helperFunction.loadUserHearts();
        updateHeartsUI();
        loadNextQuestion();
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Menu")
                .setItems(new String[]{"Về trang chủ", "Chơi lại"}, (d, which) -> {
                    if (which == 0) finish(); else restartUnit();
                }).show();
    }

    private void playSfx(int resId) {
        try {
            if (mediaPlayer != null) mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, resId);
            if (mediaPlayer != null) mediaPlayer.start();
        } catch (Exception ignored) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);
    }

    private static class Word {
        String text;
        boolean isSelected = false;
        Word(String text) { this.text = text; }
    }
}