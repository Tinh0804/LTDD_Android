package com.example.learninglanguageapp.storage.DAOs;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.learninglanguageapp.models.Entities.ExerciseEntity;

import java.util.List;

@Dao
public interface ExerciseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExerciseEntity> exercises);

    @Query("SELECT * FROM exercises WHERE lessonId = :lessonId AND exerciseType = :type")
    List<ExerciseEntity> getExercisesByLessonAndType(int lessonId, String type);

    @Query("SELECT * FROM exercises WHERE unitId = :unitId")
    List<ExerciseEntity> getExercisesByUnitId(int unitId);
    @Query("SELECT * FROM exercises WHERE lessonId = :lessonId")
    List<ExerciseEntity> getExercisesByLesson(List<Integer> lessonId);


    @Query("SELECT * FROM exercises WHERE exerciseId = :exerciseId LIMIT 1")
    ExerciseEntity getExercise(int exerciseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertExercises(List<ExerciseEntity> exercises);

    @Query("DELETE FROM exercises")
    void clear();
}
