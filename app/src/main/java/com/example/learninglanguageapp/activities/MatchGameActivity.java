package com.example.learninglanguageapp.activities;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.UIModel.MatchItem;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.utils.HelperFunction;
import com.example.learninglanguageapp.viewmodels.MatchGameViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchGameActivity extends AppCompatActivity {

    private GridLayout gameGrid;
    private ProgressBar progressBar;
    private TextView tvLives;
    private MatchItem selectedFirst = null;   // có thể là ảnh HOẶC từ
    private View selectedFirstOverlay = null;
    private final List<MatchItem> gameItems = new ArrayList<>();
    private int lives = 5;
    private final int totalPairs = 6;
    private int matchedPairs = 0;

    private MatchGameViewModel viewModel;
    private int lessonId;
    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;
    private MediaPlayer completeSound;
    private MediaPlayer gameOverSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_game);

        initViews();
        setupBackButton();
        initSounds();


        lessonId = getIntent().getIntExtra("lessonId", 1);
        // BẰNG DÒNG NÀY (Factory để truyền Context):
        viewModel = new ViewModelProvider(this, new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new MatchGameViewModel(MatchGameActivity.this);
            }
        }).get(MatchGameViewModel.class);

        observeViewModel();
        viewModel.loadWordsForGame(lessonId);
    }

    private void initViews() {
        gameGrid = findViewById(R.id.gameGrid);
        progressBar = findViewById(R.id.progressBar);
        tvLives = findViewById(R.id.tvLives);

        tvLives.setText(String.valueOf(HelperFunction.getInstance().loadUserHearts()));
        progressBar.setProgress(0);
    }

    private void setupBackButton() {
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
    private void initSounds() {
        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);
        completeSound = MediaPlayer.create(this, R.raw.complete);
        gameOverSound = MediaPlayer.create(this, R.raw.game_over);
    }

    private void playSound(MediaPlayer mp) {
        if (mp == null) return;
        // phát lại từ đầu mỗi lần
        if (mp.isPlaying()) {
            mp.seekTo(0);
        }
        mp.start();
    }

    private void observeViewModel() {
        viewModel.getWordsLiveData().observe(this, words -> {
            if (words != null && words.size() >= 6) {
                prepareGame(words);
            } else {
                Toast.makeText(this, "Không đủ từ để chơi!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        viewModel.getLoadingLiveData().observe(this, loading -> {
            progressBar.setVisibility(View.VISIBLE);
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Lỗi: " + error, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    private void prepareGame(List<Word> words) {
        Collections.shuffle(words);
        List<Word> selected = words.subList(0, 6);

        gameItems.clear();
        for (int i = 0; i < selected.size(); i++) {
            Word w = selected.get(i);
            String id = String.valueOf(i + 1);

            // Thẻ ảnh
            gameItems.add(new MatchItem(w.getWord(), w.getImageUrl(), true, id));

            // Thẻ chữ: nối với wordName (tiếng Anh)
            gameItems.add(new MatchItem(w.getWord(), "", false, id));
        }

        Collections.shuffle(gameItems);
        displayCards();
    }

    private void displayCards() {
        gameGrid.removeAllViews();
        for (MatchItem item : gameItems) {
            View cardView = LayoutInflater.from(this).inflate(R.layout.item_match_card, gameGrid, false);

            ImageView imgItem = cardView.findViewById(R.id.imgItem);
            TextView tvWord = cardView.findViewById(R.id.tvWord);
            View overlay = cardView.findViewById(R.id.overlayView);

            if (item.isImage()) {
                imgItem.setVisibility(View.VISIBLE);
                tvWord.setVisibility(View.GONE);
                Glide.with(this)
                        .load(item.getImageResource())
                        .placeholder(R.drawable.placeholder_img)
                        .error(R.drawable.bg_wrong)
                        .into(imgItem);
            } else {
                imgItem.setVisibility(View.GONE);
                tvWord.setVisibility(View.VISIBLE);
                tvWord.setText(item.getWord()); // hiển thị wordName
            }

            cardView.setOnClickListener(v -> handleClick(item, overlay, cardView));
            cardView.setTag(item);
            gameGrid.addView(cardView);
        }
    }

    private void resetSelected() {
        if (selectedFirstOverlay != null) {
            selectedFirstOverlay.setVisibility(View.GONE);
            selectedFirstOverlay.setBackground(null);
            selectedFirstOverlay = null;
        }
        selectedFirst = null;
    }

    private void handleClick(MatchItem item, View overlay, View cardView) {
        if (item.isMatched()) return;

        // 1. Chưa chọn gì → chọn cái đầu tiên
        if (selectedFirst == null) {
            selectedFirst = item;
            selectedFirstOverlay = overlay;
            highlightSelected(overlay);
            return;
        }

        // 2. Bấm lại cái đã chọn → bỏ chọn
        if (selectedFirst == item) {
            resetSelected();
            return;
        }

        // 3. Cấm chọn 2 ảnh hoặc 2 từ cùng lúc
        if (selectedFirst.isImage() == item.isImage()) {
            animateWrong(overlay);
            animateWrong(selectedFirstOverlay);
            new Handler(Looper.getMainLooper()).postDelayed(this::resetSelected, 600);
            return;
        }

        // 4. ĐÚNG CẶP → ẨN CẢ 2 THẺ, GIỮ NGUYÊN VỊ TRÍ CÁC THẺ KHÁC
        if (selectedFirst.getMatchId().equals(item.getMatchId())) {
            item.setMatched(true);
            selectedFirst.setMatched(true);
            matchedPairs++;

            playSound(correctSound);

            animateCorrect(overlay);
            animateCorrect(selectedFirstOverlay);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // Ẩn thẻ thứ 2 (hiện tại)
                cardView.setVisibility(View.INVISIBLE);

                // Ẩn thẻ đầu tiên đã chọn trước đó
                View firstCard = findCardByItem(selectedFirst);
                if (firstCard != null) {
                    firstCard.setVisibility(View.INVISIBLE);
                }

                updateProgress();

                if (matchedPairs == totalPairs) {
                    playSound(completeSound);  // ÂM THANH HOÀN THÀNH
                    Toast.makeText(this, "HOÀN THÀNH! Bạn quá giỏi!", Toast.LENGTH_LONG).show();
                }


                // Reset sau khi đã ẩn xong
                resetSelected();
            }, 700);

        } else {
            // 5. SAI CẶP → trừ mạng, rung đỏ
            lives--;
            HelperFunction.getInstance().saveUserHearts(lives);
            tvLives.setText(String.valueOf(lives));

            playSound(wrongSound);

            animateWrong(overlay);
            animateWrong(selectedFirstOverlay);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                resetSelected();
                if (lives <= 0) {
                    playSound(gameOverSound); // ÂM THANH GAME OVER
                    Toast.makeText(this, "Hết mạng! Game Over!", Toast.LENGTH_LONG).show();
                }
            }, 800);
        }

    }
    private View findCardByItem(MatchItem item) {
        for (int i = 0; i < gameGrid.getChildCount(); i++) {
            View v = gameGrid.getChildAt(i);
            if (v.getTag() == item) {
                return v;
            }
        }
        return null;
    }

    private void highlightSelected(View v) {
        v.setBackgroundResource(R.drawable.bg_selected_matchgame);
        v.setVisibility(View.VISIBLE);
    }


    private void animateCorrect(View v) {
        v.setBackgroundResource(R.drawable.bg_correct);
        v.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(v, "scaleX", 1f, 1.3f, 1f).setDuration(500).start();
        ObjectAnimator.ofFloat(v, "scaleY", 1f, 1.3f, 1f).setDuration(500).start();
        new Handler(Looper.getMainLooper()).postDelayed(() -> v.setVisibility(View.GONE), 600);
    }

    private void animateWrong(View v) {
        v.setBackgroundResource(R.drawable.bg_wrong);
        v.setVisibility(View.VISIBLE);
        ObjectAnimator.ofFloat(v, "translationX", 0, 30, -30, 20, -20, 0).setDuration(600).start();
        new Handler(Looper.getMainLooper()).postDelayed(() -> v.setVisibility(View.GONE), 600);
    }

    private void updateProgress() {
        progressBar.setProgress((matchedPairs * 100) / totalPairs);
    }
}
