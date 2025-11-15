package com.example.learninglanguageapp.models.Response;

public class ApiResponse<T> {
    private boolean status;
    private String message;
    private T data;

    public boolean isStatus() { return status; }
    public String getMessage() { return message; }
    public T getData() { return data; }
}
