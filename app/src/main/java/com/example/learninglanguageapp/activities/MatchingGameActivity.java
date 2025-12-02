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
import com.example.learninglanguageapp.utils.SharedPrefsHelper;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<Integer> selectedCards = new ArrayList<>(); // Các thẻ đang được chọn
    private Map<Integer, String> cardWords = new HashMap<>(); // Từ được gán cho card index
    private Map<String, String> correctPairs = new HashMap<>(); // Ánh xạ cặp đúng (Eng <-> Viet)
    private List<Integer> matchedCards = new ArrayList<>(); // Các thẻ đã ghép thành công
    private List<Exercise> matchingExercises = new ArrayList<>(); // Exercises tải từ API


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game);
        sharedPrefsHelper = new SharedPrefsHelper(this);
        unitId = getIntent().getIntExtra("unitId", 1);
        loadUserHearts();
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

        cards[0] = findViewById(R.id.card1); cards[1] = findViewById(R.id.card2);
        cards[2] = findViewById(R.id.card3); cards[3] = findViewById(R.id.card4);
        cards[4] = findViewById(R.id.card5); cards[5] = findViewById(R.id.card6);
        cards[6] = findViewById(R.id.card7); cards[7] = findViewById(R.id.card8);
        cards[8] = findViewById(R.id.card9); cards[9] = findViewById(R.id.card10);

        textViews[0] = findViewById(R.id.tvWord1); textViews[1] = findViewById(R.id.tvWord2);
        textViews[2] = findViewById(R.id.tvWord3); textViews[3] = findViewById(R.id.tvWord4);
        textViews[4] = findViewById(R.id.tvWord5); textViews[5] = findViewById(R.id.tvWord6);
        textViews[6] = findViewById(R.id.tvWord7); textViews[7] = findViewById(R.id.tvWord8);
        textViews[8] = findViewById(R.id.tvWord9); textViews[9] = findViewById(R.id.tvWord10);

        btnSettings.setOnClickListener(v -> showSettingsDialog());

        // Set click listeners for all cards
        for (int i = 0; i < cards.length; i++) {
            final int index = i;
            cards[i].setOnClickListener(v -> onCardClicked(index));
        }
    }

    private void initViewModel() {
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        exerciseViewModel.loadingLiveData.observe(this, isLoading -> {
        });

        exerciseViewModel.errorLiveData.observe(this, error -> {
            if (error != null)
                Toast.makeText(this, "Lỗi tải dữ liệu: " + error, Toast.LENGTH_LONG).show();
        });

        exerciseViewModel.exercisesLiveData.observe(this, exercises -> {
            if (exercises != null && !exercises.isEmpty()) {
                matchingExercises = filterAndPrepareMatchingExercises(exercises);

                if (correctPairs.size() < 5) {
                    Toast.makeText(this, "Không đủ 5 cặp ghép hợp lệ (Yêu cầu 10 từ).", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }

                setupGame();
            } else if (exercises != null && matchingExercises.isEmpty()) {
                Toast.makeText(this, "Không có bài tập ghép cặp nào.", Toast.LENGTH_LONG).show();
            }
        });

        exerciseViewModel.fetchExercisesByType(unitId, "match_pairs");
    }

    private void loadUserHearts() {
        UserResponse user = sharedPrefsHelper.getCurrentUserResponse();
        if (user != null)
            this.hearts = user.getHearts() > 0 ? user.getHearts() : 5;
        else
            this.hearts = 5;
    }

    private void saveUserHearts(int newHearts) {
        UserResponse userResponse = sharedPrefsHelper.getCurrentUserResponse();
        if (userResponse != null) {
            userResponse.setHearts(newHearts);
            sharedPrefsHelper.saveUserFromResponse(userResponse);
        }
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

        if (!matches.isEmpty())
            tvInstruction.setText(matches.get(0).getQuestion());

        return matches;
    }

    private void setupGame() {

        int pairsCount = Math.min(correctPairs.size(), 5);

        List<String> allWords = new ArrayList<>();
        List<String> engWords = new ArrayList<>(correctPairs.keySet());

        for (int i = 0; i < pairsCount; i++) {
            String eng = engWords.get(i);
            String viet = correctPairs.get(eng);

            allWords.add(eng);
            allWords.add(viet);
        }

        if (allWords.size() != 10) {
            Toast.makeText(this, "Lỗi: Không đủ cặp ghép hợp lệ. Vui lòng thử Unit khác.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Collections.shuffle(allWords);

        cardWords.clear();
        for (int i = 0; i < 10; i++) {
            cardWords.put(i, allWords.get(i));
            textViews[i].setText(allWords.get(i));
            cards[i].setVisibility(View.VISIBLE);
            resetCardStyle(i);
        }

        currentProgress = 0;
        matchedCards.clear();
        updateProgressBar();
    }

    private void onCardClicked(int cardIndex) {
        if (isCheckingAnswer || matchedCards.contains(cardIndex))
            return;

        if (selectedCards.contains(cardIndex)) {
            selectedCards.remove(Integer.valueOf(cardIndex));
            resetCardStyle(cardIndex);
        } else {
            if (selectedCards.size() < 2) {
                selectedCards.add(cardIndex);
                highlightCard(cardIndex, ContextCompat.getColor(this, R.color.primary_green));
            }
        }

        if (selectedCards.size() == 2) {
            checkAnswerAuto();
        }
    }

    private void checkAnswerAuto() {
        isCheckingAnswer = true;
        int card1 = selectedCards.get(0);
        int card2 = selectedCards.get(1);
        String word1 = cardWords.get(card1);
        String word2 = cardWords.get(card2);
        boolean isMatch = checkIfMatch(word1, word2);

        if (isMatch) {
            playSfx(R.raw.correct);

            highlightCard(card1, ContextCompat.getColor(this, R.color.success_green));
            highlightCard(card2, ContextCompat.getColor(this, R.color.success_green));

            matchedCards.add(card1);
            matchedCards.add(card2);

            increaseProgress(100 / 5);

            handler.postDelayed(() -> {
                cards[card1].setAlpha(0.3f);
                cards[card2].setAlpha(0.3f);
                cards[card1].setEnabled(false);
                cards[card2].setEnabled(false);

                selectedCards.clear();
                isCheckingAnswer = false;

                if (matchedCards.size() == 10)
                    handler.postDelayed(this::showGameCompleted, 500);
            }, 600);

        } else {
            playSfx(R.raw.wrong);

            highlightCard(card1, ContextCompat.getColor(this, R.color.error_red));
            highlightCard(card2, ContextCompat.getColor(this, R.color.error_red));
            hearts--;
            saveUserHearts(hearts);
            updateHearts();

            if (hearts <= 0)
                handler.postDelayed(this::showGameOver, 800);
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

    private boolean checkIfMatch(String word1, String word2) {
        return (correctPairs.containsKey(word1) && correctPairs.get(word1).equals(word2)) || (correctPairs.containsKey(word2) && correctPairs.get(word2).equals(word1));
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

    private void updateHearts() {
        tvHearts.setText(String.valueOf(hearts));
        tvHearts.animate().scaleX(1.5f).scaleY(1.5f).setDuration(200).withEndAction(() -> {
            tvHearts.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
        }).start();
    }

    private void updateUI() {
        tvHearts.setText(String.valueOf(hearts));
    }

    private void increaseProgress(int amount) {
        currentProgress += amount;
        if (currentProgress > 100) currentProgress = 100;
        updateProgressBar();
    }

    private void updateProgressBar() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
        params.weight = currentProgress / 100f;
        progressIndicator.setLayoutParams(params);
    }

    private void playSfx(int resId) {
        try {
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }
            mediaPlayer = MediaPlayer.create(this, resId);
            if (mediaPlayer != null)
                mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showSettingsDialog() {
        new AlertDialog.Builder(this).setTitle("Cài đặt").setItems(new String[]{"Về trang chủ", "Chơi lại", "Hủy"}, (dialog, which) -> {
            switch (which) {
                case 0:
                    finish();
                    break;
                case 1:
                    restartGame();
                    break;
            }
        }).show();
    }

    private void showGameOver() {
        playSfx(R.raw.game_over);
        new AlertDialog.Builder(this).setTitle("Hết lượt chơi!").setMessage("Bạn đã hết tim. Hãy thử lại nhé!").setCancelable(false).setPositiveButton("Về trang chủ", (d, w) -> finish()).setNegativeButton("Chơi lại", (d, w) -> restartGame()).show();
    }

    private void showGameCompleted() {
        playSfx(R.raw.complete);
        String message = String.format("Chúc mừng! Bạn đã hoàn thành!\n\n" + "Tim còn lại: %d\n" + "Tiến trình: %d%%", hearts, currentProgress);
        new AlertDialog.Builder(this).setTitle("Hoàn thành!").setMessage(message).setCancelable(false).setPositiveButton("Về trang chủ", (d, w) -> finish()).setNegativeButton("Chơi lại", (d, w) -> restartGame()).show();
    }
    private void restartGame() {
        currentProgress = 0;
        loadUserHearts();
        selectedCards.clear();
        matchedCards.clear();
        isCheckingAnswer = false;
        for (MaterialCardView card : cards) {
            card.setVisibility(View.VISIBLE);
            card.setAlpha(1.0f);
            card.setEnabled(true);
        }
        setupGame();
        updateUI();
        for (int i = 0; i < cards.length; i++)
            resetCardStyle(i);
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
        new AlertDialog.Builder(this).setTitle("Thoát game?").setMessage("Bạn có chắc muốn thoát? Tiến trình sẽ không được lưu.").setPositiveButton("Có", (dialog, which) -> {
                    super.onBackPressed();
                })
                .setNegativeButton("Không", null)
                .show();
    }
}
