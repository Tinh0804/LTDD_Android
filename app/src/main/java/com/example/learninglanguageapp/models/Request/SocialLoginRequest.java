// File: models/Request/SocialLoginRequest.java
package com.example.learninglanguageapp.models.Request;

public class SocialLoginRequest {
    private String provider;
    private String accessToken;

    public SocialLoginRequest(String provider, String accessToken) {
        this.provider = provider;
        this.accessToken = accessToken;
    }

    // Getter (nếu cần)
    public String getProvider() { return provider; }
    public String getAccessToken() { return accessToken; }
}