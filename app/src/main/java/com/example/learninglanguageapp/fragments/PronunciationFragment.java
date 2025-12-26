package com.example.learninglanguageapp.fragments;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.learninglanguageapp.R;
import com.example.learninglanguageapp.models.Sentence;
import com.example.learninglanguageapp.viewmodels.PronunciationViewModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PronunciationFragment extends Fragment {

    private PronunciationViewModel viewModel;
    private TextView tvSentence, tvProgress, tvOverallScore, tvAccuracyScore, tvFeedback, tvMicStatus;
    private ImageButton btnPlayNormal, btnPlaySlow, btnMic;
    private Button btnNext, btnRetry;
    private ProgressBar pbLoading;
    private View cvResult, vPulseRing;
    private LinearLayout llProgressDots, llActionButtons;

    private MediaRecorder recorder;
    private MediaPlayer mediaPlayer;
    private String audioPath;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private ObjectAnimator pulseAnimator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pronunciation, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        viewModel = new ViewModelProvider(this).get(PronunciationViewModel.class);
        int lessonId = getArguments() != null ? getArguments().getInt("lessonId", -1) : -1;

        audioPath = requireContext().getExternalCacheDir().getAbsolutePath() + "/record.m4a";

        setupPulseAnimation();
        observeViewModel();
        setupClickListeners();

        if (lessonId != -1) viewModel.fetchSentences(lessonId);
    }

    private void initViews(View v) {
        tvSentence = v.findViewById(R.id.tvSentence);
        tvProgress = v.findViewById(R.id.tvProgress);
        tvOverallScore = v.findViewById(R.id.tvOverallScore);
        tvAccuracyScore = v.findViewById(R.id.tvAccuracyScore);
        tvFeedback = v.findViewById(R.id.tvFeedback);
        tvMicStatus = v.findViewById(R.id.tvMicStatus);
        btnPlayNormal = v.findViewById(R.id.btnPlayNormal);
        btnPlaySlow = v.findViewById(R.id.btnPlaySlow);
        btnMic = v.findViewById(R.id.btnMic);
        btnNext = v.findViewById(R.id.btnNext);
        btnRetry = v.findViewById(R.id.btnRetry);
        pbLoading = v.findViewById(R.id.pbLoading);
        cvResult = v.findViewById(R.id.cvResult);
        vPulseRing = v.findViewById(R.id.vPulseRing);
        llProgressDots = v.findViewById(R.id.llProgressDots);
        llActionButtons = v.findViewById(R.id.llActionButtons);
    }

    private void setupPulseAnimation() {
        pulseAnimator = ObjectAnimator.ofFloat(vPulseRing, "alpha", 0.3f, 0.8f);
        pulseAnimator.setDuration(1000);
        pulseAnimator.setRepeatCount(ValueAnimator.INFINITE);
        pulseAnimator.setRepeatMode(ValueAnimator.REVERSE);
        pulseAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupClickListeners() {
        btnPlayNormal.setOnClickListener(v -> playAudioFromUrl(1.0f));
        btnPlaySlow.setOnClickListener(v -> playAudioFromUrl(0.7f));

        btnMic.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                if (checkPermission()) startRecording();
                else requestPermission();
            else if (event.getAction() == MotionEvent.ACTION_UP)
                stopRecording();
            return true;
        });

        btnNext.setOnClickListener(v -> {
            animateButtonPress(v);
            viewModel.nextSentence();
        });

        btnRetry.setOnClickListener(v -> {
            animateButtonPress(v);
        });
    }

    private void observeViewModel() {
        viewModel.getSentences().observe(getViewLifecycleOwner(), sentences -> {
            updateSentenceUI();
            createProgressDots();
        });

        viewModel.getCurrentIndex().observe(getViewLifecycleOwner(), index -> {
            updateSentenceUI();
            updateProgressDots();
        });

        viewModel.getEvaluationResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null)
                showResultWithAnimation(response);
            else
                hideResult();
        });

        viewModel.getIsProcessing().observe(getViewLifecycleOwner(), processing -> {
            pbLoading.setVisibility(processing ? View.VISIBLE : View.GONE);
            btnMic.setEnabled(!processing);
            if (processing)
                tvMicStatus.setText("Đang xử lý...");
            else if (!isRecording())
                tvMicStatus.setText("Nhấn giữ để nói");
        });
    }

    private void updateSentenceUI() {
        if (viewModel.getSentences().getValue() != null && !viewModel.getSentences().getValue().isEmpty()) {
            int index = viewModel.getCurrentIndex().getValue();
            int total = viewModel.getSentences().getValue().size();
            Sentence current = viewModel.getSentences().getValue().get(index);

            tvSentence.setText(current.getSentenceText());
            tvProgress.setText("Câu " + (index + 1) + "/" + total);

            // Update progress bar
            int progress = (int) (((index + 1) / (float) total) * 100);
        }
    }

    private void createProgressDots() {
        llProgressDots.removeAllViews();
        if (viewModel.getSentences().getValue() == null) return;

        int total = viewModel.getSentences().getValue().size();
        int dotWidth = 8;
        int dotMargin = 4;

        for (int i = 0; i < total; i++) {
            View dot = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    0,
                    dotWidth,
                    1.0f
            );
            params.setMargins(dotMargin, 0, dotMargin, 0);
            dot.setLayoutParams(params);
            llProgressDots.addView(dot);
        }
        updateProgressDots();
    }

    private void updateProgressDots() {
        if (viewModel.getCurrentIndex().getValue() == null) return;
        int currentIndex = viewModel.getCurrentIndex().getValue();

        for (int i = 0; i < llProgressDots.getChildCount(); i++) {
            View dot = llProgressDots.getChildAt(i);

        }
    }


    private void playAudioFromUrl(float speed) {
        if (viewModel.getSentences().getValue() == null) return;

        int index = viewModel.getCurrentIndex().getValue();
        String audioUrl = viewModel.getSentences().getValue().get(index).getAudioFile();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioUrl);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(mp -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(speed));
                }
                mp.start();
                animateAudioButton(speed == 1.0f ? btnPlayNormal : btnPlaySlow);
            });

            mediaPlayer.setOnCompletionListener(mp -> {
                mp.release();
                mediaPlayer = null;
            });

            mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                Toast.makeText(getContext(), "Không thể phát âm thanh", Toast.LENGTH_SHORT).show();
                return true;
            });

        } catch (IOException e) {
            Toast.makeText(getContext(), "Lỗi khi tải audio", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void animateAudioButton(ImageButton button) {
        button.animate()
                .scaleX(1.2f)
                .scaleY(1.2f)
                .setDuration(100)
                .withEndAction(() ->
                        button.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                )
                .start();
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.setOutputFile(audioPath);

        try {
            recorder.prepare();
            recorder.start();
            tvMicStatus.setText("Đang ghi âm...");

            // Start pulse animation
            vPulseRing.setVisibility(View.VISIBLE);
            pulseAnimator.start();

            // Animate mic button
            btnMic.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start();

        } catch (IOException e) {
            Toast.makeText(getContext(), "Lỗi khi ghi âm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
            } catch (RuntimeException e) {
                // Handle very short press
                Toast.makeText(getContext(), "Ghi âm quá ngắn", Toast.LENGTH_SHORT).show();
            }
            recorder = null;

            // Stop pulse animation
            pulseAnimator.cancel();
            vPulseRing.setVisibility(View.GONE);

            // Reset mic button
            btnMic.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
            tvMicStatus.setText("Nhấn giữ để nói");
            processAndUpload();
        }
    }

    private boolean isRecording() {
        return recorder != null;
    }

    private void processAndUpload() {
        File file = new File(audioPath);
        if (!file.exists()) {
            Toast.makeText(getContext(), "Không tìm thấy file ghi âm", Toast.LENGTH_SHORT).show();
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
            viewModel.sendEvaluation(base64);
        } catch (IOException e) {
            Toast.makeText(getContext(), "Lỗi khi đọc file", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void showResultWithAnimation(Object response) {
        cvResult.setAlpha(0f);
        cvResult.setVisibility(View.VISIBLE);
        cvResult.animate()
                .alpha(1f)
                .setDuration(400)
                .start();

        // Assuming response has getOverall_score(), getAccuracy_score(), getFeedback()
        try {
            int overallScore = (int) response.getClass().getMethod("getOverall_score").invoke(response);
            int accuracyScore = (int) ((double) response.getClass().getMethod("getAccuracy_score").invoke(response));
            String feedback = (String) response.getClass().getMethod("getFeedback").invoke(response);

            tvOverallScore.setText(String.valueOf(overallScore));
            tvAccuracyScore.setText(accuracyScore + "%");
            tvFeedback.setText(feedback);

            animateScore(tvOverallScore, overallScore);
            animateScore(tvAccuracyScore, accuracyScore);

        } catch (Exception e) {
            e.printStackTrace();
        }

        llActionButtons.setVisibility(View.VISIBLE);
    }

    private void animateScore(TextView textView, int targetScore) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetScore);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            if (textView == tvAccuracyScore) {
                textView.setText(value + "%");
            } else {
                textView.setText(String.valueOf(value));
            }
        });
        animator.start();
    }

    private void hideResult() {
        cvResult.setVisibility(View.GONE);
        llActionButtons.setVisibility(View.GONE);
    }


    private void animateButtonPress(View button) {
        button.animate()
                .scaleX(0.95f)
                .scaleY(0.95f)
                .setDuration(100)
                .withEndAction(() ->
                        button.animate()
                                .scaleX(1.0f)
                                .scaleY(1.0f)
                                .setDuration(100)
                                .start()
                )
                .start();
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Cleanup
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
        if (pulseAnimator != null) {
            pulseAnimator.cancel();
        }
    }
}