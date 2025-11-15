package com.example.learninglanguageapp.models.Response;
public class Result<T> {
    private T value;
    private Exception exception;

    private Result(T value, Exception exception) {
        this.value = value;
        this.exception = exception;
    }

    public static <T> Result<T> success(T value) {
        return new Result<>(value, null);
    }

    public static <T> Result<T> failure(Exception exception) {
        return new Result<>(null, exception);
    }

    public boolean isSuccess() { return exception == null; }
    public T getValue() { return value; }
    public Exception getException() { return exception; }
}
