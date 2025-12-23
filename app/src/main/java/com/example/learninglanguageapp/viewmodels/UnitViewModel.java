package com.example.learninglanguageapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.UnitWithLessons;
import com.example.learninglanguageapp.repository.UnitRepository;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

import java.util.List;

public class UnitViewModel extends AndroidViewModel {
    private final UnitRepository repository;
    private final SharedPrefsHelper sharedPrefsHelper;
    private final MutableLiveData<List<UnitWithLessons>> unitsWithLessonsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public UnitViewModel(@NonNull Application application) {
        super(application);
        repository = new UnitRepository(application);
        sharedPrefsHelper = new SharedPrefsHelper(application);
        loadUnitsWithLessons();
    }

    public LiveData<List<UnitWithLessons>> getUnitsWithLessonsLiveData() {
        return unitsWithLessonsLiveData;
    }

    public LiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void loadUnitsWithLessons() {
        String token = sharedPrefsHelper.getToken();
        if (token != null) {
            String authHeader = "Bearer " + token;
            repository.loadUnitsWithLessons(authHeader, unitsWithLessonsLiveData, isLoading, errorLiveData);
        } else {
            errorLiveData.setValue("Chưa đăng nhập");
        }
    }
}
