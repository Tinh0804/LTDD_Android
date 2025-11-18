package com.example.test;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MatchGameActivity extends AppCompatActivity {

    private GridLayout gameGrid;
    private ProgressBar progressBar;
    private TextView tvLives;

    private List<MatchItem> gameItems;

    private MatchItem selectedImage = null;
    private View selectedImageOverlay = null;

    private int lives = 5;
    private int totalPairs = 0;
    private int matchedPairs = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_game);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        gameGrid = findViewById(R.id.gameGrid);
        progressBar = findViewById(R.id.progressBar);
        tvLives = findViewById(R.id.tvLives);

        setupGame();
    }

    private void setupGame() {
        gameItems = new ArrayList<>();

        gameItems.add(new MatchItem("Tomato", "img_tomato", true, "1"));
        gameItems.add(new MatchItem("Tomato", "", false, "1"));

        gameItems.add(new MatchItem("Dog", "img_dog", true, "2"));
        gameItems.add(new MatchItem("Dog", "", false, "2"));

        gameItems.add(new MatchItem("Travel", "img_travels", true, "3"));
        gameItems.add(new MatchItem("Travel", "", false, "3"));

        gameItems.add(new MatchItem("Music", "img_music", true, "4"));
        gameItems.add(new MatchItem("Music", "", false, "4"));

        gameItems.add(new MatchItem("Cake", "img_cake", true, "5"));
        gameItems.add(new MatchItem("Cake", "", false, "5"));

        gameItems.add(new MatchItem("Leaf", "img_leaft", true, "6"));
        gameItems.add(new MatchItem("Leaf", "", false, "6"));

        totalPairs = gameItems.size() / 2;

        Collections.shuffle(gameItems);
        displayItems();
        updateProgress();
    }

    /** Reset ảnh đã chọn khi chọn sai hoặc chọn ảnh mới */
    private void resetSelectedImage() {
        if (selectedImageOverlay != null) {
            selectedImageOverlay.setVisibility(View.GONE);
            selectedImageOverlay.setBackground(null);
        }
    }

    private void displayItems() {
        gameGrid.removeAllViews();

        for (int i = 0; i < gameItems.size(); i++) {

            MatchItem item = gameItems.get(i);
            View cardView = LayoutInflater.from(this).inflate(R.layout.item_match_card, gameGrid, false);

            CardView card = cardView.findViewById(R.id.cardView);
            ImageView imgItem = cardView.findViewById(R.id.imgItem);
            TextView tvWord = cardView.findViewById(R.id.tvWord);
            View overlay = cardView.findViewById(R.id.overlayView);

            if (item.isMatched()) {
                cardView.setVisibility(View.INVISIBLE);
                gameGrid.addView(cardView);
                continue;
            }

            if (item.isImage()) {
                imgItem.setVisibility(View.VISIBLE);
                tvWord.setVisibility(View.GONE);

                int res = getResources().getIdentifier(item.getImageResource(), "drawable", getPackageName());
                if (res != 0) imgItem.setImageResource(res);

            } else {
                imgItem.setVisibility(View.GONE);
                tvWord.setVisibility(View.VISIBLE);
                tvWord.setText(item.getWord());
            }

            card.setOnClickListener(v -> handleClick(item, overlay, tvWord, cardView));

            gameGrid.addView(cardView);
        }
    }

    private void handleClick(MatchItem item, View overlay, TextView tvWord, View cardView) {

        if (item.isMatched()) return;

        if (item.isImage()) {
            resetSelectedImage();  // reset ảnh cũ trước khi chọn ảnh mới

            selectedImage = item;
            selectedImageOverlay = overlay;
            highlightSelectedImage(overlay);
            return;
        }

        // Nếu click vào chữ nhưng chưa chọn ảnh thì bỏ qua
        if (selectedImage == null) return;

        if (selectedImage.getMatchId().equals(item.getMatchId())) {
            handleCorrectMatch(item, selectedImage, overlay, selectedImageOverlay, cardView);
        } else {
            handleWrongMatch(overlay, tvWord);
        }

        // Sau khi xử lý → reset chọn
        selectedImage = null;
        selectedImageOverlay = null;
    }

    private void highlightSelectedImage(View overlay) {
        overlay.setBackgroundResource(R.drawable.bg_selected_matchgame);
        overlay.setVisibility(View.VISIBLE);
    }

    private void handleCorrectMatch(MatchItem wordItem, MatchItem imgItem,
                                    View wordOverlay, View imageOverlay, View wordCardView) {

        wordItem.setMatched(true);
        imgItem.setMatched(true);
        matchedPairs++;

        showCorrectAnimation(wordOverlay);
        showCorrectAnimation(imageOverlay);

        new Handler().postDelayed(() -> {
            wordCardView.setVisibility(View.INVISIBLE);
            refreshGrid();
        }, 400);

        updateProgress();
    }

    private void refreshGrid() {
        displayItems();
    }

    private void handleWrongMatch(View overlay, TextView tvWord) {

        lives--;
        tvLives.setText(String.valueOf(lives));

        showWrongAnimation(overlay);

        // ⭐ Quan trọng: xóa highlight của ảnh đã chọn
        resetSelectedImage();

        if (lives <= 0) {
            // TODO: game over
        }
    }

    private void showCorrectAnimation(View overlay) {
        overlay.setBackgroundResource(R.drawable.bg_correct);
        overlay.setVisibility(View.VISIBLE);

        ObjectAnimator.ofFloat(overlay, "scaleX", 1f, 1.1f, 1f)
                .setDuration(250).start();

        ObjectAnimator.ofFloat(overlay, "scaleY", 1f, 1.1f, 1f)
                .setDuration(250).start();

        new Handler().postDelayed(() -> overlay.setVisibility(View.GONE), 400);
    }

    private void showWrongAnimation(View overlay) {
        overlay.setBackgroundResource(R.drawable.bg_wrong);
        overlay.setVisibility(View.VISIBLE);

        ObjectAnimator animator = ObjectAnimator.ofFloat(
                overlay, "translationX",
                0, 25, -25, 25, -25, 12, -12, 5, -5, 0
        );

        animator.setDuration(400);
        animator.start();

        new Handler().postDelayed(() -> overlay.setVisibility(View.GONE), 400);
    }

    private void updateProgress() {
        int progress = (matchedPairs * 100) / totalPairs;
        progressBar.setProgress(progress);
    }
}
