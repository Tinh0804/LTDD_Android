package com.example.learninglanguageapp.models.Request;

public class SubmitLessonRequest {
    private int lessonId;
    private int courseId;
    private int totalExercisesCompleted;
    private int totalExperienceEarned;

    public SubmitLessonRequest(int lessonId, int courseId, int totalExercisesCompleted, int totalExperienceEarned) {
        this.lessonId = lessonId;
        this.courseId = courseId;
        this.totalExercisesCompleted = totalExercisesCompleted;
        this.totalExperienceEarned = totalExperienceEarned;
    }
}