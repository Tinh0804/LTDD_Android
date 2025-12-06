package com.example.learninglanguageapp.storage.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.learninglanguageapp.models.Entities.WordEntity;

import java.util.List;

@Dao
public interface WordDao {

    @Query("SELECT * FROM words WHERE lessonId = :lessonId")
    List<WordEntity> getWordsByLesson(int lessonId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWords(List<WordEntity> words);

    @Query("DELETE FROM words WHERE lessonId = :lessonId")
    void deleteWordsByLesson(int lessonId);
}
