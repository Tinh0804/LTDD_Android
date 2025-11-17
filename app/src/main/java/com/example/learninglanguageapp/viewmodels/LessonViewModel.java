package com.example.learninglanguageapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.repository.LessonRepository;

import java.util.List;

public class LessonViewModel extends ViewModel {

    private LessonRepository repository;

    private MutableLiveData<List<Word>> wordsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public LessonViewModel() {
        repository = new LessonRepository();
    }

    public MutableLiveData<List<Word>> getWordsLiveData() {
        return wordsLiveData;
    }

    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadWords(int lessonId, int userId) {
        repository.getWords(lessonId, wordsLiveData, isLoading, errorLiveData);
    }
}
