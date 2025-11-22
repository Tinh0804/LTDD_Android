package com.example.learninglanguageapp.mapper;


import com.example.learninglanguageapp.models.Entities.ExerciseEntity;
import com.example.learninglanguageapp.models.Exercise;

public class ExerciseMapper {

    public static Exercise toDomain(ExerciseEntity entity) {
        Exercise exercise = new Exercise();
        exercise.setExerciseId(entity.exerciseId);
        exercise.setLessonId(entity.lessonId);
        exercise.setExerciseType(entity.exerciseType);
        exercise.setQuestion(entity.question);
        exercise.setCorrectAnswer(entity.correctAnswer);
        exercise.setAudioFile(entity.audioFile);
        exercise.setExperienceReward(entity.experienceReward);
        exercise.setOptions(entity.options);
        return exercise;
    }

    public static ExerciseEntity toEntity(Exercise domain) {
        ExerciseEntity entity = new ExerciseEntity();
        entity.exerciseId = domain.getExerciseId();
        entity.lessonId = domain.getLessonId();
        entity.exerciseType = domain.getExerciseType();
        entity.question = domain.getQuestion();
        entity.correctAnswer = domain.getCorrectAnswer();
        entity.audioFile = domain.getAudioFile();
        entity.experienceReward = domain.getExperienceReward();
        entity.options = domain.getOptions();
        return entity;
    }
}
