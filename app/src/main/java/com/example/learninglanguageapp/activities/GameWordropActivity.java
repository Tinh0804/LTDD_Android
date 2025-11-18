package com.example.learninglanguageapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameWordropActivity extends AppCompatActivity {

    private TextView tvHearts;
    private TextView tvQuestion;
    private FlexboxLayout selectedWordsContainer;
    private FlexboxLayout availableWordsContainer;
    private Button btnCheck;

    private int hearts = 5;
    private int progress = 30;
    private List<Word> selectedWords = new ArrayList<>();
    private List<Word> availableWords = new ArrayList<>();
    private List<String> correctAnswer = Arrays.asList("Nice", "to", "meet", "you");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamewordrop);

        initViews();
        setupAvailableWords();
        setupCheckButton();
        updateUI();
    }

    private void initViews() {
        tvHearts = findViewById(R.id.tvHearts);
        tvQuestion = findViewById(R.id.tvQuestion);
        selectedWordsContainer = findViewById(R.id.selectedWordsContainer);
        availableWordsContainer = findViewById(R.id.availableWordsContainer);
        btnCheck = findViewById(R.id.btnCheck);
    }

    private void setupCheckButton() {
        btnCheck.setOnClickListener(v -> checkAnswer());
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
                // Placeholder khi đã chọn
                View placeholder = LayoutInflater.from(this)
                        .inflate(R.layout.word_chip_placeholder, availableWordsContainer, false);
                availableWordsContainer.addView(placeholder);
            }
        }

        // Check button on/off
        btnCheck.setEnabled(!selectedWords.isEmpty());
    }

    private View createSelectedWordView(Word word) {
        TextView textView = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.word_chip_selected, selectedWordsContainer, false);

        textView.setText(word.getText());

        textView.setOnClickListener(v -> {
            word.setSelected(false);
            selectedWords.remove(word);
            updateUI();
        });

        return textView;
    }

    private View createAvailableWordView(Word word) {
        TextView textView = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.word_chip, availableWordsContainer, false);

        textView.setText(word.getText());

        textView.setOnClickListener(v -> {
            word.setSelected(true);
            selectedWords.add(word);
            updateUI();
        });

        return textView;
    }

    private void checkAnswer() {
        List<String> userAnswer = new ArrayList<>();
        for (Word w : selectedWords) {
            userAnswer.add(w.getText());
        }

        if (userAnswer.equals(correctAnswer)) {
            Toast.makeText(this, "Correct! 🎉", Toast.LENGTH_SHORT).show();

            // Reset
            selectedWords.clear();
            for (Word w : availableWords) w.setSelected(false);

            updateUI();
        } else {
            hearts--;
            tvHearts.setText(String.valueOf(hearts));

            if (hearts <= 0) {
                Toast.makeText(this, "Game Over!", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(this, "Wrong answer ❌", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Word class
    private static class Word {
        private String text;
        private boolean selected;

        public Word(String text, boolean selected) {
            this.text = text;
            this.selected = selected;
        }

        public String getText() { return text; }

        public boolean isSelected() { return selected; }

        public void setSelected(boolean selected) { this.selected = selected; }
    }
}
