package com.example.learninglanguageapp.repository;
import android.content.Context;

import com.example.learninglanguageapp.models.Lesson;
import com.example.learninglanguageapp.models.Unit;
import com.example.learninglanguageapp.models.Word;
import com.example.learninglanguageapp.network.ApiClient;
import com.example.learninglanguageapp.network.ApiService;

import java.util.List;

import retrofit2.Call;

public class LessonRepository extends BaseRepository{
    public LessonRepository(Context context){
        super(context);
    }
    // LẤY DANH SÁCH UNIT
    public void getAllUnits(OnDataLoadedListener<List<Unit>> onSuccess, OnErrorListener onError, boolean forceRefresh) {
        Call<List<Unit>> call = apiService.getAllUnits();
        syncData(call, onSuccess, onError, forceRefresh);
    }

    //  LẤY DANH SÁCH LESSON CỦA 1 UNIT 
    public void getLessonsByUnit(int unitId, OnDataLoadedListener<List<Lesson>> onSuccess, OnErrorListener onError, boolean forceRefresh) {
        Call<List<Lesson>> call = apiService.getLessonsByUnit(unitId);
        syncData(call, onSuccess, onError, forceRefresh);
    }

    //LẤY DANH SÁCH TỪ VỰNG TRONG 1 LESSON
    public void getWordsByLesson(int lessonId, OnDataLoadedListener<List<Word>> onSuccess, OnErrorListener onError, boolean forceRefresh) {
        Call<List<Word>> call = apiService.getWordsByLesson(lessonId);
        syncData(call, onSuccess, onError, forceRefresh);
    }

    //Triển khai cache (nếu có)
    @Override
    protected <T> T loadFromLocal() {
        // TODO: đọc từ LocalStorageManager (nếu cần)
        return null;
    }

    @Override
    protected <T> void saveToLocal(T data) {
        // TODO: lưu dữ liệu vào LocalStorageManager (nếu cần)
    }
}
