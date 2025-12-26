package com.example.learninglanguageapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Request.SubmitLessonRequest;
import com.example.learninglanguageapp.models.Response.SubmitLessonResponse;
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

    private MutableLiveData<SubmitLessonResponse> submitResult = new MutableLiveData<>();
    private final MutableLiveData<SubmitLessonResponse> submitResultLiveData = new MutableLiveData<>();
    public LiveData<SubmitLessonResponse> getSubmitResult() { return submitResult; }

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
    public LiveData<SubmitLessonResponse> getSubmitResultLiveData() {
        return submitResultLiveData;
    }

    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getError() { return errorLiveData; }
    public void loadWords(int lessonId) {
        repository.getWords(lessonId, wordsLiveData, isLoading, errorLiveData);
    }
    public void fetchLessons(int unitId) {
        repository.getLessonsByUnit(unitId, lessonsLiveData, isLoading, errorLiveData);
    }

    public void submitLessonCompletion(int lessonId, int totalWords, String token) {
        SubmitLessonRequest request = new SubmitLessonRequest(lessonId, 1, totalWords, totalWords * 10);
        repository.submitLesson(token, request, submitResultLiveData, isLoading, errorLiveData);
    }
}
