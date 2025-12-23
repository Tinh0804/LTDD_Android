package com.example.learninglanguageapp.viewmodels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.learninglanguageapp.models.Course;
import com.example.learninglanguageapp.repository.CourseRepository;

import java.util.List;

public class CourseViewModel extends ViewModel {
    private CourseRepository repository;

    private final MutableLiveData<List<Course>> courses = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public void init(Context context) {
        if (repository == null) {
            repository = new CourseRepository(context);
        }
    }

    public void fetchCourses(int fromId, int toId) {
        repository.getCourses(fromId, toId, courses, isLoading, errorMessage);
    }

    // Getters
    public LiveData<List<Course>> getCourses() { return courses; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
}