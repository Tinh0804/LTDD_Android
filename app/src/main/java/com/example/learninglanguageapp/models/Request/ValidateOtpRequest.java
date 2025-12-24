package com.example.learninglanguageapp.models.Request;

public class ValidateOtpRequest {
    public String email;
    public String resetCode;
    public String newPassword;

    public ValidateOtpRequest(String email, String resetCode, String newPassword) {
        this.email = email;
        this.resetCode = resetCode;
        this.newPassword = newPassword;
    }
}
