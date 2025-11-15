package com.example.learninglanguageapp.models.Request;

public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private int nativeLanguagueId;
    private int learnLanguageId;
    private int courseId;


    public RegisterRequest(String username, String password, String fullName,
                           String phoneNumber, int nativeLanguagueId, int learnLanguageId,int courseId) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.nativeLanguagueId = nativeLanguagueId;
        this.learnLanguageId = learnLanguageId;
        this.courseId = courseId;
    }
}
