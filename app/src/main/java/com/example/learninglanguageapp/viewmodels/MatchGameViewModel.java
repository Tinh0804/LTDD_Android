package com.example.learninglanguageapp.viewmodels;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.repository.LessonRepository;

import java.util.List;
import java.util.function.Consumer;

public class MatchGameViewModel extends ViewModel {

    private final LessonRepository repository;

    private final MutableLiveData<List<Word>> wordsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    // Constructor nhận Context (dùng khi tạo bằng Factory)
    public MatchGameViewModel(Context context) {
        this.repository = new LessonRepository(context.getApplicationContext());
    }

    // Constructor không tham số (nếu dùng Hilt/DI sau này)
    public MatchGameViewModel() {
        this.repository = new LessonRepository(null); // sẽ lỗi nếu dùng, nhưng giữ để tương thích DI
    }

    public void loadWordsForGame(int lessonId) {
        // Tránh gọi nhiều lần
        if (loadingLiveData.getValue() == Boolean.TRUE) return;

        loadingLiveData.setValue(true);
        errorLiveData.setValue(null);

        repository.getWordsForMatchGame(
                lessonId,
                words -> {
                    loadingLiveData.postValue(false);
                    if (words != null && words.size() >= 6) {
                        wordsLiveData.postValue(words);
                    } else {
                        errorLiveData.postValue("Không đủ từ để chơi (cần ít nhất 6 từ)");
                    }
                },
                error -> {
                    loadingLiveData.postValue(false);
                    errorLiveData.postValue(error);
                }
        );
    }

    // Getter
    public MutableLiveData<List<Word>> getWordsLiveData() {
        return wordsLiveData;
    }

    public MutableLiveData<Boolean> getLoadingLiveData() {
        return loadingLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    // Optional: Reset khi cần
    public void reset() {
        wordsLiveData.setValue(null);
        errorLiveData.setValue(null);
        loadingLiveData.setValue(false);
    }
}

