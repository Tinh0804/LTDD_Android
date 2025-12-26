package com.example.learninglanguageapp.models;

public class Sentence {
    private int sentenceId;
    private String sentenceText;
    private String audioFile;
    private int orderIndex;

    // Getters and Setters
    public int getSentenceId() { return sentenceId; }
    public String getSentenceText() { return sentenceText; }
    public String getAudioFile() { return audioFile; }
}