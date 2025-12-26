package com.example.learninglanguageapp.models.Response;

public class PronunciationResponse {
    private double overall_score;
    private double accuracy_score;
    private String feedback;

    public PronunciationResponse() {}

    public double getOverall_score() { return overall_score; }
    public void setOverall_score(double overall_score) { this.overall_score = overall_score; }

    public double getAccuracy_score() { return accuracy_score; }
    public void setAccuracy_score(double accuracy_score) { this.accuracy_score = accuracy_score; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
}
