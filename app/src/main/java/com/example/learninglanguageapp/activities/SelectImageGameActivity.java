package com.example.learninglanguageapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.models.Exercise;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.viewmodels.ExerciseViewModel;
import com.example.learninglanguageapp.viewmodels.LessonViewModel;
import com.google.android.material.card.MaterialCardView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SelectImageGameActivity extends AppCompatActivity {

    private static final String TAG = "SelectImageGame";
    private static final int MAX_HEARTS = 5;
    private static final int MAX_OPTIONS = 4;
    private static final int CORRECT_ANSWER_DELAY = 2000; // 2 seconds

    private LessonViewModel viewModel;
    private MediaPlayer mediaPlayer;

    // UI Components
    private ImageButton btnSettings, btnAudio;
    private TextView tvHearts, tvInstruction, tvWord, tvFeedback;
    private ProgressBar progressBar;
    private View progressIndicator;
    private MaterialCardView[] optionCards = new MaterialCardView[MAX_OPTIONS];
    private ImageView[] optionImages = new ImageView[MAX_OPTIONS];
    private TextView[] optionTexts = new TextView[MAX_OPTIONS];
    private View feedbackContainer;

    // Game Data
    private List<Word> allWords; // Dữ liệu này có thể đã lỗi thời, nên dùng allExercises
    private int currentWordIndex = 0;
    private int hearts = MAX_HEARTS;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private long startTime;

    // lessonId ở đây thực ra là unitId
    private int unitId;
    private int userId; // Biến này không được sử dụng
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isAnswered = false;

    private ExerciseViewModel exerciseViewModel;
    private List<Exercise> allExercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_image_game);

        // ❗ SỬA LỖI: Gọi initViews() NGAY LẬP TỨC để ánh xạ UI components
        initViews();

        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

        unitId = getIntent().getIntExtra("unitId", 1); // Lấy unitId
        if (unitId == -1) {
            Toast.makeText(this, "Lỗi: Không tìm thấy Unit ID hợp lệ", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        exerciseViewModel.fetchExercisesByType(unitId, "listen");
        startTime = System.currentTimeMillis();

        // Observer dữ liệu bài tập
        exerciseViewModel.exercisesLiveData.observe(this, exercises -> {
            if (exercises != null && !exercises.isEmpty()) {
                allExercises = new ArrayList<Exercise>(exercises);
                Collections.shuffle(allExercises);
                currentWordIndex = 0;
                updateHearts();
                showCurrentQuestion();
//                exerciseViewModel.exercisesLiveData.setValue(null); // Reset LiveData
            } else {
                Toast.makeText(this, "Không có bài tập nào", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        // Observer trạng thái loading (Đảm bảo progressBar đã được ánh xạ)
        exerciseViewModel.loadingLiveData.observe(this, isLoading -> {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            if (isLoading) disableAllOptions();
        });

        // Observer lỗi
        exerciseViewModel.errorLiveData.observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                exerciseViewModel.errorLiveData.setValue(null);
            }
        });
    }

    private void initViews() {
        btnSettings = findViewById(R.id.btnSettings);
        btnAudio = findViewById(R.id.btnAudio);
        tvHearts = findViewById(R.id.tvHearts);
        tvInstruction = findViewById(R.id.tvInstruction);
        tvWord = findViewById(R.id.tvWord);

        // ❗ Đã sửa: Ánh xạ ProgressBar
        progressBar = findViewById(R.id.progressBar);

        progressIndicator = findViewById(R.id.progressIndicator);
        feedbackContainer = findViewById(R.id.feedbackContainer);
        tvFeedback = findViewById(R.id.tvFeedback);

        // Option cards
        optionCards[0] = findViewById(R.id.optionCard1);
        optionCards[1] = findViewById(R.id.optionCard2);
        optionCards[2] = findViewById(R.id.optionCard3);
        optionCards[3] = findViewById(R.id.optionCard4);

        optionImages[0] = findViewById(R.id.optionImage1);
        optionImages[1] = findViewById(R.id.optionImage2);
        optionImages[2] = findViewById(R.id.optionImage3);
        optionImages[3] = findViewById(R.id.optionImage4);

        optionTexts[0] = findViewById(R.id.optionText1);
        optionTexts[1] = findViewById(R.id.optionText2);
        optionTexts[2] = findViewById(R.id.optionText3);
        optionTexts[3] = findViewById(R.id.optionText4);

        btnSettings.setOnClickListener(v -> showSettingsDialog());
        btnAudio.setOnClickListener(v -> {
            if (currentWordIndex < allExercises.size()) {
                playAudio(allExercises.get(currentWordIndex).getAudioFile());
            }
        });

        // Cài đặt mặc định cho các thành phần UI ban đầu
        tvHearts.setText(String.valueOf(MAX_HEARTS));
        tvInstruction.setText("Nghe và chọn hình ảnh đúng"); // Đặt hướng dẫn cố định
    }

    // Phương thức setupViewModel đã bị loại bỏ vì đã tích hợp logic vào onCreate()

    private void showCurrentQuestion() {
        if (allExercises == null || currentWordIndex >= allExercises.size()) {
            showGameCompleted();
            return;
        }

        isAnswered = false;
        feedbackContainer.setVisibility(View.GONE);

        Exercise exercise = allExercises.get(currentWordIndex);

        // LƯU Ý: Đây là game "Select Image", nhưng bạn đang hiển thị văn bản (tvWord).
        // Cần đảm bảo layout và dữ liệu đang hiển thị hình ảnh nếu đó là ý định của bạn.
        // Nếu bạn muốn hiển thị Hình ảnh, hãy dùng ImageView ở đây.
        tvWord.setText(exercise.getQuestion());

        // Update progress
        float progress = (float)(currentWordIndex + 1)/allExercises.size();
        progressIndicator.setLayoutParams(getProgressParams(progress));

        // Tạo options từ Exercise.options
        generateOptions(exercise);

        // Play audio
        playAudio(exercise.getAudioFile());

        enableAllOptions();
        resetOptionStyles();
    }

    private void generateOptions(Exercise exercise) {
        List<String> options = new ArrayList<>(exercise.getOptions());
        Collections.shuffle(options);

        // LƯU Ý QUAN TRỌNG:
        // Bạn đang sử dụng tvWord (Text) làm câu hỏi và optionTexts (Text) làm đáp án,
        // nhưng tên Activity là SelectImageGameActivity (Chọn Hình ảnh).
        // Nếu ý định là hiển thị hình ảnh, bạn cần:
        // 1. Dùng Glide để tải exercise.getQuestion() vào ImageView (nếu câu hỏi là hình ảnh).
        // 2. Tải URL hình ảnh từ options (Giả sử options là URL hình ảnh) vào optionImages.
        // 3. Hiển thị văn bản đáp án đúng/sai ở đâu đó (có lẽ là tvWord hoặc tvInstruction).

        // Hiện tại, code này chỉ xử lý văn bản (text), KHÔNG xử lý hình ảnh.
        // Tôi giữ lại logic văn bản của bạn và giả định các options là văn bản hiển thị

        for (int i = 0; i < MAX_OPTIONS; i++) {
            if (i < options.size()) {
                String optionText = options.get(i);
                optionCards[i].setVisibility(View.VISIBLE);

                // Giả định optionText chứa URL hình ảnh hoặc tên hình ảnh (nhưng bạn đang set vào TextView)
                // Nếu là hình ảnh, bạn cần tải nó vào optionImages[i]
                optionTexts[i].setText(optionText);

                // Nếu bạn muốn hiển thị hình ảnh, hãy dùng:
                // Glide.with(this).load(optionText).placeholder(R.drawable.placeholder).into(optionImages[i]);

                final int index = i;
                optionCards[i].setOnClickListener(v -> {
                    if (!isAnswered) checkAnswer(optionText, index);
                });

            } else {
                optionCards[i].setVisibility(View.GONE);
            }
        }
    }

    private void playSfx(int resId) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, resId);
        mediaPlayer.start();
    }

    private void checkAnswer(String selected, int selectedIndex) {
        isAnswered = true;
        disableAllOptions();

        Exercise exercise = allExercises.get(currentWordIndex);
        boolean isCorrect = selected.equals(exercise.getCorrectAnswer());

        if (isCorrect) {
            playSfx(R.raw.correct);
            correctAnswers++;
            showCorrectFeedback(selectedIndex);

            handler.postDelayed(() -> {
                currentWordIndex++;
                showCurrentQuestion();
            }, CORRECT_ANSWER_DELAY);

        } else {
            playSfx(R.raw.wrong);
            wrongAnswers++;
            hearts--;
            updateHearts();
            showWrongFeedback(selectedIndex, exercise.getCorrectAnswer());

            if (hearts <= 0) handler.postDelayed(this::showGameOver, 1500);
            else handler.postDelayed(() -> {
                isAnswered = false;
                enableAllOptions();
                resetOptionStyles();
                feedbackContainer.setVisibility(View.GONE);
            }, 1500);
        }
    }


    private void showCorrectFeedback(int correctIndex) {
        // Highlight correct answer
        optionCards[correctIndex].setCardBackgroundColor(getColor(R.color.success_green));
        optionCards[correctIndex].setStrokeColor(getColor(R.color.success_green_dark));
        optionCards[correctIndex].setStrokeWidth(8);

        // Show feedback
        feedbackContainer.setVisibility(View.VISIBLE);
        feedbackContainer.setBackgroundColor(getColor(R.color.success_green));
        tvFeedback.setText("✓ Bạn giỏi lắm! Tiếp tục phát huy");
        tvFeedback.setTextColor(Color.WHITE);
    }

    private void showWrongFeedback(int wrongIndex, String correctAnswer) {
        optionCards[wrongIndex].setCardBackgroundColor(getColor(R.color.error_red));
        optionCards[wrongIndex].setStrokeColor(getColor(R.color.error_red_dark));
        optionCards[wrongIndex].setStrokeWidth(8);

        for (int i = 0; i < MAX_OPTIONS; i++) {
            if (optionTexts[i].getText().toString().equals(correctAnswer)) {
                optionCards[i].setCardBackgroundColor(getColor(R.color.success_green_light));
                optionCards[i].setStrokeColor(getColor(R.color.success_green));
                optionCards[i].setStrokeWidth(8);
                break;
            }
        }

        feedbackContainer.setVisibility(View.VISIBLE);
        feedbackContainer.setBackgroundColor(getColor(R.color.error_red));
        tvFeedback.setText("✗ Sai đáp án. Vui lòng chọn lại");
        tvFeedback.setTextColor(Color.WHITE);
    }


    private void resetOptionStyles() {
        for (MaterialCardView card : optionCards) {
            card.setCardBackgroundColor(Color.WHITE);
            card.setStrokeColor(getColor(R.color.gray_light));
            card.setStrokeWidth(4);
        }
    }

    private void updateHearts() {
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
    }

    // Phương thức này đã bị loại bỏ vì đã tích hợp logic vào initViews()
    /*
    private void playCurrentWordAudio() {
        if (currentWordIndex < allWords.size()) {
            Word currentWord = allWords.get(currentWordIndex);
            playAudio(currentWord.getAudioFile());
        }
    }
    */

    private void playAudio(String audioUrl) {
        if (audioUrl == null || audioUrl.isEmpty()) return;

        try {
            if (mediaPlayer != null) {
                mediaPlayer.reset();
            } else {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
            }

            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Log.e(TAG, "MediaPlayer error: what=" + what + ", extra=" + extra);
                return true;
            });
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            Log.e(TAG, "Error playing audio: " + e.getMessage());
        }
    }


    private void enableAllOptions() {
        for (CardView card : optionCards) {
            card.setEnabled(true);
            card.setAlpha(1f);
        }
    }

    private void disableAllOptions() {
        for (CardView card : optionCards) {
            card.setEnabled(false);
            card.setAlpha(0.5f); // Thêm hiệu ứng mờ khi bị disable
        }
    }

    private android.widget.LinearLayout.LayoutParams getProgressParams(float progress) {
        android.widget.LinearLayout.LayoutParams params =
                (android.widget.LinearLayout.LayoutParams) progressIndicator.getLayoutParams();
        params.weight = progress;
        return params;
    }

    private void showGameOver() {
        playSfx(R.raw.game_over);
        new AlertDialog.Builder(this)
                .setTitle("Hết tim rồi!")
                .setMessage("Bạn đã hết 5 tim. Hãy thử lại nhé!")
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Chơi lại", (dialog, which) -> {
                    restartGame();
                })
                .show();
    }

    private void showGameCompleted() {
        // LƯU Ý: allWords đã bị loại bỏ, hãy dùng allExercises.size()
        long timeTaken = (System.currentTimeMillis() - startTime) / 1000;
        playSfx(R.raw.complete);
        String message = String.format(
                "Chúc mừng! Bạn đã hoàn thành!\n\n" +
                        "Tổng số câu: %d\n" +
                        "Đúng: %d\n" +
                        "Sai: %d\n" +
                        "Tim còn lại: %d\n" +
                        "Thời gian: %d giây",
                allExercises.size(),
                correctAnswers,
                wrongAnswers,
                hearts,
                timeTaken
        );

        new AlertDialog.Builder(this)
                .setTitle("Hoàn thành!")
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Về trang chủ", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("Chơi lại", (dialog, which) -> {
                    restartGame();
                })
                .show();
    }

    private void restartGame() {
        currentWordIndex = 0;
        hearts = MAX_HEARTS;
        correctAnswers = 0;
        wrongAnswers = 0;
        startTime = System.currentTimeMillis();
        updateHearts();
        // Cần xáo trộn lại list bài tập
        if (allExercises != null) {
            Collections.shuffle(allExercises);
        }
        showCurrentQuestion();
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