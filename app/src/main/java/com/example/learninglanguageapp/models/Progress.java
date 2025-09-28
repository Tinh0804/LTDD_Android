package com.example.learninglanguageapp.models;


import java.util.List;

public class Progress {
    private int currentLevel;
    private int totalXp;
    private int streak;
    private long lastActivity; // timestamp millis
    private List<String> completedLessons;
    private boolean modified;

    public Progress() {
        // required empty constructor for Firestore
    }

    public Progress(int currentLevel, int totalXp, int streak, long lastActivity, List<String> completedLessons) {
        this.currentLevel = currentLevel;
        this.totalXp = totalXp;
        this.streak = streak;
        this.lastActivity = lastActivity;
        this.completedLessons = completedLessons;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getTotalXp() {
        return totalXp;
    }

    public void setTotalXp(int totalXp) {
        this.totalXp = totalXp;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public long getLastActivity() {
        return lastActivity;
    }

    public void setLastActivity(long lastActivity) {
        this.lastActivity = lastActivity;
    }

    public List<String> getCompletedLessons() {
        return completedLessons;
    }

    public void setCompletedLessons(List<String> completedLessons) {
        this.completedLessons = completedLessons;
    }

    public void setModified(boolean b) {
        this.modified = b;

    }
    public boolean isModified() {
        return modified;

    }
}