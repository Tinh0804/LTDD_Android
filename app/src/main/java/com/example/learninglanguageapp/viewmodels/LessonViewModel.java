package com.example.learninglanguageapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.repository.LessonRepository;

import java.util.List;

public class LessonViewModel extends AndroidViewModel {
    private static final String TAG = "WordViewModel";

    private LessonRepository repository;

    private MutableLiveData<List<Word>> wordsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Lesson>> lessonsLiveData = new MutableLiveData<>();

    public LessonViewModel(@NonNull Application application) {
        super(application);
        repository = new LessonRepository(application);
    }

    public MutableLiveData<List<Word>> getWordsLiveData() {
        return wordsLiveData;
    }

    public LiveData<List<Lesson>> getLessons() { return lessonsLiveData; }
    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return errorLiveData; }
    public void loadWords(int lessonId) {
        repository.getWords(lessonId, wordsLiveData, isLoading, errorLiveData);
    }
    public void fetchLessons(int unitId) {
        repository.getLessonsByUnit(unitId, lessonsLiveData, isLoading, errorLiveData);
    }
}
