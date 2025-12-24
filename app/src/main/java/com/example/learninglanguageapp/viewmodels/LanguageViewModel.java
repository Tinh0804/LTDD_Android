package com.example.learninglanguageapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Response.LanguageResponse;
import com.example.learninglanguageapp.repository.LanguageRepository;

public class LanguageViewModel extends AndroidViewModel {

    private final LanguageRepository repository;

    private final MutableLiveData<String> languageName = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LanguageViewModel(@NonNull Application application) {
        super(application);
        repository = new LanguageRepository(application);
    }

    public MutableLiveData<String> getLanguageName() {
        return languageName;
    }

    public MutableLiveData<String> getError() {
        return error;
    }

    public void loadLanguage(int languageId) {
        repository.getLanguageById(languageId,
                response -> languageName.setValue(response.getLanguageName()),
                error
        );
    }
}
