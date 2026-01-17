package com.jobportal.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private String email;
    private int userId;
    private String userType;

    public AuthResponse() {
    }

    public AuthResponse(boolean success, String message, String email, int userId, String userType) {
        this.success = success;
        this.message = message;
        this.email = email;
        this.userId = userId;
        this.userType = userType;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
