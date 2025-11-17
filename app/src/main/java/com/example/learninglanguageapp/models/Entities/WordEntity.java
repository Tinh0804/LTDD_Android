package com.example.learninglanguageapp.models.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "words")
public class WordEntity {

    @PrimaryKey
    public int wordId;

    public int languageId;
    public int lessonId;
    public String wordName;
    public String translation;
    public String pronunciation;
    public String wordType;
    public String audioFile;
    public String exampleSentence;
    public String imageUrl;
}
