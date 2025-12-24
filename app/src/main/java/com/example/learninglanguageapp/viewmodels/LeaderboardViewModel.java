package com.example.learninglanguageapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.learninglanguageapp.models.Entities.TopUserEntity;
import com.example.learninglanguageapp.repository.LeaderboardRepository;
import com.example.learninglanguageapp.utils.SharedPrefsHelper;

import java.util.List;

public class LeaderboardViewModel extends AndroidViewModel {

    private final LeaderboardRepository repository;
    private final SharedPrefsHelper prefs;

    private final MutableLiveData<List<TopUserEntity>> leaderboard = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final MutableLiveData<String> error = new MutableLiveData<>();

    public LeaderboardViewModel(@NonNull Application application) {
        super(application);
        repository = new LeaderboardRepository(application);
        prefs = new SharedPrefsHelper(application);
        loadLeaderboard();
    }

    public LiveData<List<TopUserEntity>> getLeaderboard() {
        return leaderboard;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadLeaderboard() {
        String token = prefs.getToken();
        if (token == null) {
            error.setValue("Chưa đăng nhập");
            return;
        }

        repository.loadLeaderboard(
                "Bearer " + token,
                leaderboard::setValue,
                isLoading,
                error
        );
    }
}
