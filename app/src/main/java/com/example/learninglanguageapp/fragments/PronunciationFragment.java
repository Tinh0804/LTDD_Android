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
import com.example.learninglanguageapp.models.Response.PronunciationResponse;

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
    private LinearLayout llActionButtons, llRecordingSection;

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
        llActionButtons = v.findViewById(R.id.llActionButtons);
        llRecordingSection = v.findViewById(R.id.llRecordingSection);
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

        // Logic nhấn giữ để ghi âm
        btnMic.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (checkPermission()) startRecording();
                else requestPermission();
            } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                stopRecording();
            }
            return true;
        });

        btnRetry.setOnClickListener(v -> {
            animateButtonPress(v);
            hideResultUI();
        });

        btnNext.setOnClickListener(v -> {
            animateButtonPress(v);
            hideResultUI();
            viewModel.nextSentence();
        });
    }

    private void observeViewModel() {
        viewModel.getSentences().observe(getViewLifecycleOwner(), sentences -> {
            updateSentenceUI();
        });

        viewModel.getCurrentIndex().observe(getViewLifecycleOwner(), index -> {
            updateSentenceUI();
        });

        viewModel.getEvaluationResult().observe(getViewLifecycleOwner(), response -> {
            if (response != null)
                showResultUI(response);
            else
                hideResultUI();
        });

        viewModel.getIsProcessing().observe(getViewLifecycleOwner(), processing -> {
            pbLoading.setVisibility(processing ? View.VISIBLE : View.GONE);
            btnMic.setEnabled(!processing);
            if (processing)
                tvMicStatus.setText("Đang chấm điểm...");
            else if (recorder == null)
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
        }
    }

    private void showResultUI(PronunciationResponse response) {
        cvResult.setVisibility(View.VISIBLE);
        llActionButtons.setVisibility(View.VISIBLE);

        llRecordingSection.setVisibility(View.GONE);

        int overallScore = (int) Math.round(response.getOverall_score());
        int accuracyScore = (int) Math.round(response.getAccuracy_score());

        tvFeedback.setText(response.getFeedback());

        animateScore(tvOverallScore, overallScore, false);
        animateScore(tvAccuracyScore, accuracyScore, true);
    }

    private void hideResultUI() {
        cvResult.setVisibility(View.GONE);
        llActionButtons.setVisibility(View.GONE);
        llRecordingSection.setVisibility(View.VISIBLE);
        tvMicStatus.setText("Nhấn giữ để nói");
    }

    private void animateScore(TextView textView, int targetScore, boolean isPercentage) {
        ValueAnimator animator = ValueAnimator.ofInt(0, targetScore);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            textView.setText(isPercentage ? value + "%" : String.valueOf(value));
        });
        animator.start();
    }

    private void playAudioFromUrl(float speed) {
        if (viewModel.getSentences().getValue() == null) return;
        int index = viewModel.getCurrentIndex().getValue();
        String audioUrl = viewModel.getSentences().getValue().get(index).getAudioFile();

        if (mediaPlayer != null) { mediaPlayer.release(); }

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
        } catch (IOException e) {
            Toast.makeText(getContext(), "Lỗi âm thanh", Toast.LENGTH_SHORT).show();
        }
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
            tvMicStatus.setText("Đang nghe...");
            vPulseRing.setVisibility(View.VISIBLE);
            pulseAnimator.start();
            btnMic.animate().scaleX(1.1f).scaleY(1.1f).setDuration(200).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
            } catch (Exception e) {
                Toast.makeText(getContext(), "Nhấn giữ lâu hơn một chút", Toast.LENGTH_SHORT).show();
            }
            recorder = null;
            pulseAnimator.cancel();
            vPulseRing.setVisibility(View.GONE);
            btnMic.animate().scaleX(1.0f).scaleY(1.0f).setDuration(200).start();
            processAndUpload();
        }
    }

    private void processAndUpload() {
        File file = new File(audioPath);
        if (!file.exists()) return;

        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] bytes = new byte[(int) file.length()];
            fis.read(bytes);
            String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP);
            viewModel.sendEvaluation(base64);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void animateAudioButton(ImageButton button) {
        button.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100)
                .withEndAction(() -> button.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()).start();
    }

    private void animateButtonPress(View button) {
        button.animate().scaleX(0.9f).scaleY(0.9f).setDuration(100)
                .withEndAction(() -> button.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()).start();
    }

    private boolean checkPermission() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO_PERMISSION);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mediaPlayer != null) mediaPlayer.release();
        if (recorder != null) recorder.release();
        if (pulseAnimator != null) pulseAnimator.cancel();
    }
}