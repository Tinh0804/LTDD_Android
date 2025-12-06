// models/Response/Result.java
package com.example.learninglanguageapp.models.Response;

public class Result<T> {
    private T value;
    private Exception exception;
    private boolean success;
    private boolean loading;

    private Result(T value, Exception exception, boolean success, boolean loading) {
        this.value = value;
        this.exception = exception;
        this.success = success;
        this.loading = loading;
    }

    // Các factory method
    public static <T> Result<T> success(T value) {
        return new Result<>(value, null, true, false);
    }

    public static <T> Result<T> failure(Exception exception) {
        return new Result<>(null, exception, false, false);
    }

    public static <T> Result<T> loading() {
        return new Result<>(null, null, false, true);
    }

    // GETTER – BẮT BUỘC PHẢI CÓ NHÉ!!!
    public boolean isSuccess() { return success; }
    public boolean isLoading() { return loading; }
    public T getValue() { return value; }
    public Exception getException() { return exception != null ? exception : new Exception("Unknown error"); }
}