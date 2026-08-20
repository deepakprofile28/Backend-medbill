package com.medbill.dto;

public class LoginResponse {

    private String userName;
    private String role;
    private String token;

    public LoginResponse() {
    }

    public LoginResponse(String userName, String role, String token) {
        this.userName = userName;
        this.role = role;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String userName;
        private String role;
        private String token;

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder role(String role) {
            this.role = role;
            return this;
        }

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public LoginResponse build() {
            return new LoginResponse(userName, role, token);
        }
    }
}