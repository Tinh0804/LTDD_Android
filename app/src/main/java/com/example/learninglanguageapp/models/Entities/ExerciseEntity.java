package com.example.learninglanguageapp.models.Entities;



import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.learninglanguageapp.utils.Converters;

import java.util.List;

@Entity(tableName = "exercises")
@TypeConverters(Converters.class)
public class ExerciseEntity {

    @PrimaryKey
    public int exerciseId;
    public int lessonId;
    public int unitId;
    public int orderIndex;
    public String exerciseType;
    public String question;
    public String audioFile;
    public String correctAnswer;
    public int experienceReward;


    public List<String> options;
}
