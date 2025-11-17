package com.example.learninglanguageapp.viewmodels;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.repository.LessonRepository;

import java.util.List;

public class LessonViewModel extends ViewModel {
    private static final String TAG = "WordViewModel";

    private final LessonRepository repository;
    private final MutableLiveData<List<Word>> wordsLiveData;
    private final MutableLiveData<Boolean> isLoadingLiveData;
    private final MutableLiveData<String> errorLiveData;

    public LessonViewModel() {
        repository = new LessonRepository();
        wordsLiveData = new MutableLiveData<>();
        isLoadingLiveData = new MutableLiveData<>(false);
        errorLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Word>> getWordsLiveData() {
        return wordsLiveData;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoadingLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadWords(int lessonId, int userId) {
        isLoadingLiveData.setValue(true);
        Log.d(TAG, "Loading words for lesson: " + lessonId + ", user: " + userId);

        repository.getWordsByLesson(lessonId, userId, new LessonRepository.WordCallback() {
            @Override
            public void onSuccess(List<Word> words) {
                isLoadingLiveData.setValue(false);
                wordsLiveData.setValue(words);
                Log.d(TAG, "Loaded " + words.size() + " words successfully");
            }

            @Override
            public void onError(String error) {
                isLoadingLiveData.setValue(false);
                errorLiveData.setValue(error);
                Log.e(TAG, "Error loading words: " + error);
            }
        });
    }
}