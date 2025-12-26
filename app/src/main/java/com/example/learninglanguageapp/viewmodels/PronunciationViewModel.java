package com.example.learninglanguageapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.learninglanguageapp.models.Response.PronunciationResponse;
import com.example.learninglanguageapp.models.Sentence;
import com.example.learninglanguageapp.repository.PronunciationRepository;
import java.util.List;

public class PronunciationViewModel extends AndroidViewModel {
    private final PronunciationRepository repository;
    private final MutableLiveData<List<Sentence>> sentences = new MutableLiveData<>();
    private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
    private final MutableLiveData<PronunciationResponse> evaluationResult = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isProcessing = new MutableLiveData<>(false);

    public PronunciationViewModel(@NonNull Application application) {
        super(application);
        repository = new PronunciationRepository(application);
    }

    public LiveData<List<Sentence>> getSentences() { return sentences; }
    public LiveData<Integer> getCurrentIndex() { return currentIndex; }
    public LiveData<PronunciationResponse> getEvaluationResult() { return evaluationResult; }
    public LiveData<Boolean> getIsProcessing() { return isProcessing; }

    public void fetchSentences(int lessonId) {
        repository.getSentences(lessonId, sentences);
    }

    public void sendEvaluation(String base64) {
        List<Sentence> list = sentences.getValue();
        Integer index = currentIndex.getValue();
        if (list != null && index != null) {
            repository.evaluate(list.get(index).getSentenceId(), base64, evaluationResult, isProcessing);
        }
    }

    public void nextSentence() {
        Integer index = currentIndex.getValue();
        List<Sentence> list = sentences.getValue();
        if (index != null && list != null && index < list.size() - 1) {
            currentIndex.setValue(index + 1);
            evaluationResult.setValue(null);
        }
    }
}