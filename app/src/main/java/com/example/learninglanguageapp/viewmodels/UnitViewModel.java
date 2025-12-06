package com.example.learninglanguageapp.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.learninglanguageapp.models.UnitWithLessons;
import com.example.learninglanguageapp.repository.UnitRepository;
import java.util.List;

public class UnitViewModel extends AndroidViewModel {
    private UnitRepository repository;
    private MutableLiveData<List<UnitWithLessons>> unitsWithLessonsLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    public UnitViewModel(@NonNull Application application) {
        super(application);
        repository = new UnitRepository(application);
        loadUnitsWithLessons();
    }
    public MutableLiveData<List<UnitWithLessons>> getUnitsWithLessonsLiveData() {
        return unitsWithLessonsLiveData;
    }
    public MutableLiveData<Boolean> getIsLoadingLiveData() {
        return isLoading;
    }
    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
    public void loadUnitsWithLessons() {
        repository.loadUnitsWithLessons(unitsWithLessonsLiveData, isLoading, errorLiveData);
    }
}