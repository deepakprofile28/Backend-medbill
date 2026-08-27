package com.medbill.dto;

public class LoginResponse {

    private String userName;
    private String role;
    private String token;

    private Long companyId;
    private String companyName;

    public LoginResponse() {

    }

    public LoginResponse(
            String userName,
            String role,
            String token,
            Long companyId,
            String companyName) {

        this.userName = userName;
        this.role = role;
        this.token = token;
        this.companyId = companyId;
        this.companyName = companyName;
    }

    // ================= GETTERS =================

    public String getUserName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    // ================= SETTERS =================

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // ================= BUILDER =================

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String userName;
        private String role;
        private String token;

        private Long companyId;
        private String companyName;

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

        public Builder companyId(Long companyId) {
            this.companyId = companyId;
            return this;
        }

        public Builder companyName(String companyName) {
            this.companyName = companyName;
            return this;
        }

        public LoginResponse build() {

            return new LoginResponse(
                    userName,
                    role,
                    token,
                    companyId,
                    companyName
            );
        }
    }
}