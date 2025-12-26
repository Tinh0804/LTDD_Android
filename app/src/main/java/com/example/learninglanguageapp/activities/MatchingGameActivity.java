package com.example.learninglanguageapp.activities;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.models.Response.UserResponse;
import com.example.learninglanguageapp.utils.GameResultHelper;
import com.example.learninglanguageapp.utils.HelperFunction;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingGameActivity extends AppCompatActivity {

    private ExerciseViewModel exerciseViewModel;
    private SharedPrefsHelper sharedPrefsHelper;

    private int unitId;
    private int hearts;
    private int currentProgress = 0;
    private boolean isCheckingAnswer = false;

    private ImageButton btnSettings;
    private TextView tvHearts;
    private View progressIndicator;
    private TextView tvInstruction;
    private Button btnCheck;

    private MaterialCardView[] cards = new MaterialCardView[10];
    private TextView[] textViews = new TextView[10];

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());
    private List<Integer> selectedCards = new ArrayList<>();
    private Map<Integer, String> cardWords = new HashMap<>();
    private Map<String, String> correctPairs = new HashMap<>();
    private List<Integer> matchedCards = new ArrayList<>();
    private List<Exercise> matchingExercises = new ArrayList<>();
    private HelperFunction helperFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game);

        HelperFunction.init(getApplicationContext());
        sharedPrefsHelper = new SharedPrefsHelper(this);
        unitId = getIntent().getIntExtra("unitId", 1);

        helperFunction = HelperFunction.getInstance();
        this.hearts = helperFunction.loadUserHearts();

        initViews();
        initViewModel();
        updateUI();
    }

    private void initViews() {
        btnSettings = findViewById(R.id.btnSettings);
        tvHearts = findViewById(R.id.tvHearts);
        progressIndicator = findViewById(R.id.progressIndicator);
        tvInstruction = findViewById(R.id.tvInstruction);
        btnCheck = findViewById(R.id.btnCheck);
        btnCheck.setVisibility(View.GONE);

        // Bind cards and textviews
        for (int i = 0; i < 10; i++) {
            int cardId = getResources().getIdentifier("card" + (i + 1), "id", getPackageName());
            int tvId = getResources().getIdentifier("tvWord" + (i + 1), "id", getPackageName());
            cards[i] = findViewById(cardId);
            textViews[i] = findViewById(tvId);

            final int index = i;
            cards[i].setOnClickListener(v -> onCardClicked(index));
        }

        btnSettings.setOnClickListener(v -> showSettingsDialog());
    }

    private void initViewModel() {
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        // LẮNG NGHE KẾT QUẢ SUBMIT TỪ SERVER
        exerciseViewModel.submitResultLiveData.observe(this, response -> {
            if (response != null) {
                GameResultHelper.showResultDialog(this, response, this::finish);
            }
        });

        exerciseViewModel.errorLiveData.observe(this, error -> {
            if (error != null)
                Toast.makeText(this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
        });

        exerciseViewModel.exercisesLiveData.observe(this, exercises -> {
            if (exercises != null && !exercises.isEmpty()) {
                matchingExercises = filterAndPrepareMatchingExercises(exercises);
                if (correctPairs.size() < 5) {
                    Toast.makeText(this, "Không đủ cặp ghép.", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                setupGame();
            }
        });

        exerciseViewModel.fetchExercisesByType(unitId, "match_pairs");
    }

    private List<Exercise> filterAndPrepareMatchingExercises(List<Exercise> allExercises) {
        List<Exercise> matches = new ArrayList<>();
        correctPairs.clear();
        for (Exercise exercise : allExercises) {
            if ("match_pairs".equalsIgnoreCase(exercise.getExerciseType()) && exercise.getOptions() != null) {
                matches.add(exercise);
                for (String pair : exercise.getOptions()) {
                    String[] parts = pair.split(",");
                    if (parts.length == 2)
                        correctPairs.put(parts[0].trim(), parts[1].trim());
                }
            }
        }
        if (!matches.isEmpty()) tvInstruction.setText(matches.get(0).getQuestion());
        return matches;
    }

    private void setupGame() {
        int pairsCount = Math.min(correctPairs.size(), 5);
        List<String> allWords = new ArrayList<>();
        List<String> engWords = new ArrayList<>(correctPairs.keySet());

        for (int i = 0; i < pairsCount; i++) {
            String eng = engWords.get(i);
            allWords.add(eng);
            allWords.add(correctPairs.get(eng));
        }

        Collections.shuffle(allWords);
        cardWords.clear();
        for (int i = 0; i < 10; i++) {
            cardWords.put(i, allWords.get(i));
            textViews[i].setText(allWords.get(i));
            cards[i].setVisibility(View.VISIBLE);
            cards[i].setAlpha(1.0f);
            cards[i].setEnabled(true);
            resetCardStyle(i);
        }
        currentProgress = 0;
        matchedCards.clear();
        updateProgressBar();
    }

    private void onCardClicked(int cardIndex) {
        if (isCheckingAnswer || matchedCards.contains(cardIndex)) return;

        if (selectedCards.contains(cardIndex)) {
            selectedCards.remove(Integer.valueOf(cardIndex));
            resetCardStyle(cardIndex);
        } else if (selectedCards.size() < 2) {
            selectedCards.add(cardIndex);
            highlightCard(cardIndex, ContextCompat.getColor(this, R.color.primary_green));
        }

        if (selectedCards.size() == 2) checkAnswerAuto();
    }

    private void checkAnswerAuto() {
        isCheckingAnswer = true;
        int card1 = selectedCards.get(0);
        int card2 = selectedCards.get(1);
        boolean isMatch = checkIfMatch(cardWords.get(card1), cardWords.get(card2));

        if (isMatch) {
            playSfx(R.raw.correct);
            highlightCard(card1, ContextCompat.getColor(this, R.color.success_green));
            highlightCard(card2, ContextCompat.getColor(this, R.color.success_green));

            matchedCards.add(card1);
            matchedCards.add(card2);
            increaseProgress(20);

            handler.postDelayed(() -> {
                cards[card1].setAlpha(0.3f);
                cards[card2].setAlpha(0.3f);
                cards[card1].setEnabled(false);
                cards[card2].setEnabled(false);
                selectedCards.clear();
                isCheckingAnswer = false;

                if (matchedCards.size() == 10) handler.postDelayed(this::showGameCompleted, 500);
            }, 600);
        } else {
            playSfx(R.raw.wrong);
            highlightCard(card1, ContextCompat.getColor(this, R.color.error_red));
            highlightCard(card2, ContextCompat.getColor(this, R.color.error_red));

            hearts--;
            saveUserHearts(hearts);
            updateHeartsUI();

            if (hearts <= 0) handler.postDelayed(this::showGameOver, 800);
            else {
                handler.postDelayed(() -> {
                    resetCardStyle(card1);
                    resetCardStyle(card2);
                    selectedCards.clear();
                    isCheckingAnswer = false;
                }, 800);
            }
        }
    }

    private void showGameCompleted() {
        playSfx(R.raw.complete);

        // Gửi kết quả lên Server
        int lessonId = (matchingExercises != null && !matchingExercises.isEmpty())
                ? matchingExercises.get(0).getLessonId() : 0;
        String token = sharedPrefsHelper.getToken();

        if (token != null) {
            // Submit: 5 cặp đúng, tổng 5 bài tập
            exerciseViewModel.submitExerciseResult(token, lessonId, 5, 5);
        } else {
            finish();
        }
    }

    // --- Helper Methods ---
    private boolean checkIfMatch(String w1, String w2) {
        return (correctPairs.containsKey(w1) && correctPairs.get(w1).equals(w2)) ||
                (correctPairs.containsKey(w2) && correctPairs.get(w2).equals(w1));
    }

    private void highlightCard(int index, int color) {
        cards[index].setCardBackgroundColor(color);
        cards[index].setStrokeColor(color);
        cards[index].setStrokeWidth(8);
        textViews[index].setTextColor(Color.WHITE);
    }

    private void resetCardStyle(int index) {
        cards[index].setCardBackgroundColor(Color.WHITE);
        cards[index].setStrokeColor(ContextCompat.getColor(this, R.color.gray_light));
        cards[index].setStrokeWidth(4);
        textViews[index].setTextColor(ContextCompat.getColor(this, R.color.text_primary));
    }

    private void updateHeartsUI() {
        tvHearts.setText(String.valueOf(hearts));
        tvHearts.animate().scaleX(1.4f).scaleY(1.4f).setDuration(200)
                .withEndAction(() -> tvHearts.animate().scaleX(1f).scaleY(1f).start()).start();
    }

    private void saveUserHearts(int newHearts) {
        UserResponse userResponse = sharedPrefsHelper.getCurrentUserResponse();
        if (userResponse != null) {
            userResponse.setHearts(newHearts);
            sharedPrefsHelper.saveUserFromResponse(userResponse);
        }
    }

    private void updateUI() { tvHearts.setText(String.valueOf(hearts)); }

    private void increaseProgress(int amount) {
        currentProgress += amount;
        updateProgressBar();
    }

    private void updateProgressBar() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
        params.weight = Math.min(currentProgress, 100) / 100f;
        progressIndicator.setLayoutParams(params);
    }

    private void playSfx(int resId) {
        try {
            if (mediaPlayer != null) mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, resId);
            if (mediaPlayer != null) mediaPlayer.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this).setTitle("Cài đặt")
                .setItems(new String[]{"Về trang chủ", "Chơi lại", "Hủy"}, (dialog, which) -> {
                    if (which == 0) finish(); else if (which == 1) restartGame();
                }).show();
    }

    private void showGameOver() {
        playSfx(R.raw.game_over);
        new AlertDialog.Builder(this).setTitle("Hết lượt!")
                .setMessage("Bạn đã hết tim. Hãy thử lại nhé!").setCancelable(false)
                .setPositiveButton("Về trang chủ", (d, w) -> finish())
                .setNegativeButton("Chơi lại", (d, w) -> restartGame()).show();
    }

    private void restartGame() {
        currentProgress = 0;
        this.hearts = helperFunction.loadUserHearts();
        selectedCards.clear();
        matchedCards.clear();
        isCheckingAnswer = false;
        setupGame();
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) mediaPlayer.release();
        handler.removeCallbacksAndMessages(null);
    }
}