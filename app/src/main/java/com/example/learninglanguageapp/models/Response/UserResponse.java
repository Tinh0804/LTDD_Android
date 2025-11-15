package com.example.learninglanguageapp.models.Response;

// User Response
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String fullName;
    // add các field khác nếu API có

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }

}
