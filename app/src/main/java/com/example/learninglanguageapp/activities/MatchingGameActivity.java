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

import com.example.learninglanguageapp.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingGameActivity extends AppCompatActivity {

    private ImageButton btnSettings;
    private TextView tvHearts;
    private View progressIndicator;
    private TextView tvInstruction;
    private Button btnCheck;

    // Cards and TextViews
    private MaterialCardView[] cards = new MaterialCardView[10];
    private TextView[] textViews = new TextView[10];

    // Game data
    private int hearts = 5;
    private int currentProgress = 0;
    private boolean isCheckingAnswer = false;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());

    // Matching logic
    private List<Integer> selectedCards = new ArrayList<>();
    private Map<Integer, String> cardWords = new HashMap<>();
    private Map<String, String> correctPairs = new HashMap<>();
    private List<Integer> matchedCards = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_game);

        initViews();
        setupGame();
        updateUI();
    }

    private void initViews() {
        btnSettings = findViewById(R.id.btnSettings);
        tvHearts = findViewById(R.id.tvHearts);
        progressIndicator = findViewById(R.id.progressIndicator);
        tvInstruction = findViewById(R.id.tvInstruction);
        btnCheck = findViewById(R.id.btnCheck);

        // Initialize all cards
        cards[0] = findViewById(R.id.card1);
        cards[1] = findViewById(R.id.card2);
        cards[2] = findViewById(R.id.card3);
        cards[3] = findViewById(R.id.card4);
        cards[4] = findViewById(R.id.card5);
        cards[5] = findViewById(R.id.card6);
        cards[6] = findViewById(R.id.card7);
        cards[7] = findViewById(R.id.card8);
        cards[8] = findViewById(R.id.card9);
        cards[9] = findViewById(R.id.card10);

        textViews[0] = findViewById(R.id.tvWord1);
        textViews[1] = findViewById(R.id.tvWord2);
        textViews[2] = findViewById(R.id.tvWord3);
        textViews[3] = findViewById(R.id.tvWord4);
        textViews[4] = findViewById(R.id.tvWord5);
        textViews[5] = findViewById(R.id.tvWord6);
        textViews[6] = findViewById(R.id.tvWord7);
        textViews[7] = findViewById(R.id.tvWord8);
        textViews[8] = findViewById(R.id.tvWord9);
        textViews[9] = findViewById(R.id.tvWord10);

        btnSettings.setOnClickListener(v -> showSettingsDialog());
        btnCheck.setOnClickListener(v -> checkAnswer());

        // Set click listeners for all cards
        for (int i = 0; i < cards.length; i++) {
            final int index = i;
            cards[i].setOnClickListener(v -> onCardClicked(index));
        }
    }

    private void setupGame() {
        // Define correct pairs
        correctPairs.put("hello", "Xin chào");
        correctPairs.put("person", "Người");
        correctPairs.put("buy", "Mua");
        correctPairs.put("where", "Ở đâu");
        correctPairs.put("no", "Không");

        // Create list of all words
        List<String> allWords = new ArrayList<>();
        allWords.addAll(correctPairs.keySet()); // English words
        allWords.addAll(correctPairs.values()); // Vietnamese words

        // Shuffle words
        Collections.shuffle(allWords);

        // Assign words to cards
        for (int i = 0; i < 10; i++) {
            cardWords.put(i, allWords.get(i));
            textViews[i].setText(allWords.get(i));
        }

        updateProgressBar();
    }

    private void onCardClicked(int cardIndex) {
        if (isCheckingAnswer || matchedCards.contains(cardIndex)) {
            return; // Ignore clicks during checking or on matched cards
        }

        if (selectedCards.contains(cardIndex)) {
            // Deselect card
            selectedCards.remove(Integer.valueOf(cardIndex));
            resetCardStyle(cardIndex);
        } else {
            if (selectedCards.size() < 2) {
                // Select card
                selectedCards.add(cardIndex);
                highlightCard(cardIndex, ContextCompat.getColor(this, R.color.primary_green));
            }
        }

        updateCheckButton();
    }

    private void checkAnswer() {
        if (selectedCards.size() != 2) {
            return;
        }

        isCheckingAnswer = true;
        btnCheck.setEnabled(false);

        int card1 = selectedCards.get(0);
        int card2 = selectedCards.get(1);

        String word1 = cardWords.get(card1);
        String word2 = cardWords.get(card2);

        // Check if they match
        boolean isMatch = checkIfMatch(word1, word2);

        if (isMatch) {
            // ✅ CORRECT MATCH
            playSfx(R.raw.correct);

            highlightCard(card1, ContextCompat.getColor(this, R.color.success_green));
            highlightCard(card2, ContextCompat.getColor(this, R.color.success_green));

            matchedCards.add(card1);
            matchedCards.add(card2);

            Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();

            // Increase progress
            increaseProgress(20); // 5 pairs x 20% = 100%

            handler.postDelayed(() -> {
                // Hide matched cards
                cards[card1].setVisibility(View.INVISIBLE);
                cards[card2].setVisibility(View.INVISIBLE);

                selectedCards.clear();
                isCheckingAnswer = false;

                // Check if game completed
                if (matchedCards.size() == 10) {
                    handler.postDelayed(this::showGameCompleted, 500);
                } else {
                    updateCheckButton();
                }
            }, 800);

        } else {
            // ❌ WRONG MATCH
            playSfx(R.raw.wrong);

            highlightCard(card1, ContextCompat.getColor(this, R.color.error_red));
            highlightCard(card2, ContextCompat.getColor(this, R.color.error_red));

            hearts--;
            updateHearts();

            Toast.makeText(this, "Sai rồi! Thử lại nhé ❌", Toast.LENGTH_SHORT).show();

            if (hearts <= 0) {
                handler.postDelayed(this::showGameOver, 800);
            } else {
                handler.postDelayed(() -> {
                    resetCardStyle(card1);
                    resetCardStyle(card2);
                    selectedCards.clear();
                    isCheckingAnswer = false;
                    updateCheckButton();
                }, 800);
            }
        }
    }

    private boolean checkIfMatch(String word1, String word2) {
        // Check if word1 is English and word2 is its Vietnamese translation
        if (correctPairs.containsKey(word1) && correctPairs.get(word1).equals(word2)) {
            return true;
        }
        // Check if word2 is English and word1 is its Vietnamese translation
        if (correctPairs.containsKey(word2) && correctPairs.get(word2).equals(word1)) {
            return true;
        }
        return false;
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

    private void updateCheckButton() {
        btnCheck.setEnabled(selectedCards.size() == 2 && !isCheckingAnswer);
    }

    private void updateHearts() {
        tvHearts.setText(String.valueOf(hearts));

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

    private void updateUI() {
        tvHearts.setText(String.valueOf(hearts));
        updateCheckButton();
    }

    private void increaseProgress(int amount) {
        currentProgress += amount;
        if (currentProgress > 100) currentProgress = 100;
        updateProgressBar();
    }

    private void updateProgressBar() {
        LinearLayout.LayoutParams params =
                (LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
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
            if (mediaPlayer != null) {
                mediaPlayer.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void showGameOver() {
        playSfx(R.raw.game_over);

        new AlertDialog.Builder(this)
                .setTitle("Hết lượt chơi!")
                .setMessage("Bạn đã hết 5 tim. Hãy thử lại nhé!")
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (d, w) -> finish())
                .setNegativeButton("Chơi lại", (d, w) -> restartGame())
                .show();
    }

    private void showGameCompleted() {
        playSfx(R.raw.complete);

        String message = String.format(
                "Chúc mừng! Bạn đã hoàn thành!\n\n" +
                        "Tim còn lại: %d\n" +
                        "Tiến trình: %d%%",
                hearts,
                currentProgress
        );

        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành!")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (d, w) -> finish())
                .setNegativeButton("Chơi lại", (d, w) -> restartGame())
                .show();
    }

    private void restartGame() {
        currentProgress = 0;
        hearts = 5;
        selectedCards.clear();
        matchedCards.clear();
        isCheckingAnswer = false;

        // Reset all cards visibility
        for (MaterialCardView card : cards) {
            card.setVisibility(View.VISIBLE);
        }

        // Setup new game
        setupGame();
        updateUI();

        // Reset all card styles
        for (int i = 0; i < cards.length; i++) {
            resetCardStyle(i);
        }
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