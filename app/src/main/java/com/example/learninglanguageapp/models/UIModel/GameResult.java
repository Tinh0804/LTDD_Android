package com.example.learninglanguageapp.models.UIModel;


public class GameResult {
    private int totalWords;
    private int correctAnswers;
    private int wrongAnswers;
    private int heartsLost;
    private long timeTaken;

    public GameResult() {}

    public GameResult(int totalWords, int correctAnswers, int wrongAnswers, int heartsLost, long timeTaken) {
        this.totalWords = totalWords;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.heartsLost = heartsLost;
        this.timeTaken = timeTaken;
    }

    // Getters and Setters
    public int getTotalWords() { return totalWords; }
    public void setTotalWords(int totalWords) { this.totalWords = totalWords; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public int getWrongAnswers() { return wrongAnswers; }
    public void setWrongAnswers(int wrongAnswers) { this.wrongAnswers = wrongAnswers; }

    public int getHeartsLost() { return heartsLost; }
    public void setHeartsLost(int heartsLost) { this.heartsLost = heartsLost; }

    public long getTimeTaken() { return timeTaken; }
    public void setTimeTaken(long timeTaken) { this.timeTaken = timeTaken; }
}