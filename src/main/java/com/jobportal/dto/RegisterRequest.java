package com.jobportal.dto;

public class RegisterRequest {
    private String fullName;
    private String email;
    private String password;
    private int userTypeId;

    public RegisterRequest() {
    }

    public RegisterRequest(String fullName, String email, String password, int userTypeId) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.userTypeId = userTypeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserTypeId() {
        return userTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        this.userTypeId = userTypeId;
    }
}
