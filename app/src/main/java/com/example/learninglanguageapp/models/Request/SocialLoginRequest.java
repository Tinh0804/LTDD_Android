package com.example.learninglanguageapp.models.Request;

public class SocialLoginRequest {
    public String provider;
    public String accessToken;

    public SocialLoginRequest(String provider, String accessToken) {
        this.provider = provider;
        this.accessToken = accessToken;
    }


}