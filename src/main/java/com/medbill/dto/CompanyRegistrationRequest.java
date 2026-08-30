package com.medbill.dto;

public class CompanyRegistrationRequest {

    private String companyName;
    private String name;
    private String businessEmail;
    private String ownerEmail;
    private String email;
    private String countryCode;
    private String mobile;
    private String businessPhone;
    private String ownerMobile;
    private String ownerCountryCode;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String invoicePrefix;
    private String ownerName;
    private String password;
    private String status;
    private String plan;

    public CompanyRegistrationRequest() {
    }

    // Helper getters that fallback cleanly
    public String getEffectiveCompanyName() {
        if (companyName != null && !companyName.trim().isEmpty()) {
            return companyName.trim();
        }
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        return "Pharmacy";
    }

    public String getEffectiveEmail() {
        if (email != null && !email.trim().isEmpty()) {
            return email.trim().toLowerCase();
        }
        if (businessEmail != null && !businessEmail.trim().isEmpty()) {
            return businessEmail.trim().toLowerCase();
        }
        if (ownerEmail != null && !ownerEmail.trim().isEmpty()) {
            return ownerEmail.trim().toLowerCase();
        }
        return "";
    }

    public String getEffectiveCountryCode() {
        if (countryCode != null && !countryCode.trim().isEmpty()) {
            return countryCode.trim();
        }
        if (ownerCountryCode != null && !ownerCountryCode.trim().isEmpty()) {
            return ownerCountryCode.trim();
        }
        return "+91";
    }

    public String getEffectiveMobile() {
        if (mobile != null && !mobile.trim().isEmpty()) {
            return mobile.replaceAll("\\D", "");
        }
        if (businessPhone != null && !businessPhone.trim().isEmpty()) {
            return businessPhone.replaceAll("\\D", "");
        }
        if (ownerMobile != null && !ownerMobile.trim().isEmpty()) {
            return ownerMobile.replaceAll("\\D", "");
        }
        return "";
    }

    public String getEffectiveOwnerName() {
        if (ownerName != null && !ownerName.trim().isEmpty()) {
            return ownerName.trim();
        }
        if (name != null && !name.trim().isEmpty()) {
            return name.trim();
        }
        return getEffectiveCompanyName();
    }

    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessEmail() {
        return businessEmail;
    }

    public void setBusinessEmail(String businessEmail) {
        this.businessEmail = businessEmail;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBusinessPhone() {
        return businessPhone;
    }

    public void setBusinessPhone(String businessPhone) {
        this.businessPhone = businessPhone;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public String getOwnerCountryCode() {
        return ownerCountryCode;
    }

    public void setOwnerCountryCode(String ownerCountryCode) {
        this.ownerCountryCode = ownerCountryCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getInvoicePrefix() {
        return invoicePrefix;
    }

    public void setInvoicePrefix(String invoicePrefix) {
        this.invoicePrefix = invoicePrefix;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}

