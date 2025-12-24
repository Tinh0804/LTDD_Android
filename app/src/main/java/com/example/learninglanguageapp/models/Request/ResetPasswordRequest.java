package com.example.learninglanguageapp.models.Request;

public class ResetPasswordRequest {
    public String email;
    public String resetCode;
    public String newPassword;

    public ResetPasswordRequest(String email, String resetCode, String newPassword) {
        this.email = email;
        this.resetCode = resetCode;
        this.newPassword = newPassword;
    }
}
