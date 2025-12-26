package com.example.learninglanguageapp.models.Response;

public class SubmitLessonResponse {
    private int completedLessonId;
    private int lessonExperienceReward;
    private int nextLessonId;
    private int nextUnitId;
    private boolean isCourseCompleted;
    private boolean isUnlocked; // Biến này là private
    private String message;

    public int getLessonExperienceReward() {
        return lessonExperienceReward;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public String getMessage() {
        return message;
    }

}