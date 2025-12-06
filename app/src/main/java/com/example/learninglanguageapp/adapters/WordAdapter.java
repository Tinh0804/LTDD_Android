package com.example.learninglanguageapp.adapters;

import static com.example.learninglanguageapp.R.*;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    private Context context;
    private List<Word> wordList;
    private MediaPlayer mediaPlayer;

    public WordAdapter(Context context) {
        this.context = context;
        this.wordList = new ArrayList<>();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_word_card, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = wordList.get(position);
        holder.bind(word);
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public void setWordList(List<Word> wordList) {
        this.wordList = wordList != null ? wordList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<Word> getWordList() {
        return wordList;
    }

    // ViewHolder với Card Flip
    class WordViewHolder extends RecyclerView.ViewHolder {

        // Container
        private View cardFrontContainer, cardBackContainer;

        // Front side views (Mặt trước - Hiển thị chính)
        private TextView tvWord, tvTranslation, tvPronunciation, tvWordType;
        private ImageView ivWord;
        private ImageButton btnAudio, btnFlip;

        // Back side views (Mặt sau - Chi tiết)
        private TextView tvWordBack, tvPronunciationBack, tvWordTypeBack;
        private TextView tvTranslationBack, tvExampleSentence;
        private ImageButton btnFlipBack, btnAudioBack;

        private boolean isShowingFront = true;
        private AnimatorSet flipToBackAnimation;
        private AnimatorSet flipToFrontAnimation;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);

            // Containers
            cardFrontContainer = itemView.findViewById(R.id.cardFrontContainer);
            cardBackContainer = itemView.findViewById(R.id.cardBackContainer);

            // Front side
            tvWord = itemView.findViewById(R.id.tvWord);
            tvTranslation = itemView.findViewById(R.id.tvTranslation);
            tvPronunciation = itemView.findViewById(R.id.tvPronunciation);
            tvWordType = itemView.findViewById(R.id.tvWordType);
            ivWord = itemView.findViewById(R.id.ivWord);
            btnAudio = itemView.findViewById(R.id.btnAudio);
            btnFlip = itemView.findViewById(R.id.btnFlip);

            // Back side
            tvWordBack = itemView.findViewById(R.id.tvWordBack);
            tvPronunciationBack = itemView.findViewById(R.id.tvPronunciationBack);
            tvWordTypeBack = itemView.findViewById(R.id.tvWordTypeBack);
            tvTranslationBack = itemView.findViewById(R.id.tvTranslationBack);
            tvExampleSentence = itemView.findViewById(R.id.tvExampleSentence);
            btnFlipBack = itemView.findViewById(R.id.btnFlipBack);
            btnAudioBack = itemView.findViewById(R.id.btnAudioBack);

            // Load animations
            loadAnimations();
        }

        @SuppressLint("ResourceType")
        private void loadAnimations() {
            // Load flip animations from res/animator
            float scale = context.getResources().getDisplayMetrics().density;
            cardFrontContainer.setCameraDistance(8000 * scale);
            cardBackContainer.setCameraDistance(8000 * scale);

            flipToBackAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.anim.card_flip_out);
            flipToFrontAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.anim.card_flip_in);
        }

        @SuppressLint("ResourceType")
        public void bind(Word word) {
            // Reset to front
            isShowingFront = true;
            cardFrontContainer.setVisibility(View.VISIBLE);
            cardBackContainer.setVisibility(View.GONE);
            cardFrontContainer.setRotationY(0);
            cardBackContainer.setRotationY(180);

            // Bind FRONT data
            tvWord.setText(word.getWord() != null ? word.getWord() : "");
            tvTranslation.setText(word.getTranslation() != null ? word.getTranslation() : "");

            // Pronunciation with format
            if (word.getPronunciation() != null && !word.getPronunciation().isEmpty()) {
                tvPronunciation.setText("/" + word.getPronunciation() + "/");
                tvPronunciation.setVisibility(View.VISIBLE);
            } else {
                tvPronunciation.setVisibility(View.GONE);
            }

            // Word Type with badge style
            if (word.getWordType() != null && !word.getWordType().isEmpty()) {
                tvWordType.setText(word.getWordType());
                tvWordType.setVisibility(View.VISIBLE);
            } else {
                tvWordType.setVisibility(View.GONE);
            }

            // Load image with Glide
            if (word.getImageUrl() != null && !word.getImageUrl().isEmpty()) {
                Glide.with(context)
                        .load(word.getImageUrl())
                        .placeholder(R.drawable.placeholder)
                        .error(R.drawable.image_error)
                        .centerInside()
                        .into(ivWord);
                ivWord.setVisibility(View.VISIBLE);
            } else {
                ivWord.setVisibility(View.GONE);
            }

            // Bind BACK data
            tvWordBack.setText(word.getWord() != null ? word.getWord() : "");
            tvTranslationBack.setText(word.getTranslation() != null ? word.getTranslation() : "");

            if (word.getPronunciation() != null && !word.getPronunciation().isEmpty()) {
                tvPronunciationBack.setText("/" + word.getPronunciation() + "/");
                tvPronunciationBack.setVisibility(View.VISIBLE);
            } else {
                tvPronunciationBack.setVisibility(View.GONE);
            }

            if (word.getWordType() != null && !word.getWordType().isEmpty()) {
                tvWordTypeBack.setText(word.getWordType());
                tvWordTypeBack.setVisibility(View.VISIBLE);
            } else {
                tvWordTypeBack.setVisibility(View.GONE);
            }

            // Example Sentence
            if (word.getExampleSentence() != null && !word.getExampleSentence().isEmpty()) {
                tvExampleSentence.setText(word.getExampleSentence());
                tvExampleSentence.setVisibility(View.VISIBLE);
            } else {
                tvExampleSentence.setText("Không có câu ví dụ");
                tvExampleSentence.setVisibility(View.VISIBLE);
            }

            // Flip buttons
            btnFlip.setOnClickListener(v -> flipCard());
            btnFlipBack.setOnClickListener(v -> flipCard());

            // Audio buttons
            btnAudio.setOnClickListener(v -> playAudio(word.getAudioFile()));
            btnAudioBack.setOnClickListener(v -> playAudio(word.getAudioFile()));
        }

        private void flipCard() {
            if (isShowingFront) {
                // Flip to back
                flipToBackAnimation.setTarget(cardFrontContainer);
                flipToBackAnimation.start();

                cardBackContainer.setVisibility(View.VISIBLE);
                AnimatorSet flipBackIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.anim.card_flip_in);
                flipBackIn.setTarget(cardBackContainer);
                flipBackIn.start();

                cardFrontContainer.postDelayed(() -> {
                    cardFrontContainer.setVisibility(View.GONE);
                }, 200);

            } else {
                // Flip to front
                flipToBackAnimation.setTarget(cardBackContainer);
                flipToBackAnimation.start();

                cardFrontContainer.setVisibility(View.VISIBLE);
                AnimatorSet flipFrontIn = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.anim.card_flip_in);
                flipFrontIn.setTarget(cardFrontContainer);
                flipFrontIn.start();

                cardBackContainer.postDelayed(() -> {
                    cardBackContainer.setVisibility(View.GONE);
                }, 200);
            }

            isShowingFront = !isShowingFront;
        }

        private void playAudio(String audioUrl) {
            if (audioUrl == null || audioUrl.isEmpty()) {
                Toast.makeText(context, "Không có audio", Toast.LENGTH_SHORT).show();
                return;
            }

            // Release previous MediaPlayer
            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;
            }

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );

                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepareAsync();

                mediaPlayer.setOnPreparedListener(MediaPlayer::start);

                mediaPlayer.setOnCompletionListener(mp -> {
                    mp.release();
                    mediaPlayer = null;
                });

                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    Toast.makeText(context, "Lỗi phát audio", Toast.LENGTH_SHORT).show();
                    mp.release();
                    mediaPlayer = null;
                    return true;
                });

            } catch (IOException e) {
                Toast.makeText(context, "Không thể phát audio", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    // Clean up MediaPlayer
    public void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}