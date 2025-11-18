package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.learninglanguageapp.R;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameWordropActivity extends AppCompatActivity {

    private ImageButton btnSettings;
    private TextView tvHearts;
    private View progressIndicator;

    private TextView tvQuestion;
    private FlexboxLayout selectedWordsContainer;
    private FlexboxLayout availableWordsContainer;

    // Game data
    private int hearts = 5;
    private List<Word> selectedWords = new ArrayList<>();
    private List<Word> availableWords = new ArrayList<>();
    private List<String> correctAnswer = Arrays.asList("Nice", "to", "meet", "you");

    private int currentProgress = 0;  // percent 0 → 100
    private boolean isCheckingAnswer = false; // Để tránh click liên tục

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamewordrop);

        initViews();
        setupTopBar();
        setupAvailableWords();
        updateUI();
    }

    private void initViews() {
        btnSettings = findViewById(R.id.btnSettings);
        tvHearts = findViewById(R.id.tvHearts);
        progressIndicator = findViewById(R.id.progressIndicator);

        tvQuestion = findViewById(R.id.tvQuestion);
        selectedWordsContainer = findViewById(R.id.selectedWordsContainer);
        availableWordsContainer = findViewById(R.id.availableWordsContainer);
    }

    private void setupTopBar() {
        btnSettings.setOnClickListener(v -> showSettingsDialog());
        updateProgressBar();
    }

    private void setupAvailableWords() {
        availableWords.add(new Word("meet", false));
        availableWords.add(new Word("name", false));
        availableWords.add(new Word("what's", false));
        availableWords.add(new Word("you", false));
        availableWords.add(new Word("to", false));
        availableWords.add(new Word("where", false));
        availableWords.add(new Word("your", false));
        availableWords.add(new Word("from", false));
        availableWords.add(new Word("Nice", false));
    }

    private void updateUI() {
        // Update hearts
        tvHearts.setText(String.valueOf(hearts));

        // Selected words
        selectedWordsContainer.removeAllViews();
        for (Word word : selectedWords) {
            View wordView = createSelectedWordView(word);
            selectedWordsContainer.addView(wordView);
        }

        // Available words
        availableWordsContainer.removeAllViews();
        for (Word word : availableWords) {
            if (!word.isSelected()) {
                View wordView = createAvailableWordView(word);
                availableWordsContainer.addView(wordView);
            } else {
                // Tạo placeholder để giữ vị trí
                View placeholder = LayoutInflater.from(this)
                        .inflate(R.layout.word_chip_placeholder, availableWordsContainer, false);
                availableWordsContainer.addView(placeholder);
            }
        }
    }

    private View createSelectedWordView(Word word) {
        TextView textView = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.word_chip_selected, selectedWordsContainer, false);

        textView.setText(word.getText());

        // Click để bỏ từ ra khỏi vùng chọn
        textView.setOnClickListener(v -> {
            if (!isCheckingAnswer) {
                word.setSelected(false);
                selectedWords.remove(word);
                updateUI();
            }
        });

        return textView;
    }

    private View createAvailableWordView(Word word) {
        TextView textView = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.word_chip, availableWordsContainer, false);

        textView.setText(word.getText());

        // Click để thêm từ vào vùng chọn VÀ KIỂM TRA NGAY
        textView.setOnClickListener(v -> {
            if (!isCheckingAnswer) {
                word.setSelected(true);
                selectedWords.add(word);
                updateUI();

                // KIỂM TRA NGAY LẬP TỨC
                checkAnswerImmediately(word);
            }
        });

        return textView;
    }

    private void checkAnswerImmediately(Word lastAddedWord) {
        isCheckingAnswer = true;

        int currentIndex = selectedWords.size() - 1;

        // Kiểm tra xem từ vừa chọn có đúng vị trí không
        if (currentIndex < correctAnswer.size() &&
                lastAddedWord.getText().equals(correctAnswer.get(currentIndex))) {

            // ✅ ĐÚNG - Highlight xanh đậm
            playSfx(R.raw.correct);
            highlightLastWord(ContextCompat.getColor(this, R.color.success_green));

            // Kiểm tra xem đã hoàn thành câu chưa
            if (selectedWords.size() == correctAnswer.size()) {
                // HOÀN THÀNH CÂU
                Toast.makeText(this, "Chính xác! 🎉", Toast.LENGTH_SHORT).show();
                increaseProgress(25);

                // Kiểm tra xem đã hoàn thành game chưa
                if (currentProgress >= 100) {
                    handler.postDelayed(this::showGameCompleted, 1000);
                } else {
                    // Reset để chơi câu tiếp theo
                    handler.postDelayed(() -> {
                        selectedWords.clear();
                        for (Word w : availableWords) {
                            w.setSelected(false);
                        }
                        isCheckingAnswer = false;
                        updateUI();
                    }, 1000);
                }
            } else {
                // Vẫn chưa xong câu, tiếp tục chọn từ tiếp theo
                handler.postDelayed(() -> {
                    isCheckingAnswer = false;
                }, 300);
            }

        } else {
            // ❌ SAI - Highlight đỏ đậm và trả từ về vị trí cũ
            playSfx(R.raw.wrong);
            highlightLastWord(ContextCompat.getColor(this, R.color.error_red));

            // Giảm tim
            hearts--;
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

            if (hearts <= 0) {
                // HẾT TIM - GAME OVER
                handler.postDelayed(this::showGameOver, 800);
            } else {
                Toast.makeText(this, "Sai rồi! Thử lại nhé ❌", Toast.LENGTH_SHORT).show();

                // Trả từ về vị trí cũ sau 800ms
                handler.postDelayed(() -> {
                    lastAddedWord.setSelected(false);
                    selectedWords.remove(lastAddedWord);
                    isCheckingAnswer = false;
                    updateUI();
                }, 800);
            }
        }
    }

    private void highlightLastWord(int color) {
        int lastIndex = selectedWordsContainer.getChildCount() - 1;
        if (lastIndex >= 0) {
            View lastWord = selectedWordsContainer.getChildAt(lastIndex);
            lastWord.setBackgroundColor(color);
        }
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
                            goToHome();
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
                .setPositiveButton("Về trang chủ", (d, w) -> goToHome())
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
        selectedWords.clear();
        for (Word w : availableWords) {
            w.setSelected(false);
        }
        isCheckingAnswer = false;

        updateProgressBar();
        updateUI();
    }
    private void goToHome() {
        Intent intent = new Intent(GameWordropActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
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

    // ===================================
    // Inner Word class
    // ===================================
    private static class Word {
        private String text;
        private boolean selected;

        public Word(String text, boolean selected) {
            this.text = text;
            this.selected = selected;
        }

        public String getText() {
            return text;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }
}