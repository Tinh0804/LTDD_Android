package com.example.learninglanguageapp.models.Request;

public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private int courseId;
    private int nativeLanguageId;
    private int learnLanguageId;



    public RegisterRequest(String username, String password, String fullName,
                           String phoneNumber,int courseId, int nativeLanguageId, int learnLanguageId) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.nativeLanguageId = nativeLanguageId;
        this.learnLanguageId = learnLanguageId;
        this.courseId = courseId;
    }
}
