package com.example.learninglanguageapp.firebase;

import android.content.Context;
import android.os.Bundle;

import com.example.learninglanguageapp.utils.Constants;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AnalyticsManager {

    private static AnalyticsManager instance;
    private FirebaseAnalytics analytics;

    private AnalyticsManager(Context context) {
        this.analytics = FirebaseAnalytics.getInstance(context);
    }

    public static synchronized AnalyticsManager getInstance(Context context) {
        if (instance == null) {
            instance = new AnalyticsManager(context);
        }
        return instance;
    }

    public void logLessonStarted(String lessonId, String courseId) {
        Bundle params = new Bundle();
        params.putString("lesson_id", lessonId);
        params.putString("course_id", courseId);
        params.putLong("timestamp", System.currentTimeMillis());

        analytics.logEvent(Constants.EVENT_LESSON_STARTED, params);
    }

    public void logLessonCompleted(String lessonId, String courseId,
                                   int score, long timeSpent) {
        Bundle params = new Bundle();
        params.putString("lesson_id", lessonId);
        params.putString("course_id", courseId);
        params.putInt("score", score);
        params.putLong("time_spent", timeSpent);

        analytics.logEvent(Constants.EVENT_LESSON_COMPLETED, params);
    }

    public void logQuizCompleted(String quizId, int correctAnswers,
                                 int totalQuestions, long timeSpent) {
        Bundle params = new Bundle();
        params.putString("quiz_id", quizId);
        params.putInt("correct_answers", correctAnswers);
        params.putInt("total_questions", totalQuestions);
        params.putLong("time_spent", timeSpent);
        params.putDouble("accuracy", (double) correctAnswers / totalQuestions);

        analytics.logEvent(Constants.EVENT_QUIZ_COMPLETED, params);
    }

    public void logLevelUp(int newLevel, int totalXp) {
        Bundle params = new Bundle();
        params.putInt("new_level", newLevel);
        params.putInt("total_xp", totalXp);

        analytics.logEvent(Constants.EVENT_LEVEL_UP, params);
    }

    public void logStreakAchieved(int streakDays) {
        Bundle params = new Bundle();
        params.putInt("streak_days", streakDays);

        analytics.logEvent(Constants.EVENT_STREAK_ACHIEVED, params);
    }

    public void setUserProperty(String name, String value) {
        analytics.setUserProperty(name, value);
    }

    public void setUserId(String userId) {
        analytics.setUserId(userId);
    }

    public void logEvent(String userLogin, Object o) {
        analytics.logEvent(userLogin, null);

    }
}