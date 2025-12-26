package com.example.learninglanguageapp.models.Request;

public class ExerciseSubmitRequest {
    public int lessonId;
    public int courseId;
    public int totalExercises;
    public int correctAnswers;
    public int experienceEarned;

    public ExerciseSubmitRequest(int lessonId, int courseId, int totalExercises, int correctAnswers, int experienceEarned) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.totalExercises = totalExercises;
        this.correctAnswers = correctAnswers;
        this.experienceEarned = experienceEarned;
    }
}